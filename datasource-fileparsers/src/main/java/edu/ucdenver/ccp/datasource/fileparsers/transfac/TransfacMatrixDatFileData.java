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
package edu.ucdenver.ccp.fileparsers.transfac;

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
import edu.ucdenver.ccp.datasource.identifiers.transfac.TransfacFactorID;
import edu.ucdenver.ccp.datasource.identifiers.transfac.TransfacMatrixID;

/**
 * This is an incomplete representation of the data contained in the Transfac gene.dat file record.
 * 
 * @author Bill Baumgartner
 * 
 */
@Record(dataSource = DataSource.TRANSFAC,isComplete = false, label="matrix record (INC)")
public class TransfacMatrixDatFileData extends MultiLineFileRecord {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(TransfacMatrixDatFileData.class);

	public static final String TRANSFAC_MATRIX_RECORD_NAME_PREFIX = "TRANSFAC_MATRIX_RECORD_";

	@RecordField
	private TransfacMatrixID internalMatrixAccessionID;
	@RecordField
	private Set<TransfacFactorID> linkedBindingFactorIDs;

	private final static String ACCESSION_TAG = "AC";

	private final static String LINKED_BINDING_FACTOR_TAG = "BF";

	public TransfacMatrixDatFileData(TransfacMatrixID transfacMatrixID, Set<TransfacFactorID> linkedBindingFactorIDs,
			long byteOffset) {
		super(byteOffset);
		this.internalMatrixAccessionID = transfacMatrixID;
		this.linkedBindingFactorIDs = linkedBindingFactorIDs;
	}

	public static TransfacMatrixDatFileData parseTransfacMatrixDataRecord(MultiLineBuffer transfacDataRecordBuffer) {
		TransfacMatrixDatFileData transfacMatrixFileData = null;
		TransfacMatrixID internalMatrixAccessionID = null;
		Set<TransfacFactorID> linkedBindingFactorIDs = new HashSet<TransfacFactorID>();

		String line;
		BufferedReader br = new BufferedReader(new StringReader(transfacDataRecordBuffer.toString()));
		try {
			while ((line = br.readLine()) != null) {
				if (line.startsWith(ACCESSION_TAG)) {
					internalMatrixAccessionID = new TransfacMatrixID(getLineValue(line));
				} else if (line.startsWith(LINKED_BINDING_FACTOR_TAG)) {
					Pattern p = Pattern.compile("(T\\d+)");
					Matcher m = p.matcher(line);
					while (m.find()) {
						linkedBindingFactorIDs.add(new TransfacFactorID(m.group(1)));
					}
				}
			}

			transfacMatrixFileData = new TransfacMatrixDatFileData(internalMatrixAccessionID, linkedBindingFactorIDs,
					transfacDataRecordBuffer.getByteOffset());
			return transfacMatrixFileData;
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		} finally {
			IOUtils.closeQuietly(br);
		}
	}

	/**
	 * @param line
	 * @return
	 */
	private static String getLineValue(String line) {
		return line.substring(2).trim();
	}

	public TransfacMatrixID getTransfacMatrixID() {
		return internalMatrixAccessionID;
	}

	public Set<TransfacFactorID> getLinkedBindingFactorIDs() {
		return linkedBindingFactorIDs;
	}

}
