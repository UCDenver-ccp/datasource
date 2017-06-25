package edu.ucdenver.ccp.datasource.fileparsers.transfac;

/*
 * #%L
 * Colorado Computational Pharmacology's common module
 * %%
 * Copyright (C) 2012 - 2015 Regents of the University of Colorado
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the Regents of the University of Colorado nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import edu.ucdenver.ccp.datasource.fileparsers.MultiLineFileRecord;
import edu.ucdenver.ccp.datasource.fileparsers.MultiLineFileRecordReader.MultiLineBuffer;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EmblID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.TransfacFactorID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.TransfacGeneID;

/**
 * This is an incomplete representation of the data contained in the Transfac gene.dat file record.
 * 
 * @author Bill Baumgartner
 * 
 */
@Record(dataSource = DataSource.TRANSFAC,isComplete =false, label="gene record (INC)")
public class TransfacGeneDatFileData extends MultiLineFileRecord {
	private static Logger logger = Logger.getLogger(TransfacGeneDatFileData.class);

	public static final String TRANSFAC_GENE_RECORD_NAME_PREFIX = "TRANSFAC_GENE_RECORD_";
	@RecordField
	private final TransfacGeneID transfacGeneID;
	@RecordField
	private final Set<TransfacFactorID> encodedFactorIDs;
	@RecordField
	private final NcbiGeneId entrezGeneDatabaseReferenceID;
	@RecordField
	private final MgiGeneID mgiDatabaseReferenceID;
	@RecordField
	private final Set<EmblID> emblDatabaseReferenceIDs;
	@RecordField
	private final Set<TransfacFactorID> bindingFactorIDs;

	private final static String ACCESSION_TAG = "AC";

	private final static String BINDING_SITE_TAG = "BS";

	private final static String DATABASE_REFERENCE_TAG = "DR";

	private final static String ENCODED_FACTOR_TAG = "FA";

	private final static String DATABASE_REFERENCE_ENTREZ_GENE_TAG = "ENTREZGENE:";

	private final static String DATABASE_REFERENCE_MGI_TAG = "MGI:";

	private final static String DATABASE_REFERENCE_EMBL_TAG = "EMBL:";

	public TransfacGeneDatFileData(TransfacGeneID transfacGeneID, Set<TransfacFactorID> encodedFactorIDs,
			NcbiGeneId entrezGeneDatabaseReferenceID, MgiGeneID mgiDatabaseReferenceID,
			Set<EmblID> emblDatabaseReferenceIDs, Set<TransfacFactorID> bindingFactorIDs, long byteOffset) {
		super(byteOffset);
		this.transfacGeneID = transfacGeneID;
		this.encodedFactorIDs = encodedFactorIDs;
		this.entrezGeneDatabaseReferenceID = entrezGeneDatabaseReferenceID;
		this.mgiDatabaseReferenceID = mgiDatabaseReferenceID;
		this.emblDatabaseReferenceIDs = emblDatabaseReferenceIDs;
		this.bindingFactorIDs = bindingFactorIDs;
	}

	public TransfacGeneID getTransfacGeneID() {
		return transfacGeneID;
	}

	public Set<TransfacFactorID> getEncodedFactorIDs() {
		return encodedFactorIDs;
	}

	public NcbiGeneId getEntrezGeneDatabaseReferenceID() {
		return entrezGeneDatabaseReferenceID;
	}

	public MgiGeneID getMgiDatabaseReferenceID() {
		return mgiDatabaseReferenceID;
	}

	public Set<EmblID> getEmblDatabaseReferenceIDs() {
		return emblDatabaseReferenceIDs;
	}

	public Set<TransfacFactorID> getBindingFactorIDs() {
		return bindingFactorIDs;
	}

	public static TransfacGeneDatFileData parseTransfacGeneDataRecord(MultiLineBuffer transfacDataRecordBuffer) {
		TransfacGeneID transfacGeneID = null;
		Set<TransfacFactorID> encodedFactorIDs = new HashSet<TransfacFactorID>();
		NcbiGeneId entrezGeneID = null;
		MgiGeneID mgiGeneID = null;
		Set<TransfacFactorID> bindingFactorIDs = new HashSet<TransfacFactorID>();
		Set<EmblID> emblIDs = new HashSet<EmblID>();

		String line;
		BufferedReader br = new BufferedReader(new StringReader(transfacDataRecordBuffer.toString()));
		try {
			while ((line = br.readLine()) != null) {
				if (line.startsWith(ACCESSION_TAG)) {
					transfacGeneID = new TransfacGeneID(getLineValue(line));
				} else if (line.startsWith(DATABASE_REFERENCE_TAG)) {
					String databaseRef = getLineValue(line);
					if (databaseRef != null) {
						if (databaseRef.startsWith(DATABASE_REFERENCE_ENTREZ_GENE_TAG)) {
							Pattern p = Pattern.compile(DATABASE_REFERENCE_ENTREZ_GENE_TAG + "\\s+(\\d+)");
							Matcher m = p.matcher(line);
							if (m.find()) {
								entrezGeneID = new NcbiGeneId(m.group(1));
							} else {
								logger.error("Unable to extract Entrez Gene ID from line: " + line);
							}
						} else if (databaseRef.startsWith(DATABASE_REFERENCE_MGI_TAG)) {
							Pattern p = Pattern.compile(DATABASE_REFERENCE_MGI_TAG + "\\s+(\\d+)");
							Matcher m = p.matcher(line);
							if (m.find()) {
								mgiGeneID = new MgiGeneID("MGI:" + m.group(1));
							} else {
								logger.error("Unable to extract MGI ID from line: " + line);
							}
						} else if (databaseRef.startsWith(DATABASE_REFERENCE_EMBL_TAG)) {
							Pattern p = Pattern.compile(DATABASE_REFERENCE_EMBL_TAG + "\\s+([^;\\.]+)[;\\.]");
							Matcher m = p.matcher(line);
							if (m.find()) {
								emblIDs.add(new EmblID(m.group(1)));
							} else {
								logger.error("Unable to extract EMBL ID from line: " + line);
							}
						}
					} else {
						logger.warn("Null database ref on line: " + line);
					}
				} else if (line.startsWith(BINDING_SITE_TAG)) {
					Pattern p = Pattern.compile("<(T\\d+)>");
					Matcher m = p.matcher(line);
					while (m.find()) {
						bindingFactorIDs.add(new TransfacFactorID(m.group(1)));
					}
				} else if (line.startsWith(ENCODED_FACTOR_TAG)) {
					Pattern p = Pattern.compile("(T\\d+);");
					Matcher m = p.matcher(line);
					if (m.find()) {
						encodedFactorIDs.add(new TransfacFactorID(m.group(1)));
					} else {
						logger.warn("Unable to find encoded transcription factor id on line: " + line);
					}
				}
			}

			return new TransfacGeneDatFileData(transfacGeneID, encodedFactorIDs, entrezGeneID, mgiGeneID, emblIDs,
					bindingFactorIDs, transfacDataRecordBuffer.getByteOffset());
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		} finally {
			IOUtils.closeQuietly(br);
		}
	}

	/**
	 * 
	 * @param line
	 * @return
	 */
	private static String getLineValue(String line) {
		return line.substring(2).trim();
	}

}
