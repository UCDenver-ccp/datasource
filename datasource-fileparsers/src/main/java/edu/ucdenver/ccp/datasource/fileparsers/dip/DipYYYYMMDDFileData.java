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
package edu.ucdenver.ccp.datasource.fileparsers.dip;

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

import java.util.Set;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.DipInteractionID;
import lombok.Getter;

/**
 * Data structure for storing data extracted from the DipYYYMMDD file. 
 *
 * @see http://code.google.com/p/psimi/wiki/PsimiTabFormat
 * 
 * @author Bill Baumgartner
 * 
 */
@Record(dataSource = DataSource.DIP, 
		comment = "see http://code.google.com/p/psimi/wiki/PsimiTabFormat", 
		citation="pmid:14681454,pmid:11752321", 
		license=License.DIP, label = "DIP record" )
@Getter
public class DipYYYYMMDDFileData extends SingleLineFileRecord {
	private static Logger logger = Logger.getLogger(DipYYYYMMDDFileData.class);

	@RecordField(comment = "Unique identifier for interactor A, represented as databaseName:ac, where databaseName is the name of the corresponding database as defined in the PSI-MI controlled vocabulary, and ac is the unique primary identifier of the molecule in the database. Identifiers from multiple databases can be separated by \"|\". It is recommended that proteins be identified by stable identifiers such as their UniProtKB or RefSeq accession number", label = "interactor A")
	private final DipInteractor interactor_A;

	@RecordField(comment="see interactor_A", label = "interactor B")
	private final DipInteractor interactor_B;

	@RecordField(label = "experiments")
	private Set<DipInteractionExperiment> interactionExperiments;

	@RecordField(label = "source db")
	private final DipInteractionSourceDatabase sourceDatabase;

	@RecordField(label = "interaction id")
	private final DipInteractionID interactionID;

	@RecordField(comment = "Confidence score. Denoted as scoreType:value. There are many different types of confidence score, but so far no controlled vocabulary. Thus the only current recommendation is to use score types consistently within one source. Multiple scores separated by \"|\".  ", label = "quality score")
	private final String qualityStatus;

	/**
	 * @param byteOffset
	 * @param lineNumber
	 * @param interactor_A
	 * @param interactor_B
	 * @param interactionExperiments
	 * @param sourceDatabase
	 * @param interactionID
	 * @param qualityStatus
	 */
	public DipYYYYMMDDFileData(DipInteractor interactor_A, DipInteractor interactor_B,
			Set<DipInteractionExperiment> interactionExperiments, DipInteractionSourceDatabase sourceDatabase,
			DipInteractionID interactionID, String qualityStatus, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.interactor_A = interactor_A;
		this.interactor_B = interactor_B;
		this.interactionExperiments = interactionExperiments;
		this.sourceDatabase = sourceDatabase;
		this.interactionID = interactionID;
		this.qualityStatus = qualityStatus;
	}

}
