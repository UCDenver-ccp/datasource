package edu.ucdenver.ccp.datasource.fileparsers.mgi;

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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.common.string.RegExPatterns;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.download.FtpHost;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.NucleotideAccessionResolver;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.VegaID;

/**
 * This class is used to parse MGI MRK_Sequence.rpt files
 * 
 * @author Bill Baumgartner
 * 
 */
public class MRKSequenceFileParser extends SingleLineFileRecordReader<MRKSequenceFileData> {
	/*
	 * There is a line break in the header. The final column header (Feature
	 * Type) is on the next line by itself.
	 */
	private static final String HEADER = "MGI Marker Accession ID\tMarker Symbol\tStatus\tMarker Type\tMarker Name\tcM position\tChromosome\tGenome Coordinate Start\tGenome Coordinate End\tStrand\tGenBank IDs\tRefSeq transcript IDs\tVEGA transcript IDs\tEnsembl transcript IDs\tUniProt IDs\tTrEMBL IDs\tVEGA protein IDs\tEnsembl protein IDs\tRefSeq protein IDs\tUniGene IDs";

	private static final Logger logger = Logger.getLogger(MRKSequenceFileParser.class);

	public static final String FTP_FILE_NAME = "MRK_Sequence.rpt";
	public static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;

	@FtpDownload(server = FtpHost.MGI_HOST, path = FtpHost.MGI_REPORTS_PATH, filename = FTP_FILE_NAME, filetype = FileType.ASCII)
	private File mrkSequenceRptFile;

	public MRKSequenceFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, null);
	}

	public MRKSequenceFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(mrkSequenceRptFile, encoding, skipLinePrefix);
	}

	@Override
	protected String getFileHeader() throws IOException {
		String header = readLine().getText();
		/*
		 * There is a line break in the header. The final column header (Feature
		 * Type) is on the next line by itself so we burn a line here.
		 */
		readLine();
		return header;
	}

	@Override
	protected String getExpectedFileHeader() throws IOException {
		return HEADER;
	}

	@Override
	protected MRKSequenceFileData parseRecordFromLine(Line line) {
		String[] toks = line.getText().split("\\t", -1);

		MgiGeneID mgiAccessionID = new MgiGeneID(toks[0]);
		String markerSymbol = new String(toks[1]);
		String status = toks[2];
		MgiGeneType markerType = MgiGeneType.getValue(toks[3]);
		String markerName = new String(toks[4]);
		String cM_Position = toks[5];
		String chromosome = null;
		if (!toks[6].equals("UN")) {
			chromosome = toks[6];
		}
		Integer genomeCoordinateStart = null;
		if (!toks[7].isEmpty()) {
			genomeCoordinateStart = Integer.parseInt(toks[7]);
		}
		Integer genomeCoordinateEnd = null;
		if (!toks[8].isEmpty()) {
			genomeCoordinateEnd = Integer.parseInt(toks[8]);
		}
		String strand = null;
		if (!toks[9].isEmpty()) {
			strand = toks[9];
		}
		Set<DataSourceIdentifier<?>> genBankAccessionIDs = new HashSet<DataSourceIdentifier<?>>();
		if (!toks[10].isEmpty()) {
			String[] genBankIDs = toks[10].split(RegExPatterns.PIPE);
			for (String genBankID : genBankIDs) {
				if (genBankID.trim().length() > 0) {
					DataSourceIdentifier<String> resolveNucleotideAccession = NucleotideAccessionResolver
							.resolveNucleotideAccession(genBankID, genBankID);
					genBankAccessionIDs.add(resolveNucleotideAccession);
				}
			}
		}
		Set<RefSeqID> refseqTranscriptIds = new HashSet<RefSeqID>();
		if (!toks[11].isEmpty()) {
			String[] ids = toks[11].split(RegExPatterns.PIPE);
			for (String id : ids) {
				if (id.trim().length() > 0) {
					refseqTranscriptIds.add(new RefSeqID(id));
				}
			}
		}
		Set<VegaID> vegaTranscriptIds = new HashSet<VegaID>();
		if (!toks[12].isEmpty()) {
			String[] ids = toks[12].split(RegExPatterns.PIPE);
			for (String id : ids) {
				if (id.trim().length() > 0) {
					vegaTranscriptIds.add(new VegaID(id));
				}
			}
		}
		Set<EnsemblGeneID> ensemblTranscriptIds = new HashSet<EnsemblGeneID>();
		if (!toks[13].isEmpty()) {
			String[] ids = toks[13].split(RegExPatterns.PIPE);
			for (String id : ids) {
				if (id.trim().length() > 0) {
					ensemblTranscriptIds.add(new EnsemblGeneID(id));
				}
			}
		}
		Set<UniProtID> uniprotIds = new HashSet<UniProtID>();
		if (!toks[14].isEmpty()) {
			String[] ids = toks[14].split(RegExPatterns.PIPE);
			for (String id : ids) {
				if (id.trim().length() > 0) {
					uniprotIds.add(new UniProtID(id));
				}
			}
		}
		Set<UniProtID> tremblIds = new HashSet<UniProtID>();
		if (!toks[15].isEmpty()) {
			String[] ids = toks[15].split(RegExPatterns.PIPE);
			for (String id : ids) {
				if (id.trim().length() > 0) {
					tremblIds.add(new UniProtID(id));
				}
			}
		}
		Set<VegaID> vegaProteinIds = new HashSet<VegaID>();
		if (!toks[16].isEmpty()) {
			String[] ids = toks[16].split(RegExPatterns.PIPE);
			for (String id : ids) {
				if (id.trim().length() > 0) {
					vegaProteinIds.add(new VegaID(id));
				}
			}
		}
		Set<EnsemblGeneID> ensemblProteinIds = new HashSet<EnsemblGeneID>();
		if (!toks[17].isEmpty()) {
			String[] ids = toks[17].split(RegExPatterns.PIPE);
			for (String id : ids) {
				if (id.trim().length() > 0) {
					ensemblProteinIds.add(new EnsemblGeneID(id));
				}
			}
		}
		Set<RefSeqID> refseqProteinIds = new HashSet<RefSeqID>();
		if (!toks[18].isEmpty()) {
			String[] ids = toks[18].split(RegExPatterns.PIPE);
			for (String id : ids) {
				if (id.trim().length() > 0) {
					try {
						RefSeqID refSeqID = new RefSeqID(id);
						refseqProteinIds.add(refSeqID);
					} catch (IllegalArgumentException e) {
						logger.warn("Detected illegal RefSeq identifier: " + id + " on line: " + line.getLineNumber());
					}
				}
			}
		}
		Set<UniGeneID> unigeneIds = new HashSet<UniGeneID>();
		if (!toks[19].isEmpty()) {
			String[] ids = toks[19].split(RegExPatterns.PIPE);
			for (String id : ids) {
				if (id.trim().length() > 0) {
					unigeneIds.add(new UniGeneID(id));
				}
			}
		}

		String featureType = toks[20];

		return new MRKSequenceFileData(mgiAccessionID, markerSymbol, status, markerType, markerName, cM_Position,
				chromosome, genomeCoordinateStart, genomeCoordinateEnd, strand, genBankAccessionIDs,
				refseqTranscriptIds, vegaTranscriptIds, ensemblTranscriptIds, uniprotIds, tremblIds, vegaProteinIds,
				ensemblProteinIds, refseqProteinIds, unigeneIds, featureType, line.getByteOffset(),
				line.getLineNumber());

	}

	/**
	 * Returns a map from refseq ID to MGI IDs
	 * 
	 * @param mrkSequenceRptFile
	 * @return
	 */
	public static Map<RefSeqID, Set<MgiGeneID>> getRefSeqID2MgiIDsMap(File mrkSequenceRptFile,
			CharacterEncoding encoding) throws IOException {
		Map<RefSeqID, Set<MgiGeneID>> refseqID2MgiIDMap = new HashMap<RefSeqID, Set<MgiGeneID>>();

		MRKSequenceFileParser parser = null;
		try {
			parser = new MRKSequenceFileParser(mrkSequenceRptFile, encoding);
			while (parser.hasNext()) {
				MRKSequenceFileData dataRecord = parser.next();
				MgiGeneID mgiID = dataRecord.getMgiAccessionID();
				Set<RefSeqID> refSeqIDs = dataRecord.getRefseqProteinIds();
				refSeqIDs.addAll(dataRecord.getRefseqTranscriptIds());

				for (RefSeqID refseqID : refSeqIDs) {
					if (refseqID2MgiIDMap.containsKey(refseqID)) {
						refseqID2MgiIDMap.get(refseqID).add(mgiID);
					} else {
						Set<MgiGeneID> mgiIDs = new HashSet<MgiGeneID>();
						mgiIDs.add(mgiID);
						refseqID2MgiIDMap.put(refseqID, mgiIDs);
					}
				}
			}
			return refseqID2MgiIDMap;
		} finally {
			if (parser != null) {
				parser.close();
			}
		}
	}

}
