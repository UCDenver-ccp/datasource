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
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.TransfacFactorID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.TransfacMatrixID;

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
