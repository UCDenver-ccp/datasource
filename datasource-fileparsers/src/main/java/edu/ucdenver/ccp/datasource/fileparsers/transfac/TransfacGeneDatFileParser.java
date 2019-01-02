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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.datasource.fileparsers.MultiLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.TransfacFactorID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.TransfacGeneID;

/**
 * This class is used to parse the Transfac gene.dat file, available only via registration (and
 * potential payment) with www.gene-regulation.com
 * 
 * @author Bill Baumgartner
 * 
 */
public class TransfacGeneDatFileParser extends MultiLineFileRecordReader<TransfacGeneDatFileData> {

	public static final String GENE_DAT_FILE_NAME = "gene.dat";
	
	private static Logger logger = Logger.getLogger(TransfacGeneDatFileParser.class);

	public TransfacGeneDatFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, null);
	}

	/**
	 * During the initialization we want to skip the header, determine the character offsets for the
	 * column and load the first record.
	 */
	protected void initialize() throws IOException {
		/* Read until line starts with AC */
		line = readLine();
		while (!line.getText().startsWith("AC"))
			line = readLine();
		super.initialize();
	}

	@Override
	protected MultiLineBuffer compileMultiLineBuffer() throws IOException {
		if (line == null)
			return null;
		MultiLineBuffer transfacDataRecordBuffer = new MultiLineBuffer();
		do {
			transfacDataRecordBuffer.add(line);
			line = readLine();
		} while (line != null && !line.getText().startsWith("//"));
		line = readLine();
		return transfacDataRecordBuffer;
	}

	@Override
	protected TransfacGeneDatFileData parseRecordFromMultipleLines(MultiLineBuffer transfacDataRecordBuffer) {
		return TransfacGeneDatFileData.parseTransfacGeneDataRecord(transfacDataRecordBuffer);
	}

	/**
	 * Returns a mapping from Transfac internal gene ID (G00001) to Entrez Gene ID (if there is one
	 * associated in the gene.dat file)
	 * 
	 * @param transfacGeneDatFile
	 * @return
	 */
	public static Map<TransfacGeneID, NcbiGeneId> getTransfacGeneID2EntrezGeneIDMap(File transfacGeneDatFile,
			CharacterEncoding encoding) {
		Map<TransfacGeneID, NcbiGeneId> transfacGeneID2EntrezGeneIDMap = new HashMap<TransfacGeneID, NcbiGeneId>();

		TransfacGeneDatFileParser parser = null;
		try {
			parser = new TransfacGeneDatFileParser(transfacGeneDatFile, encoding);
			while (parser.hasNext()) {
				TransfacGeneDatFileData dataRecord = parser.next();
				TransfacGeneID transfacInternalGeneID = dataRecord.getTransfacGeneID();
				NcbiGeneId entrezGeneID = dataRecord.getEntrezGeneDatabaseReferenceID();
				if (entrezGeneID != null) {
					if (!transfacGeneID2EntrezGeneIDMap.containsKey(transfacInternalGeneID)) {
						transfacGeneID2EntrezGeneIDMap.put(transfacInternalGeneID, entrezGeneID);
					} else {
						logger.warn("Duplicate transfac gene ID encountered: " + transfacInternalGeneID);
					}
				}
			}
			parser.close();
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}

		return transfacGeneID2EntrezGeneIDMap;
	}

	/**
	 * Returns a mapping from Transfac factor ID (T00001) to the encoding gene ID (G00001)
	 * 
	 * @param transfacGeneDatFile
	 * @return
	 */
	public static Map<TransfacFactorID, TransfacGeneID> getTransfacFactorID2EncodingGeneIDMap(File transfacGeneDatFile,
			CharacterEncoding encoding) {
		Map<TransfacFactorID, TransfacGeneID> transfacFactorID2EncodingGeneIDMap = new HashMap<TransfacFactorID, TransfacGeneID>();

		TransfacGeneDatFileParser parser = null;
		try {
			parser = new TransfacGeneDatFileParser(transfacGeneDatFile, encoding);
			while (parser.hasNext()) {
				TransfacGeneDatFileData dataRecord = parser.next();
				TransfacGeneID transfacInternalGeneID = dataRecord.getTransfacGeneID();
				Set<TransfacFactorID> encodedFactorIDs = dataRecord.getEncodedFactorIDs();

				for (TransfacFactorID encodedFactorID : encodedFactorIDs) {
					if (!transfacFactorID2EncodingGeneIDMap.containsKey(encodedFactorID)) {
						transfacFactorID2EncodingGeneIDMap.put(encodedFactorID, transfacInternalGeneID);
					} else {
						logger.warn("Duplicate encoded factor ID encountered: " + encodedFactorID);
					}
				}
			}
			parser.close();
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}

		return transfacFactorID2EncodingGeneIDMap;
	}

	// public static void main(String[] args) {
	// /* how many records have an encoded factor, but no entrez gene ID?? */
	// try {
	// TransfacGeneDatFileParser parser = new TransfacGeneDatFileParser(
	// "/Volumes/hal-1/hanalyzer/research/accelerator/data/expert-data/mouse-data/expert-data/explicit-experts/Transfac/raw/gene.dat");
	//
	// int recordCount = 0;
	// int recordWithEncodedFactor = 0;
	// int recordWithEntrezGeneID = 0;
	// int recordWithBothEncodedFactorAndEntrezGeneID = 0;
	// int recordWithMgiID = 0;
	// int recordWithBothEncodedFactorAndMgiID = 0;
	//
	// while (parser.hasNext()) {
	// TransfacGeneDatFileData dataRecord = parser.next();
	// recordCount++;
	// if (dataRecord.getEncodedFactorIDs() != null && dataRecord.getEncodedFactorIDs().size() > 0)
	// {
	// recordWithEncodedFactor++;
	// }
	// if (dataRecord.getEntrezGeneDatabaseReferenceID() != null) {
	// recordWithEntrezGeneID++;
	// }
	// if ((dataRecord.getEncodedFactorIDs() != null && dataRecord.getEncodedFactorIDs().size() > 0)
	// & (dataRecord.getEntrezGeneDatabaseReferenceID() != null)) {
	// recordWithBothEncodedFactorAndEntrezGeneID++;
	// }
	// if (dataRecord.getMgiDatabaseReferenceID() != null) {
	// recordWithMgiID++;
	// }
	// if ((dataRecord.getEncodedFactorIDs() != null && dataRecord.getEncodedFactorIDs().size() > 0)
	// & (dataRecord.getMgiDatabaseReferenceID() != null)) {
	// recordWithBothEncodedFactorAndMgiID++;
	// }
	// }
	//
	// System.out.println("Record Count: " + recordCount);
	// System.out.println("Records with Encoded Factor(s): " + recordWithEncodedFactor);
	// System.out.println("Records with Entrez Gene ID: " + recordWithEntrezGeneID);
	// System.out.println("Records with BOTH encoded factor(s) and entrez gene id: "
	// + recordWithBothEncodedFactorAndEntrezGeneID);
	// System.out.println("Records with MGI ID: " + recordWithMgiID);
	// System.out
	// .println("Records with BOTH encoded factor(s) and MGI ID: " +
	// recordWithBothEncodedFactorAndMgiID);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }

}
