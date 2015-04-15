/*
 * Copyright (C) 2009 Center for Computational Pharmacology, University of Colorado School of Medicine
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 */
package edu.ucdenver.ccp.fileparsers.ebi.interpro;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.InterProID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;

/**
 * Representation of data from InterPro protein2ipr.dat file
 * 
 * @author Bill Baumgartner
 * 
 */
@Record(dataSource = DataSource.INTERPRO,
		comment="Protein2IPR is all UniProtKB proteins and the InterPro entries and individual signatures they match, in a tab-delimited format. InterPro is a resource that provides functional analysis of protein sequences by classifying them into families and predicting the presence of domains and important sites. See: http://www.ebi.ac.uk/interpro/about.html;jsessionid=1DCC494ADDE7E06E21FB719FC2BFEC0B",
		license=License.EBI,
		citation="Sarah Hunter; Philip Jones; Alex Mitchell; Rolf Apweiler; Teresa K. Attwood; Alex Bateman; Thomas Bernard; David Binns; Peer Bork; Sarah Burge; Edouard de Castro; Penny Coggill; Matthew Corbett; Ujjwal Das; Louise Daugherty; Lauranne Duquenne; Robert D. Finn; Matthew Fraser; Julian Gough; Daniel Haft; Nicolas Hulo; Daniel Kahn; Elizabeth Kelly; Ivica Letunic; David Lonsdale; Rodrigo Lopez; Martin Madera; John Maslen; Craig McAnulla; Jennifer McDowall; Conor McMenamin; Huaiyu Mi; Prudence Mutowo-Muellenet; Nicola Mulder; Darren Natale; Christine Orengo; Sebastien Pesseat; Marco Punta; Antony F. Quinn; Catherine Rivoire; Amaia Sangrador-Vegas; Jeremy D. Selengut; Christian J. A. Sigrist; Maxim Scheremetjew; John Tate; Manjulapramila Thimmajanarthanan; Paul D. Thomas; Cathy H. Wu; Corin Yeats; Siew-Yit Yong (2011). InterPro in 2011: new developments in the family and domain prediction database. Nucleic Acids Research 2011; doi: 10.1093/nar/gkr948",
		label="protein2interpro record")
public class InterProProtein2IprDatFileData extends SingleLineFileRecord {

	public static final String INTERPRO_PROTEIN2IPR_RECORD_ID_PREFIX = "INTERPRO_PROTEIN_TO_IPR_RECORD_";

	@RecordField(comment="")
	private final UniProtID uniprotID;

	@RecordField(comment="")
	private final InterProID interProID;

	@RecordField(comment="")
	private final InterProName interProName;

	@RecordField(comment="")
	private final DataSourceIdentifier<String> externalReference;

	@RecordField(comment="")
	private final SequencePosition sequenceStartPosition;

	@RecordField(comment="")
	private final SequencePosition sequenceEndPosition;

	public InterProProtein2IprDatFileData(UniProtID uniprotID, InterProID interProID, InterProName interProName,
			DataSourceIdentifier<String> externalReference, SequencePosition sequenceStartPosition,
			SequencePosition sequenceEndPosition, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.uniprotID = uniprotID;
		this.interProID = interProID;
		this.interProName = interProName;
		this.externalReference = externalReference;
		this.sequenceStartPosition = sequenceStartPosition;
		this.sequenceEndPosition = sequenceEndPosition;
	}

	public UniProtID getUniProtID() {
		return uniprotID;
	}

	public InterProID getInterProID() {
		return interProID;
	}

	public InterProName getInterProName() {
		return interProName;
	}

	public static InterProProtein2IprDatFileData parseInterProProtein2IprDatLine(Line line) {
		Logger logger = Logger.getLogger(InterProProtein2IprDatFileData.class);

		try {
			String[] toks = line.getText().split("\\t");
			if (toks.length > 2) {
				UniProtID uniprotID = new UniProtID(toks[0]);
				InterProID interProID = new InterProID(toks[1]);
				InterProName interProName = new InterProName(toks[2]);
				DataSourceIdentifier<String> externalReference = InterProExternalReferenceFactory
						.parseExternalReference(toks[3]);
				SequencePosition startPosition = new SequencePosition(Integer.parseInt(toks[4]));
				SequencePosition endPosition = new SequencePosition(Integer.parseInt(toks[5]));
				return new InterProProtein2IprDatFileData(uniprotID, interProID, interProName, externalReference,
						startPosition, endPosition, line.getByteOffset(), line.getLineNumber());
			}

			String errorMessage = String.format("Unexpected number of tokens detected (%d) on line: %s", toks.length,
					line.getText().replaceAll("\\t", " [TAB] "));
			logger.error(errorMessage);
			return null;
		} catch (NumberFormatException e) {
			logger.error(String.format("Error while parsing line (%d): %s", line.getLineNumber(), line.getText()), e);
			return null;
		}
	}

	public DataSourceIdentifier<String> getExternalReference() {
		return externalReference;
	}

	public SequencePosition getSequenceStartPosition() {
		return sequenceStartPosition;
	}

	public SequencePosition getSequenceEndPosition() {
		return sequenceEndPosition;
	}

}
