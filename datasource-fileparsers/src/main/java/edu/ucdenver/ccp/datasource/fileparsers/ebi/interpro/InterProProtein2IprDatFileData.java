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
package edu.ucdenver.ccp.datasource.fileparsers.ebi.interpro;

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

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.InterProID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;

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
	private final String interProName;

	@RecordField(comment="")
	private final DataSourceIdentifier<String> externalReference;

	@RecordField(comment="")
	private final Integer sequenceStartPosition;

	@RecordField(comment="")
	private final Integer sequenceEndPosition;

	public InterProProtein2IprDatFileData(UniProtID uniprotID, InterProID interProID, String interProName,
			DataSourceIdentifier<String> externalReference, Integer sequenceStartPosition,
			Integer sequenceEndPosition, long byteOffset, long lineNumber) {
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

	public String getInterProName() {
		return interProName;
	}

	public static InterProProtein2IprDatFileData parseInterProProtein2IprDatLine(Line line) {
		Logger logger = Logger.getLogger(InterProProtein2IprDatFileData.class);

		try {
			String[] toks = line.getText().split("\\t");
			if (toks.length > 2) {
				UniProtID uniprotID = new UniProtID(toks[0]);
				InterProID interProID = new InterProID(toks[1]);
				String interProName = new String(toks[2]);
				DataSourceIdentifier<String> externalReference = InterProExternalReferenceFactory
						.parseExternalReference(toks[3]);
				Integer startPosition = new Integer(Integer.parseInt(toks[4]));
				Integer endPosition = new Integer(Integer.parseInt(toks[5]));
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

	public Integer getSequenceStartPosition() {
		return sequenceStartPosition;
	}

	public Integer getSequenceEndPosition() {
		return sequenceEndPosition;
	}

}
