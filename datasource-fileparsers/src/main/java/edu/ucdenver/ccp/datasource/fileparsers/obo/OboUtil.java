/*
 Copyright (c) 2012, Regents of the University of Colorado
 All rights reserved.

 Redistribution and use in source and binary forms, with or without modification, 
 are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this 
    list of conditions and the following disclaimer.
   
 * Redistributions in binary form must reproduce the above copyright notice, 
    this list of conditions and the following disclaimer in the documentation 
    and/or other materials provided with the distribution.
   
 * Neither the name of the University of Colorado nor the names of its 
    contributors may be used to endorse or promote products derived from this 
    software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
 ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package edu.ucdenver.ccp.datasource.fileparsers.obo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.geneontology.oboedit.dataadapter.DefaultOBOParser;
import org.geneontology.oboedit.dataadapter.OBOParseEngine;
import org.geneontology.oboedit.dataadapter.OBOParseException;
import org.geneontology.oboedit.datamodel.LinkedObject;
import org.geneontology.oboedit.datamodel.OBOClass;
import org.geneontology.oboedit.datamodel.OBOSession;
import org.geneontology.oboedit.datamodel.Synonym;
import org.geneontology.oboedit.datamodel.impl.OBORestrictionImpl;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.collections.IteratorUtil;
import edu.ucdenver.ccp.common.collections.LegacyCollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileWriterUtil;
import edu.ucdenver.ccp.common.file.FileWriterUtil.FileSuffixEnforcement;
import edu.ucdenver.ccp.common.file.FileWriterUtil.WriteMode;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineIterator;
import edu.ucdenver.ccp.datasource.identifiers.obo.OntologyID;

/**
 * Utility class for dealing with OBO files.
 * 
 * @author Bill Baumgartner
 * @param <T>
 * 
 */
public class OboUtil<T extends OntologyID> {
	private static final Logger logger = Logger.getLogger(OboUtil.class);

	private OBOSession session;

	public OboUtil(File oboFile, CharacterEncoding encoding) throws IOException {
		session = initSession(oboFile, encoding);
	}

	public Iterator<OBOClass> getClassIterator() {
		return IteratorUtil.consolidate(
				LegacyCollectionsUtil.checkIterator(session.getTerms().iterator(), OBOClass.class),
				LegacyCollectionsUtil.checkIterator(session.getObsoleteTerms().iterator(), OBOClass.class));
	}

	/**
	 * This method taken directly from the OBO-Edit FAQ webpage:
	 * http://wiki.geneontology.org/index.php/OBO-Edit:_OBO_Parser_-_Getting_Started
	 * 
	 * @param path
	 * @return an OBOSession object containing the contents of the input obo file
	 * @throws OBOParseException
	 * @throws IOException
	 */
	private OBOSession initSession(File oboFile, CharacterEncoding encoding) throws IOException {
		File modifiedOboFile = removeConsiderTags(oboFile, encoding);
		DefaultOBOParser parser = new DefaultOBOParser();
		OBOParseEngine engine = new OBOParseEngine(parser);
		/*
		 * OBOParseEngine can parse several files at once and create one munged-together ontology,
		 * so we need to provide a Collection to the setPaths() method
		 */
		engine.setPaths(CollectionsUtil.createList(modifiedOboFile.getAbsolutePath()));
		try {
			engine.parse();
		} catch (OBOParseException e) {
			throw new IOException(e);
		}
		return parser.getSession();
	}

	/**
	 * There appears to be an issue with unresolved identifiers in consider: tags. This method
	 * simply creates a copy of the obo file with the consider tags removed.
	 * 
	 * @param oboFile
	 * @return
	 * @throws IOException
	 */
	private static File removeConsiderTags(File oboFile, CharacterEncoding encoding) throws IOException {
		File modifiedOboFile = new File(oboFile.getParentFile(), oboFile.getName() + ".mod");
		BufferedWriter writer = null;
		try {
			writer = FileWriterUtil.initBufferedWriter(modifiedOboFile, encoding, WriteMode.OVERWRITE,
					FileSuffixEnforcement.OFF);
			for (StreamLineIterator lineIter = new StreamLineIterator(oboFile, encoding); lineIter.hasNext();) {
				Line line = lineIter.next();
				if (!line.getText().startsWith("consider:")) {
					writer.write(line.getText());
					writer.newLine();
				}
			}
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
		return modifiedOboFile;
	}

	/**
	 * Returns a mapping from term name/synonym/id string to an Ontology ID
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Set<T>> getTermName2IdMap(String idPrefix) {
		Map<String, OBOClass> termID2OboClassMap = getTermID2OboClassMap();
		Map<String, Set<T>> synonym2NameMap = new HashMap<String, Set<T>>();
		for (Entry<String, OBOClass> entry : termID2OboClassMap.entrySet()) {
			String termID = entry.getKey();
			if (termID.startsWith(idPrefix)) {
				T ontologyID = (T) OntologyIdResolver.resolveOntologyID(termID);
				OBOClass oboClass = entry.getValue();
				String termName = oboClass.getName().toLowerCase();

				addTermIdPairToMap(synonym2NameMap, ontologyID, termName);

				Set<Synonym> synonyms = oboClass.getSynonyms();
				for (Synonym synonym : synonyms) {
					String synonymText = synonym.getText();
					addTermIdPairToMap(synonym2NameMap, ontologyID, synonymText);
				}

				synonym2NameMap.put(termID.toLowerCase(), CollectionsUtil.createSet(ontologyID));
			}
		}

		return synonym2NameMap;
	}

	/**
	 * Addes a synonym/ontology ID pair to the input map. Throws a RuntimeException if the synonym
	 * already exists in the map.
	 * 
	 * @param synonym2NameMap
	 * @param ontologyID
	 * @param termName
	 */
	@SuppressWarnings("unchecked")
	private void addTermIdPairToMap(Map<String, Set<T>> synonym2NameMap, T ontologyID, String termName) {
		if (!synonym2NameMap.containsKey(termName))
			synonym2NameMap.put(termName, CollectionsUtil.createSet(ontologyID));
		else {
			if (!synonym2NameMap.get(termName).contains(ontologyID)) {
				synonym2NameMap.get(termName).add(ontologyID);
				logger.warn(String.format("Duplicate term name detected: '%s' maps to multiple term ids: %s", termName,
						synonym2NameMap.get(termName).toString()));
			}
		}
	}

	/**
	 * Returns a mapping from term ID string to OBOClass object for each class in the ontology. This
	 * method makes a point to include obsolete classes. It also removes "meta" terms such as
	 * obo:object[obo:object].
	 * 
	 * @return
	 */
	private Map<String, OBOClass> getTermID2OboClassMap() {
		@SuppressWarnings("unchecked")
		Map<String, OBOClass> termID2OboClassMap = session.getAllTermsHash();
		@SuppressWarnings("unchecked")
		Set<OBOClass> obsoleteTerms = session.getObsoleteTerms();
		for (OBOClass obsoleteTerm : obsoleteTerms) {
			termID2OboClassMap.put(obsoleteTerm.getID(), obsoleteTerm);
		}

		List<String> termIDsToRemove = new ArrayList<String>();
		/*
		 * Check for invalid term ids.The OBOEdit parser can make terms such as:
		 * "obo:object[obo:object]  isRoot:true  isObsolete:false"
		 */
		for (String termID : termID2OboClassMap.keySet()) {
			if (!isValidOntologyID(termID)) {
				termIDsToRemove.add(termID);
			}
		}

		for (String termID : termIDsToRemove) {
			termID2OboClassMap.remove(termID);
		}
		return termID2OboClassMap;
	}

	private static boolean isValidOntologyID(String termID) {
		Pattern termIDPattern = Pattern.compile("^[A-Z]+:\\d+$");
		Matcher m = termIDPattern.matcher(termID);
		/*
		 * Ensure we are returning a true ontology term. The OBOEdit parser can make terms such as:
		 * "obo:object[obo:object]  isRoot:true  isObsolete:false"
		 */
		return m.find();
	}

	public OBOSession getSession() {
		return session;
	}

	public Set<OBOClass> getAncestors(OBOClass childClass) {
		// session.get
		return null;
	}

	public boolean isDescendant(OBOClass childClass, OBOClass parentClass) {
		Set parents = childClass.getParents();
		boolean childIsDescendant = false;
		for (Object parent : parents) {
			if (!childIsDescendant) {
				if (parent instanceof OBORestrictionImpl) {
					OBORestrictionImpl parentImpl = (OBORestrictionImpl) parent;
					LinkedObject linkedObject = parentImpl.getParent();
					if (linkedObject instanceof OBOClass) {
						OBOClass childsParentClass = (OBOClass) linkedObject;
						if (childsParentClass.getID().equals(parentClass.getID()))
							childIsDescendant = true;
						else if (childsParentClass.isRoot())
							childIsDescendant = false;
						else
							childIsDescendant = isDescendant(childsParentClass, parentClass);
					}
				}
			}
		}
		return childIsDescendant;

		// /* If the flag to recurse is set to true, then process each child of the input OBOClass
		// */
		// if (recurse) {
		// Set childrenOfRoot = oboClass.getChildren();
		// for (Object child : childrenOfRoot) {
		// if (child instanceof OBORestrictionImpl) {
		// OBORestrictionImpl childImpl = (OBORestrictionImpl) child;
		// LinkedObject linkedObject = childImpl.getChild();
		// if (linkedObject instanceof OBOClass) {
		// OBOClass childClass = (OBOClass) linkedObject;
		// printOboClass(childClass, recurse, indent += 2);
		// }
		// } else {
		// warn("Unexpected class found in children set: " + child.getClass().getName());
		// }
		// }
		// }
	}

	public boolean isDescendent(String childId, String parentId) {
		OBOClass childClass = session.getTerm(childId);
		OBOClass parentClass = session.getTerm(parentId);
		if (childClass == null) {
			logger.warn("Null child class: " + childId);
			return false;
		}
		if (parentClass == null) {
			logger.warn("Null parent class: " + parentId);
			return false;
		}
		return isDescendant(childClass, parentClass);
	}

}
