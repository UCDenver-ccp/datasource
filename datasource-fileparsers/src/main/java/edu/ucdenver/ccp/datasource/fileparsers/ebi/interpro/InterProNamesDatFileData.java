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
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.InterProID;

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
	private String interProName;

	public InterProNamesDatFileData(InterProID interProID, String interProName, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.interProID = interProID;
		this.interProName = interProName;
	}

	public InterProID getInterProID() {
		return interProID;
	}

	public String getInterProName() {
		return interProName;
	}

	public static InterProNamesDatFileData parseInterProNamesDatLine(Line line) {
		String[] toks = line.getText().split("\\t");
		if (toks.length == 2) {
			InterProID interProID = new InterProID(toks[0]);
			String interProName = new String(toks[1]);
			return new InterProNamesDatFileData(interProID, interProName, line.getByteOffset(), line.getLineNumber());
		}

		logger.error("Unexpected number of tokens (" + toks.length + ") on line: " + line);
		return null;
	}

}
