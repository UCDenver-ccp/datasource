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
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.InterProID;

/**
 * Representation of data from InterPro names.dat file
 * 
 * @author Bill Baumgartner
 * 
 */
@Record(dataSource = DataSource.INTERPRO,
		comment="This file maps from InterPro ids to InterPro names. InterPro is a resource that provides functional analysis of protein sequences by classifying them into families and predicting the presence of domains and important sites. See: http://www.ebi.ac.uk/interpro/about.html;jsessionid=1DCC494ADDE7E06E21FB719FC2BFEC0B",
		license=License.EBI,
		citation="Sarah Hunter; Philip Jones; Alex Mitchell; Rolf Apweiler; Teresa K. Attwood; Alex Bateman; Thomas Bernard; David Binns; Peer Bork; Sarah Burge; Edouard de Castro; Penny Coggill; Matthew Corbett; Ujjwal Das; Louise Daugherty; Lauranne Duquenne; Robert D. Finn; Matthew Fraser; Julian Gough; Daniel Haft; Nicolas Hulo; Daniel Kahn; Elizabeth Kelly; Ivica Letunic; David Lonsdale; Rodrigo Lopez; Martin Madera; John Maslen; Craig McAnulla; Jennifer McDowall; Conor McMenamin; Huaiyu Mi; Prudence Mutowo-Muellenet; Nicola Mulder; Darren Natale; Christine Orengo; Sebastien Pesseat; Marco Punta; Antony F. Quinn; Catherine Rivoire; Amaia Sangrador-Vegas; Jeremy D. Selengut; Christian J. A. Sigrist; Maxim Scheremetjew; John Tate; Manjulapramila Thimmajanarthanan; Paul D. Thomas; Cathy H. Wu; Corin Yeats; Siew-Yit Yong (2011). InterPro in 2011: new developments in the family and domain prediction database. Nucleic Acids Research 2011; doi: 10.1093/nar/gkr948",
		label="name2id record")
public class InterProNamesDatFileData extends SingleLineFileRecord {
	private static final Logger logger = Logger.getLogger(InterProNamesDatFileData.class);

	// public static final String INTERPRO_RECORD_ID_PREFIX = "INTERPRO_NAMES_DAT_RECORD_";

	@RecordField(comment="")
	private InterProID interProID;

	@RecordField(comment="")
	private InterProName interProName;

	public InterProNamesDatFileData(InterProID interProID, InterProName interProName, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.interProID = interProID;
		this.interProName = interProName;
	}

	public InterProID getInterProID() {
		return interProID;
	}

	public InterProName getInterProName() {
		return interProName;
	}

	public static InterProNamesDatFileData parseInterProNamesDatLine(Line line) {
		String[] toks = line.getText().split("\\t");
		if (toks.length == 2) {
			InterProID interProID = new InterProID(toks[0]);
			InterProName interProName = new InterProName(toks[1]);
			return new InterProNamesDatFileData(interProID, interProName, line.getByteOffset(), line.getLineNumber());
		}

		logger.error("Unexpected number of tokens (" + toks.length + ") on line: " + line);
		return null;
	}

}
