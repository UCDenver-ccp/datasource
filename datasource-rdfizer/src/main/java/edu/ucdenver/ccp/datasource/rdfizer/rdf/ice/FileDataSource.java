package edu.ucdenver.ccp.datasource.rdfizer.rdf.ice;

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
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.download.DownloadMetadata;
import edu.ucdenver.ccp.common.download.DownloadUtil;
import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.download.HttpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileArchiveUtil;
import edu.ucdenver.ccp.common.file.FileReaderUtil;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.common.http.HttpUtil;
import edu.ucdenver.ccp.datasource.fileparsers.FileRecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.drugbank.DrugbankXmlFileRecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.goa.gaf.GoaGaf2FileRecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.goa.gaf.GoaGafFileRecordReaderFactory;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.goa.gaf.GoaGafFileRecordReaderFactory.AnnotationType;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.goa.gaf.GoaGafFileRecordReaderFactory.FileInfo;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.interpro.InterPro2GoFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.interpro.InterProNamesDatFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.interpro.InterProProtein2IprDatFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.uniprot.SparseTremblDatFileRecordReader_HumanOnly;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.uniprot.SparseTremblXmlFileRecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.uniprot.SwissProtXmlFileRecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.uniprot.UniProtIDMappingFileRecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.gad.GeneticAssociationDbAllTxtFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.hgnc.HgncDownloadFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.hgnc.HgncDownloadFileParser.WithdrawnRecordTreatment;
import edu.ucdenver.ccp.datasource.fileparsers.hp.HpAnnotationFileRecordReader_AllSourcesAllFrequencies;
import edu.ucdenver.ccp.datasource.fileparsers.hprd.HprdIdMappingsTxtFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.irefweb.IRefWebPsiMitab2_6FileParser_AllSpecies;
import edu.ucdenver.ccp.datasource.fileparsers.irefweb.IRefWebPsiMitab2_6FileParser_HumanOnly;
import edu.ucdenver.ccp.datasource.fileparsers.kegg.KeggGenesFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.kegg.KeggGenesFileParserFactory;
import edu.ucdenver.ccp.datasource.fileparsers.kegg.KeggMapTitleTabFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.mgi.MGIEntrezGeneFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.mgi.MGIPhenoGenoMPFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.mgi.MRKListFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.mgi.MRKReferenceFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.mgi.MRKSequenceFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.mgi.MRKSwissProtFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.mirbase.MirBaseMiRnaDatFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.ncbi.gene.NcbiGene2RefseqFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.ncbi.gene.NcbiGeneInfoFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.ncbi.gene.NcbiGeneMim2GeneFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.ncbi.gene.NcbiGeneRefSeqUniprotKbCollabFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.ncbi.homologene.HomoloGeneDataFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.ncbi.omim.OmimTxtFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.ncbi.refseq.RefSeqReleaseCatalogFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.pharmgkb.PharmGkbDiseaseFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.pharmgkb.PharmGkbDrugFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.pharmgkb.PharmGkbGeneFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.pharmgkb.PharmGkbRelationFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.premod.HumanPReModModuleTabFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.premod.MousePReModModuleTabFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.pro.ProMappingFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.reactome.ReactomeUniprot2PathwayStidTxtFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.rgd.RgdRatGeneFileRecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.rgd.RgdRatGeneMpAnnotationFileRecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.rgd.RgdRatGeneNboAnnotationFileRecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.rgd.RgdRatGenePwAnnotationFileRecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.rgd.RgdRatGeneRdoAnnotationFileRecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.transfac.TransfacGeneDatFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.transfac.TransfacMatrixDatFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.vectorbase.VectorBaseFastaFileRecordReader_aael_transcripts;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.ice.FileDataSourceParams.IsTaxonAware;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.ice.FileDataSourceParams.RequiresManualDownload;

/**
 * This enum separates RDF generation by data source file. It is intended to
 * provide an easy way to generate RDF for a particular resource. Its methods
 * are aimed at providing a simple manner to generate RDF concurrently in
 * different processes.
 * 
 * @author Center for Computational Pharmacology, UC Denver;
 *         ccpsupport@ucdenver.edu
 * 
 */

public enum FileDataSource {

	/*
	 * DIP is now part of IRefWeb, so it has been commented out since it
	 * requires the extra manual step of logging in to the DIP website and
	 * downloading the file (and IRefWeb does not).
	 */
	// /**
	// * The DIP data file must be obtained manually. It is assumed to already
	// be
	// * in place when RDF generation commences. It must be the only file in the
	// * DIP data source directory.
	// *
	// */
	// DIP(DataSource.DIP) {
	//
	// @Override
	// protected FileRecordReader<?> initFileRecordReader(File
	// sourceFileDirectory, boolean cleanSourceFiles,
	// File idListDir, Set<NcbiTaxonomyID> taxonIds) throws IOException {
	// logger.info("sourceFileDirectory (exists): (" +
	// sourceFileDirectory.exists() + ")" + sourceFileDirectory);
	// logger.info("file listing: " +
	// Arrays.toString(sourceFileDirectory.listFiles()));
	// File dipDataFile = sourceFileDirectory.listFiles()[0];
	// logger.info("File exists: " + dipDataFile.exists() + " -- " +
	// dipDataFile.getAbsolutePath());
	// FileUtil.validateFile(dipDataFile);
	// return new DipYYYYMMDDFileParser(dipDataFile, CharacterEncoding.US_ASCII,
	// taxonIds);
	// }
	//
	// @Override
	// protected boolean isTaxonAware() {
	// return true;
	// }
	// },

	/**
	 * this data source represents genes as defined by the KEGG resource
	 */
	KEGG_GENE(DataSource.KEGG, IsTaxonAware.YES, RequiresManualDownload.YES) {
		@Override
		protected RecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			File genesFileDirectory = new File(sourceFileDirectory, "gene-files");
			FileUtil.validateDirectory(genesFileDirectory);
			return KeggGenesFileParserFactory.getAggregateRecordReader(taxonIds, genesFileDirectory);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return KeggGenesFileParser.class;
		}
	},

	KEGG_PATHWAY_NAMES(DataSource.KEGG, IsTaxonAware.NO, RequiresManualDownload.YES) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			File keggMapTitleTabFile = new File(sourceFileDirectory, "map_title.tab");
			FileUtil.validateFile(keggMapTitleTabFile);
			return new KeggMapTitleTabFileParser(keggMapTitleTabFile, CharacterEncoding.US_ASCII);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return KeggMapTitleTabFileParser.class;
		}
	},
	/**
	 * 
	 */
	PHARMGKB_DISEASE(DataSource.PHARMGKB, IsTaxonAware.NO, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new PharmGkbDiseaseFileParser(sourceFileDirectory, cleanSourceFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return PharmGkbDiseaseFileParser.class;
		}
	},

	PHARMGKB_GENE(DataSource.PHARMGKB, IsTaxonAware.NO, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new PharmGkbGeneFileParser(sourceFileDirectory, cleanSourceFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return PharmGkbGeneFileParser.class;
		}
	},

	PHARMGKB_DRUG(DataSource.PHARMGKB, IsTaxonAware.NO, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new PharmGkbDrugFileParser(sourceFileDirectory, cleanSourceFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return PharmGkbDrugFileParser.class;
		}
	},
	/**
	 * 
	 *
	 */
	DRUGBANK(DataSource.DRUGBANK, IsTaxonAware.NO, RequiresManualDownload.YES) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {

			File drugbankXmlFile = new File(sourceFileDirectory, "full database.xml");

			if (drugbankXmlFile.exists()) {
				FileUtil.validateFile(drugbankXmlFile);
				return new DrugbankXmlFileRecordReader(drugbankXmlFile);
			}

			logger.warn("\n\n!!!!!!!!!!!!!!!!!!!!!!   SKIPPING DRUGBANK   !!!!!!!!!!!!!!!!!!!!!!"
					+ "The DrugBank 'full database.xml' file could not be located in the expected folder: "
					+ sourceFileDirectory.getAbsolutePath()
					+ "\nIf you would like DrugBank to be processed, please place a copy of the DrugBank 'full database.xml' "
					+ "file in that folder and restart this process."
					+ "\n\n!!!!!!!!!!!!!!!!!!!!!!   SKIPPING DRUGBANK   !!!!!!!!!!!!!!!!!!!!!!");
			return null;

		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return DrugbankXmlFileRecordReader.class;
		}
	},

	/**
	 * 
	 */
	HGNC(DataSource.HGNC, IsTaxonAware.NO, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new HgncDownloadFileParser(sourceFileDirectory, cleanSourceFiles, WithdrawnRecordTreatment.IGNORE);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return HgncDownloadFileParser.class;
		}
	},
	/**
	 * 
	 */
	HOMOLOGENE(DataSource.HOMOLOGENE, IsTaxonAware.YES, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new HomoloGeneDataFileParser(sourceFileDirectory, cleanSourceFiles, taxonIds);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return HomoloGeneDataFileParser.class;
		}
	},

	HP_ANNOTATIONS_ALL_SOURCES(DataSource.HP, IsTaxonAware.NO, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new HpAnnotationFileRecordReader_AllSourcesAllFrequencies(sourceFileDirectory, cleanSourceFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return HpAnnotationFileRecordReader_AllSourcesAllFrequencies.class;
		}
	},
	/**
	 * 
	 */
	IREFWEB(DataSource.IREFWEB, IsTaxonAware.YES, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new IRefWebPsiMitab2_6FileParser_AllSpecies(sourceFileDirectory, cleanSourceFiles, taxonIds);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return IRefWebPsiMitab2_6FileParser_AllSpecies.class;
		}
	},

	/**
	 * 
	 */
	IREFWEB_HUMAN_ONLY(DataSource.IREFWEB, IsTaxonAware.YES, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new IRefWebPsiMitab2_6FileParser_HumanOnly(sourceFileDirectory, cleanSourceFiles, taxonIds);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return IRefWebPsiMitab2_6FileParser_HumanOnly.class;
		}
	},

	/**
	 * 
	 * 
	 */
	MGI_ENTREZGENE(DataSource.MGI, IsTaxonAware.NO, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new MGIEntrezGeneFileParser(sourceFileDirectory, cleanSourceFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return MGIEntrezGeneFileParser.class;
		}
	},
	MGI_MGIPHENOGENO(DataSource.MGI, IsTaxonAware.NO, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new MGIPhenoGenoMPFileParser(sourceFileDirectory, cleanSourceFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return MGIPhenoGenoMPFileParser.class;
		}
	},
	MGI_MRKLIST(DataSource.MGI, IsTaxonAware.NO, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new MRKListFileParser(sourceFileDirectory, cleanSourceFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return MRKListFileParser.class;
		}
	},
	MGI_MRKREFERENCE(DataSource.MGI, IsTaxonAware.NO, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new MRKReferenceFileParser(sourceFileDirectory, cleanSourceFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return MRKReferenceFileParser.class;
		}
	},
	MGI_MRKSEQUENCE(DataSource.MGI, IsTaxonAware.NO, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new MRKSequenceFileParser(sourceFileDirectory, cleanSourceFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return MRKSequenceFileParser.class;
		}
	},
	MGI_MRKSWISSPROT(DataSource.MGI, IsTaxonAware.NO, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new MRKSwissProtFileParser(sourceFileDirectory, cleanSourceFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return MRKSwissProtFileParser.class;
		}
	},
	MIRBASE(DataSource.MIRBASE, IsTaxonAware.NO, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new MirBaseMiRnaDatFileParser(sourceFileDirectory, cleanSourceFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return MirBaseMiRnaDatFileParser.class;
		}
	},

	/**
	 *
	 */
	RGD_GENES(DataSource.RGD, IsTaxonAware.NO, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new RgdRatGeneFileRecordReader(sourceFileDirectory, cleanSourceFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return RgdRatGeneFileRecordReader.class;
		}
	},

	/**
	 *
	 */
	RGD_GENE_MP(DataSource.RGD, IsTaxonAware.NO, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new RgdRatGeneMpAnnotationFileRecordReader(sourceFileDirectory, cleanSourceFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return RgdRatGeneMpAnnotationFileRecordReader.class;
		}
	},

	/**
	 *
	 */
	RGD_GENE_RDO(DataSource.RGD, IsTaxonAware.NO, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new RgdRatGeneRdoAnnotationFileRecordReader(sourceFileDirectory, cleanSourceFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return RgdRatGeneRdoAnnotationFileRecordReader.class;
		}
	},

	/**
	 *
	 */
	RGD_GENE_NBO(DataSource.RGD, IsTaxonAware.NO, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new RgdRatGeneNboAnnotationFileRecordReader(sourceFileDirectory, cleanSourceFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return RgdRatGeneNboAnnotationFileRecordReader.class;
		}
	},

	/**
	 *
	 */
	RGD_GENE_PW(DataSource.RGD, IsTaxonAware.NO, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new RgdRatGenePwAnnotationFileRecordReader(sourceFileDirectory, cleanSourceFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return RgdRatGenePwAnnotationFileRecordReader.class;
		}
	},

	/**
	 *
	 */
	PREMOD_HUMAN(DataSource.PREMOD, IsTaxonAware.NO, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new HumanPReModModuleTabFileParser(sourceFileDirectory, cleanSourceFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return HumanPReModModuleTabFileParser.class;
		}
	},
	PREMOD_MOUSE(DataSource.PREMOD, IsTaxonAware.NO, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new MousePReModModuleTabFileParser(sourceFileDirectory, cleanSourceFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return MousePReModModuleTabFileParser.class;
		}
	},

	/**
	 *
	 */
	PR_MAPPINGFILE(DataSource.PR, IsTaxonAware.NO, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new ProMappingFileParser(sourceFileDirectory, cleanSourceFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return ProMappingFileParser.class;
		}
	},
	/**
	 *
	 */
	REACTOME_UNIPROT2PATHWAYSTID(DataSource.REACTOME, IsTaxonAware.YES_BUT_REQUIRES_EXTERNAL_ID_TO_TAXON_MAPPINGS, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new ReactomeUniprot2PathwayStidTxtFileParser(sourceFileDirectory, cleanSourceFiles, idListDir,
					taxonIds, sourceFileDirectory, cleanIdListFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return ReactomeUniprot2PathwayStidTxtFileParser.class;
		}
	},
	/**
	 *
	 */
	REFSEQ_RELEASECATALOG(DataSource.REFSEQ, 3, IsTaxonAware.YES, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new RefSeqReleaseCatalogFileParser(sourceFileDirectory, cleanSourceFiles, taxonIds);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return RefSeqReleaseCatalogFileParser.class;
		}
	},
	/**
	 *
	 */
	NCBIGENE_GENE2REFSEQ(DataSource.NCBI_GENE, IsTaxonAware.YES, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new NcbiGene2RefseqFileParser(sourceFileDirectory, cleanSourceFiles, taxonIds);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return NcbiGene2RefseqFileParser.class;
		}
	},
	NCBIGENE_GENEINFO(DataSource.NCBI_GENE, IsTaxonAware.YES, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new NcbiGeneInfoFileParser(sourceFileDirectory, cleanSourceFiles, taxonIds);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return NcbiGeneInfoFileParser.class;
		}
	},
	NCBIGENE_MIM2GENE(DataSource.NCBI_GENE, IsTaxonAware.NO, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new NcbiGeneMim2GeneFileParser(sourceFileDirectory, cleanSourceFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return NcbiGeneMim2GeneFileParser.class;
		}
	},
	NCBIGENE_REFSEQUNIPROTCOLLAB(DataSource.NCBI_GENE, IsTaxonAware.YES_BUT_REQUIRES_EXTERNAL_ID_TO_TAXON_MAPPINGS,
			RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new NcbiGeneRefSeqUniprotKbCollabFileParser(sourceFileDirectory, cleanSourceFiles, idListDir,
					taxonIds, baseSourceFileDirectory, cleanIdListFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return NcbiGeneRefSeqUniprotKbCollabFileParser.class;
		}
	},

	GOA(DataSource.GOA, IsTaxonAware.YES, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return GoaGafFileRecordReaderFactory.getRecordReader(sourceFileDirectory, taxonIds,
					AnnotationType.CANONICAL, cleanSourceFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return GoaGaf2FileRecordReader.class;
		}

		@Override
		public Set<DownloadMetadata> getDownloadMetadata(Class<? extends RecordReader<?>> rrClass,
				File sourceFileDirectory, Set<NcbiTaxonomyID> taxonIds) throws IOException, ParseException {
			AnnotationType annotType = AnnotationType.CANONICAL;
			FileInfo downloadFileInfo = GoaGafFileRecordReaderFactory.getDownloadFileInfo(taxonIds, annotType);
			File downloadedFile = new File(sourceFileDirectory, downloadFileInfo.getFilenameToDownload(annotType));
			File readySemaphoreFile = DownloadUtil.getReadySemaphoreFile(downloadedFile);
			Set<DownloadMetadata> metadata = new HashSet<DownloadMetadata>();
			if (readySemaphoreFile.exists()) {
				metadata.add(DownloadMetadata.loadFromPropertiesFile(readySemaphoreFile));
			}
			return metadata;
		}
	},
	GOA_HUMAN(DataSource.GOA, IsTaxonAware.YES, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return GoaGafFileRecordReaderFactory.getRecordReader(sourceFileDirectory, NcbiTaxonomyID.HUMAN,
					AnnotationType.CANONICAL, cleanSourceFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return GoaGaf2FileRecordReader.class;
		}

		@Override
		public Set<DownloadMetadata> getDownloadMetadata(Class<? extends RecordReader<?>> rrClass,
				File sourceFileDirectory, Set<NcbiTaxonomyID> taxonIds) throws IOException, ParseException {
			AnnotationType annotType = AnnotationType.CANONICAL;
			FileInfo downloadFileInfo = GoaGafFileRecordReaderFactory.getDownloadFileInfo(taxonIds, annotType);
			File downloadedFile = new File(sourceFileDirectory, downloadFileInfo.getFilenameToDownload(annotType));
			File readySemaphoreFile = DownloadUtil.getReadySemaphoreFile(downloadedFile);
			Set<DownloadMetadata> metadata = new HashSet<DownloadMetadata>();
			if (readySemaphoreFile.exists()) {
				metadata.add(DownloadMetadata.loadFromPropertiesFile(readySemaphoreFile));
			}
			return metadata;
		}
	},
	GOA_HUMAN_ISOFORM(DataSource.GOA, IsTaxonAware.YES, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return GoaGafFileRecordReaderFactory.getRecordReader(sourceFileDirectory, NcbiTaxonomyID.HUMAN,
					AnnotationType.ISOFORM, cleanSourceFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return GoaGaf2FileRecordReader.class;
		}

		@Override
		public Set<DownloadMetadata> getDownloadMetadata(Class<? extends RecordReader<?>> rrClass,
				File sourceFileDirectory, Set<NcbiTaxonomyID> taxonIds) throws IOException, ParseException {
			AnnotationType annotType = AnnotationType.ISOFORM;
			FileInfo downloadFileInfo = GoaGafFileRecordReaderFactory.getDownloadFileInfo(taxonIds, annotType);
			File downloadedFile = new File(sourceFileDirectory, downloadFileInfo.getFilenameToDownload(annotType));
			File readySemaphoreFile = DownloadUtil.getReadySemaphoreFile(downloadedFile);
			Set<DownloadMetadata> metadata = new HashSet<DownloadMetadata>();
			if (readySemaphoreFile.exists()) {
				metadata.add(DownloadMetadata.loadFromPropertiesFile(readySemaphoreFile));
			}
			return metadata;
		}
	},
	GOA_HUMAN_COMPLEX(DataSource.GOA, IsTaxonAware.YES, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return GoaGafFileRecordReaderFactory.getRecordReader(sourceFileDirectory, NcbiTaxonomyID.HUMAN,
					AnnotationType.COMPLEX, cleanSourceFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return GoaGaf2FileRecordReader.class;
		}

		@Override
		public Set<DownloadMetadata> getDownloadMetadata(Class<? extends RecordReader<?>> rrClass,
				File sourceFileDirectory, Set<NcbiTaxonomyID> taxonIds) throws IOException, ParseException {
			AnnotationType annotType = AnnotationType.COMPLEX;
			FileInfo downloadFileInfo = GoaGafFileRecordReaderFactory.getDownloadFileInfo(taxonIds, annotType);
			File downloadedFile = new File(sourceFileDirectory, downloadFileInfo.getFilenameToDownload(annotType));
			File readySemaphoreFile = DownloadUtil.getReadySemaphoreFile(downloadedFile);
			Set<DownloadMetadata> metadata = new HashSet<DownloadMetadata>();
			if (readySemaphoreFile.exists()) {
				metadata.add(DownloadMetadata.loadFromPropertiesFile(readySemaphoreFile));
			}
			return metadata;
		}
	},
	GOA_HUMAN_RNA(DataSource.GOA, IsTaxonAware.YES, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return GoaGafFileRecordReaderFactory.getRecordReader(sourceFileDirectory, NcbiTaxonomyID.HUMAN,
					AnnotationType.RNA, cleanSourceFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return GoaGaf2FileRecordReader.class;
		}

		@Override
		public Set<DownloadMetadata> getDownloadMetadata(Class<? extends RecordReader<?>> rrClass,
				File sourceFileDirectory, Set<NcbiTaxonomyID> taxonIds) throws IOException, ParseException {
			AnnotationType annotType = AnnotationType.RNA;
			FileInfo downloadFileInfo = GoaGafFileRecordReaderFactory.getDownloadFileInfo(taxonIds, annotType);
			File downloadedFile = new File(sourceFileDirectory, downloadFileInfo.getFilenameToDownload(annotType));
			File readySemaphoreFile = DownloadUtil.getReadySemaphoreFile(downloadedFile);
			Set<DownloadMetadata> metadata = new HashSet<DownloadMetadata>();
			if (readySemaphoreFile.exists()) {
				metadata.add(DownloadMetadata.loadFromPropertiesFile(readySemaphoreFile));
			}
			return metadata;
		}
	},
	/**
	 */
	UNIPROT_SWISSPROT(DataSource.UNIPROT, IsTaxonAware.YES, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new SwissProtXmlFileRecordReader(sourceFileDirectory, cleanSourceFiles, taxonIds);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return SwissProtXmlFileRecordReader.class;
		}
	},
	UNIPROT_IDMAPPING(DataSource.UNIPROT, 3, IsTaxonAware.YES, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new UniProtIDMappingFileRecordReader(sourceFileDirectory, cleanSourceFiles, taxonIds);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return UniProtIDMappingFileRecordReader.class;
		}
	},
	// UNIPROT_TREMBL(DataSource.UNIPROT, 33, 1000000) {
	// @Override
	// protected FileRecordReader<?> initFileRecordReader(File
	// sourceFileDirectory, boolean
	// cleanSourceFiles,
	// File idListDir, Set<NcbiTaxonomyID> taxonIds) throws IOException {
	// return new TremblXmlFileRecordReader(sourceFileDirectory,
	// cleanSourceFiles, taxonIds);
	// }
	// },
	UNIPROT_TREMBL_SPARSE(DataSource.UNIPROT, 33, 1000000, IsTaxonAware.YES, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new SparseTremblXmlFileRecordReader(sourceFileDirectory, cleanSourceFiles, taxonIds);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return SparseTremblXmlFileRecordReader.class;
		}
	},
	UNIPROT_TREMBL_SPARSE_HUMAN_ONLY(DataSource.UNIPROT, 33, 1000000, IsTaxonAware.YES, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new SparseTremblDatFileRecordReader_HumanOnly(sourceFileDirectory, CharacterEncoding.UTF_8,
					cleanSourceFiles, taxonIds);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return SparseTremblDatFileRecordReader_HumanOnly.class;
		}
	},

	VECTORBASE_AAEL_TRANSCRIPTS(DataSource.VECTORBASE, IsTaxonAware.NO, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new VectorBaseFastaFileRecordReader_aael_transcripts(sourceFileDirectory, cleanSourceFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return VectorBaseFastaFileRecordReader_aael_transcripts.class;
		}
	},

	/**
	 * 
	 */
	INTERPRO_NAMESDAT(DataSource.INTERPRO, IsTaxonAware.NO, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new InterProNamesDatFileParser(sourceFileDirectory, cleanSourceFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return InterProNamesDatFileParser.class;
		}
	},
	INTERPRO_INTERPRO2GO(DataSource.INTERPRO, IsTaxonAware.NO, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new InterPro2GoFileParser(sourceFileDirectory, cleanSourceFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return InterPro2GoFileParser.class;
		}
	},
	INTERPRO_PROTEIN2IPR(DataSource.INTERPRO, 13, IsTaxonAware.YES_BUT_REQUIRES_EXTERNAL_ID_TO_TAXON_MAPPINGS,
			RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new InterProProtein2IprDatFileParser(sourceFileDirectory, cleanSourceFiles, idListDir, taxonIds,
					baseSourceFileDirectory, cleanIdListFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return InterProProtein2IprDatFileParser.class;
		}
	},

	/**
	 * The HPRD HPRD_ID_MAPPINGS.txt file must be obtained manually. It is
	 * assumed to already be in place when RDF generation commences.
	 */
	HPRD_ID_MAPPINGS(DataSource.HPRD, IsTaxonAware.NO, RequiresManualDownload.YES) {

		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			File hprdIdMappingFile = new File(sourceFileDirectory,
					HprdIdMappingsTxtFileParser.HPRD_ID_MAPPINGS_TXT_FILE_NAME);
			FileUtil.validateFile(hprdIdMappingFile);
			return new HprdIdMappingsTxtFileParser(hprdIdMappingFile, CharacterEncoding.US_ASCII);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return HprdIdMappingsTxtFileParser.class;
		}

		@Override
		public Set<DownloadMetadata> getDownloadMetadata(Class<? extends RecordReader<?>> rrClass,
				File sourceFileDirectory, Set<NcbiTaxonomyID> taxonIds) throws IOException, ParseException {
			File hprdIdMappingFile = new File(sourceFileDirectory,
					HprdIdMappingsTxtFileParser.HPRD_ID_MAPPINGS_TXT_FILE_NAME);
			File readySemaphoreFile = DownloadUtil.getReadySemaphoreFile(hprdIdMappingFile);
			Set<DownloadMetadata> metadata = new HashSet<DownloadMetadata>();
			if (readySemaphoreFile.exists()) {
				metadata.add(DownloadMetadata.loadFromPropertiesFile(readySemaphoreFile));
			}
			return metadata;
		}
	},
	/**
	 * The TRANSFAC gene.dat and matrix.dat files must be obtained manually.
	 * They are assumed to already be in place when RDF generation commences.
	 */
	TRANSFAC_GENE(DataSource.TRANSFAC, IsTaxonAware.NO, RequiresManualDownload.YES) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			File transfacGeneDatFile = new File(sourceFileDirectory, TransfacGeneDatFileParser.GENE_DAT_FILE_NAME);
			FileUtil.validateFile(transfacGeneDatFile);
			return new TransfacGeneDatFileParser(transfacGeneDatFile, CharacterEncoding.ISO_8859_1);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return TransfacGeneDatFileParser.class;
		}

		@Override
		public Set<DownloadMetadata> getDownloadMetadata(Class<? extends RecordReader<?>> rrClass,
				File sourceFileDirectory, Set<NcbiTaxonomyID> taxonIds) throws IOException, ParseException {
			File transfacGeneDatFile = new File(sourceFileDirectory, TransfacGeneDatFileParser.GENE_DAT_FILE_NAME);
			File readySemaphoreFile = DownloadUtil.getReadySemaphoreFile(transfacGeneDatFile);
			Set<DownloadMetadata> metadata = new HashSet<DownloadMetadata>();
			if (readySemaphoreFile.exists()) {
				metadata.add(DownloadMetadata.loadFromPropertiesFile(readySemaphoreFile));
			}
			return metadata;
		}
	},

	TRANSFAC_MATRIX(DataSource.TRANSFAC, IsTaxonAware.NO, RequiresManualDownload.YES) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			File transfacMatrixDatFile = new File(sourceFileDirectory,
					TransfacMatrixDatFileParser.MATRIX_DAT_FILE_NAME);
			FileUtil.validateFile(transfacMatrixDatFile);
			return new TransfacMatrixDatFileParser(transfacMatrixDatFile, CharacterEncoding.ISO_8859_1);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return TransfacMatrixDatFileParser.class;
		}

		@Override
		public Set<DownloadMetadata> getDownloadMetadata(Class<? extends RecordReader<?>> rrClass,
				File sourceFileDirectory, Set<NcbiTaxonomyID> taxonIds) throws IOException, ParseException {
			File transfacMatrixDatFile = new File(sourceFileDirectory,
					TransfacMatrixDatFileParser.MATRIX_DAT_FILE_NAME);
			File readySemaphoreFile = DownloadUtil.getReadySemaphoreFile(transfacMatrixDatFile);
			Set<DownloadMetadata> metadata = new HashSet<DownloadMetadata>();
			if (readySemaphoreFile.exists()) {
				metadata.add(DownloadMetadata.loadFromPropertiesFile(readySemaphoreFile));
			}
			return metadata;
		}
	},
	/**
	 * The GAD all.txt data file must be obtained manually. It is assumed to
	 * already be in place when RDF generation commences.
	 */
	GAD(DataSource.GAD, IsTaxonAware.NO, RequiresManualDownload.YES) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			File gadAllTxtFile = new File(sourceFileDirectory,
					GeneticAssociationDbAllTxtFileParser.GAD_ALL_TXT_FILE_NAME);
			FileUtil.validateFile(gadAllTxtFile);
			return new GeneticAssociationDbAllTxtFileParser(gadAllTxtFile, CharacterEncoding.US_ASCII);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return GeneticAssociationDbAllTxtFileParser.class;
		}

		@Override
		public Set<DownloadMetadata> getDownloadMetadata(Class<? extends RecordReader<?>> rrClass,
				File sourceFileDirectory, Set<NcbiTaxonomyID> taxonIds) throws IOException, ParseException {
			File gadAllTxtFile = new File(sourceFileDirectory,
					GeneticAssociationDbAllTxtFileParser.GAD_ALL_TXT_FILE_NAME);
			File readySemaphoreFile = DownloadUtil.getReadySemaphoreFile(gadAllTxtFile);
			Set<DownloadMetadata> metadata = new HashSet<DownloadMetadata>();
			if (readySemaphoreFile.exists()) {
				metadata.add(DownloadMetadata.loadFromPropertiesFile(readySemaphoreFile));
			}
			return metadata;
		}
	},
	/**
	 *
	 */
	NCBI_OMIM(DataSource.OMIM, IsTaxonAware.NO, RequiresManualDownload.NO) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			return new OmimTxtFileParser(sourceFileDirectory, cleanSourceFiles);
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return OmimTxtFileParser.class;
		}
	},
	PHARMGKB_RELATION(DataSource.PHARMGKB, IsTaxonAware.NO, RequiresManualDownload.YES) {
		@Override
		protected FileRecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
				boolean cleanSourceFiles, boolean cleanIdListFiles, File idListDir, Set<NcbiTaxonomyID> taxonIds)
				throws IOException {
			File pharmgkbRelationshipsDataFile = new File(sourceFileDirectory, "relationships.tsv");

			if (pharmgkbRelationshipsDataFile.exists()) {
				return new PharmGkbRelationFileParser(pharmgkbRelationshipsDataFile, CharacterEncoding.UTF_8);
			}

			logger.warn("\n\n!!!!!!!!!!!!!!!!!!!!!!   SKIPPING PHARMGKB RELATIONS   !!!!!!!!!!!!!!!!!!!!!!"
					+ "The PharmGKB 'relationships.tsv' file could not be located in the expected folder: "
					+ sourceFileDirectory.getAbsolutePath()
					+ "\nIf you would like PharmGKB relationships to be processed, please place a copy of the 'relationships.tsv' "
					+ "file in that folder and restart this process."
					+ "\n\n!!!!!!!!!!!!!!!!!!!!!!   SKIPPING PHARMGKB RELATIONS   !!!!!!!!!!!!!!!!!!!!!!");
			return null;
		}

		@Override
		protected Class<? extends RecordReader<?>> getRecordReaderClass() {
			return PharmGkbRelationFileParser.class;
		}

		@Override
		public Set<DownloadMetadata> getDownloadMetadata(Class<? extends RecordReader<?>> rrClass,
				File sourceFileDirectory, Set<NcbiTaxonomyID> taxonIds) throws IOException, ParseException {
			File pharmgkbRelationshipsDataFile = new File(sourceFileDirectory, "relationships.tsv");
			File readySemaphoreFile = DownloadUtil.getReadySemaphoreFile(pharmgkbRelationshipsDataFile);
			Set<DownloadMetadata> metadata = new HashSet<DownloadMetadata>();
			if (readySemaphoreFile.exists()) {
				metadata.add(DownloadMetadata.loadFromPropertiesFile(readySemaphoreFile));
			}
			return metadata;
		}
	};

	public enum Split {
		BY_STAGES, NONE;
	}

	private static final Logger logger = Logger.getLogger(FileDataSource.class);

	/**
	 * For single source files that are processed by multiple stages to produce
	 * multiple RDF output files, the files are processed in blocks of records
	 * as indicated by this constant, i.e. 10 million records from the source
	 * file will be put into each RDF output file.
	 */
	private static final long BLOCK_RECORD_COUNT = 10000000;
	/**
	 * Constant indicating all stages of RDF generation should be run serially
	 */
	private static final int DO_ALL_STAGES = -1;
	/**
	 * Suffix used for the static rdf files stored on the classpath
	 */
	private static final String STATIC_RDF_FILE_SUFFIX = ".nt";
	/**
	 * 
	 */
	private final DataSource dataSource;

	/**
	 * Indicates the number of individual RDF generation steps for a given
	 * resource
	 */
	private final int numberOfStages;

	private final Long blockRecordCount;

	private final IsTaxonAware isTaxonAware;

	private final RequiresManualDownload requiresManualDownload;

	private FileDataSource(DataSource dataSource, int numberOfStages, long blockRecordCount, IsTaxonAware isTaxonAware,
			RequiresManualDownload requiresManualDownload) {
		this.dataSource = dataSource;
		this.numberOfStages = numberOfStages;
		this.blockRecordCount = blockRecordCount;
		this.isTaxonAware = isTaxonAware;
		this.requiresManualDownload = requiresManualDownload;
	}

	private FileDataSource(DataSource dataSource, int numberOfStages, IsTaxonAware isTaxonAware,
			RequiresManualDownload requiresManualDownload) {
		this.dataSource = dataSource;
		this.numberOfStages = numberOfStages;
		this.blockRecordCount = null;
		this.isTaxonAware = isTaxonAware;
		this.requiresManualDownload = requiresManualDownload;
	}

	private FileDataSource(DataSource dataSource, IsTaxonAware isTaxonAware,
			RequiresManualDownload requiresManualDownload) {
		this.dataSource = dataSource;
		this.numberOfStages = 1;
		this.blockRecordCount = null;
		this.isTaxonAware = isTaxonAware;
		this.requiresManualDownload = requiresManualDownload;
	}

	public DataSource dataSource() {
		return dataSource;
	}

	public Long blockRecordCount() {
		return blockRecordCount;
	}

	public boolean isTaxonAware() {
		return isTaxonAware == IsTaxonAware.YES_BUT_REQUIRES_EXTERNAL_ID_TO_TAXON_MAPPINGS
				|| isTaxonAware == IsTaxonAware.YES;
	}

	public boolean requiresExternalIdToTaxonMappings() {
		return isTaxonAware == IsTaxonAware.YES_BUT_REQUIRES_EXTERNAL_ID_TO_TAXON_MAPPINGS;
	}

	public boolean requiresManualDownload() {
		return requiresManualDownload == RequiresManualDownload.YES;
	}

	// /**
	// * @param stageNum
	// * @param baseSourceFileDirectory
	// * @param baseRdfOutputDirectory
	// * @param cleanSourceFiles
	// * @param compress
	// * @param outputRecordLimit
	// * @return a collection of file references to all RDF file that were
	// created
	// * @throws IOException
	// */
	// public Collection<File> runStage(long currentTime, int stageNum, File
	// baseSourceFileDirectory,
	// File baseRdfOutputDirectory, boolean cleanSourceFiles, boolean compress,
	// long
	// outputRecordLimit)
	// throws IOException {
	// logger.info("Running " + this.name() + " Stage " + stageNum);
	// if (stageNum == 0) {
	// initialize(currentTime, baseSourceFileDirectory, baseRdfOutputDirectory);
	// return Collections.emptyList();
	// }
	// File sourceFileDirectory =
	// getSourceFileDirectory(baseSourceFileDirectory);
	// File rdfOutputDirectory = getOutputDirectory(baseRdfOutputDirectory);
	// return generateRdf(sourceFileDirectory, rdfOutputDirectory, currentTime,
	// cleanSourceFiles,
	// compress,
	// outputRecordLimit, stageNum);
	// }
	//
	// /**
	// * Creates all RDF for a given data source by running each stage
	// consecutively.
	// *
	// * @param baseSourceFileDirectory
	// * @param baseRdfOutputDirectory
	// * @param cleanSourceFiles
	// * @param compress
	// * @param outputRecordLimit
	// * @return a collection of file references to all RDF file that were
	// created
	// * @throws IOException
	// */
	// public Collection<File> generateRdf(long currentTime, File
	// baseSourceFileDirectory, File
	// baseRdfOutputDirectory,
	// boolean cleanSourceFiles, boolean compress, long outputRecordLimit)
	// throws IOException {
	// File sourceFileDirectory =
	// getSourceFileDirectory(baseSourceFileDirectory);
	// File rdfOutputDirectory = getOutputDirectory(baseRdfOutputDirectory);
	// initialize(currentTime, baseSourceFileDirectory, baseRdfOutputDirectory);
	// return generateRdf(sourceFileDirectory, rdfOutputDirectory, currentTime,
	// cleanSourceFiles,
	// compress,
	// outputRecordLimit, DO_ALL_STAGES);
	// }

	protected abstract RecordReader<?> initFileRecordReader(File sourceFileDirectory, File baseSourceFileDirectory,
			boolean cleanSourceFiles, boolean cleanIdListFiles, File idListFileDirectory, Set<NcbiTaxonomyID> taxonIds)
			throws IOException;

	protected abstract Class<? extends RecordReader<?>> getRecordReaderClass();

	public Set<DownloadMetadata> getDownloadMetadata(Class<? extends RecordReader<?>> rrClass, File sourceFileDirectory,
			Set<NcbiTaxonomyID> taxonIds) throws IOException, ParseException {
		Set<DownloadMetadata> metadata = new HashSet<DownloadMetadata>();
		for (Field field : rrClass.getDeclaredFields()) {
			if (field.isAnnotationPresent(FtpDownload.class)) {
				FtpDownload annotation = field.getAnnotation(FtpDownload.class);
				File downloadedFile = getDownloadedFilename(sourceFileDirectory, annotation);
				File readySemaphoreFile = DownloadUtil.getReadySemaphoreFile(downloadedFile);
				if (readySemaphoreFile.exists()) {
					logger.info("Reading download metadata from property file: " + readySemaphoreFile);
					logger.info("Metadata contents: " + CollectionsUtil.createDelimitedString(
							FileReaderUtil.loadLinesFromFile(readySemaphoreFile, CharacterEncoding.UTF_8), "\n"));
					metadata.add(DownloadMetadata.loadFromPropertiesFile(readySemaphoreFile));
				}
			} else if (field.isAnnotationPresent(HttpDownload.class)) {
				HttpDownload annotation = field.getAnnotation(HttpDownload.class);
				File downloadedFile = getDownloadedFilename(sourceFileDirectory, annotation);
				File readySemaphoreFile = DownloadUtil.getReadySemaphoreFile(downloadedFile);
				if (readySemaphoreFile.exists()) {
					metadata.add(DownloadMetadata.loadFromPropertiesFile(readySemaphoreFile));
				}
			}
		}
		return metadata;
	}

	/**
	 * @param sourceFileDirectory
	 * @param annotation
	 * @return a reference to the downloaded file from a HttpDownload annotation
	 * @throws IOException
	 */
	private File getDownloadedFilename(File sourceFileDirectory, HttpDownload annotation) throws IOException {
		if (!annotation.targetFileName().isEmpty()) {
			return new File(sourceFileDirectory, annotation.targetFileName());
		}
		String filename = null;
		if (!annotation.fileName().isEmpty()) {
			filename = annotation.fileName();
		} else {
			/* otherwise attempt to infer the file name from the download URL */
			URL url;
			try {
				url = new URL(annotation.url());
			} catch (MalformedURLException e) {
				throw new IOException(e);
			}
			filename = HttpUtil.getFinalPathElement(url);
		}
		if (annotation.decompress()) {
			filename = FileArchiveUtil.getUnzippedFileName(annotation.fileName());
		}
		return new File(sourceFileDirectory, filename);

	}

	/**
	 * @param sourceFileDirectory
	 * @param annotation
	 * @return a reference to the downloaded file from a FtpDownload annotation
	 */
	private File getDownloadedFilename(File sourceFileDirectory, FtpDownload annotation) {
		if (!annotation.targetFileName().isEmpty()) {
			return new File(sourceFileDirectory, annotation.targetFileName());
		}
		String filename = annotation.filename();
		if (annotation.decompress()) {
			filename = FileArchiveUtil.getUnzippedFileName(annotation.filename());
		}
		return new File(sourceFileDirectory, filename);
	}

	// /**
	// * To be implemented by each DataSourceRdfGenerator instance.
	// *
	// * @param sourceFileDirectory
	// * @param rdfOutputDirectory
	// * @param createdTime
	// * @param cleanSourceFiles
	// * @param compress
	// * @param outputRecordLimit
	// * @param stageNum
	// * @return a collection of file references to all RDF file that were
	// created
	// * @throws IOException
	// */
	// protected abstract Collection<File> generateRdf(File sourceFileDirectory,
	// File
	// rdfOutputDirectory,
	// long createdTime, boolean cleanSourceFiles, boolean compress, long
	// outputRecordLimit) throws
	// IOException;

	// /**
	// * Creates the source file and RDF output directories if necessary
	// *
	// * and copies the data source specific static RDF file from the classpath
	// (the files in
	// * src/main/resources/edu/ucdenver/ccp/rdfizer/rdf/ice) to the RDF output
	// directory
	// *
	// * @param baseSourceFileDirectory
	// * the base directory where source files can be found or are to be stored
	// after
	// * downloading. A datasource-specific subdirectory will be created in this
	// base
	// * directory for each datasource that is processed.
	// * @param baseRdfOutputDirectory
	// * the base directory where RDF output is to be stored. A
	// datasource-specific
	// * subdirectory will be created in this base directory for each datasource
	// that is
	// * processed.
	// * @param cleanSourceFiles
	// * if true the original source files are deleted and then re-downloaded
	// prior to RDF
	// * generation. This parameter is not used in the default case as it's
	// typically
	// * handled in the generateRdf() method, however the value is made
	// available in
	// * initialize() for special cases (See HOMOLOGENE).
	// */
	// public void initialize(long currentTime, File baseSourceFileDirectory,
	// File
	// baseRdfOutputDirectory) {
	// logger.info("Initializing RDF Generator: " + this.name());
	// File outputDirectory = getOutputDirectory(baseRdfOutputDirectory);
	// FileUtil.mkdir(outputDirectory);
	// FileUtil.mkdir(getSourceFileDirectory(baseSourceFileDirectory));
	// // try {
	// // File staticRdfFile =
	// ClassPathUtil.copyClasspathResourceToDirectory(getClass(),
	// // getDataSourceStaticRdfFileName(), outputDirectory);
	// // return outputMetaRdf(RdfNamespace.getNamespace(dataSource),
	// staticRdfFile,
	// // outputDirectory,
	// // createBaseRdfSource(currentTime, baseRdfOutputDirectory));
	// // } catch (IOException e) {
	// // throw new RuntimeException(e);
	// // }
	//
	// }

	// /**
	// * @return the static RDF file name for a specific data source which is
	// the
	// * {@link RdfDataSource#name()} lower-cased with a .nt suffix. This file
	// must
	// * exist on the classpath
	// (src/main/resources/edu/ucdenver/ccp/rdfizer/rdf/ice)
	// */
	// private String getDataSourceStaticRdfFileName() {
	// return this.name().toLowerCase() + STATIC_RDF_FILE_SUFFIX;
	// }

	/**
	 * @param baseRdfOutputDirectory
	 *            the base directory where RDF output is to be stored. A
	 *            datasource-specific subdirectory will be created in this base
	 *            directory for each datasource that is processed.
	 * @return a datasource-specific directory indicating where the RDF files
	 *         should be created
	 */
	private File getOutputDirectory(File baseRdfOutputDirectory) {
		return new File(baseRdfOutputDirectory, this.name().toLowerCase());
	}

	/**
	 * @param baseSourceFileDirectory
	 *            the base directory where source files can be found or are to
	 *            be stored after downloading. A datasource-specific
	 *            subdirectory will be created in this base directory for each
	 *            datasource that is processed.
	 * @return a datasource-specific directory indicating where the source data
	 *         files either are available, or where they should be stored when
	 *         downloaded
	 */
	private File getSourceFileDirectory(File baseSourceFileDirectory) {
		return new File(baseSourceFileDirectory, this.dataSource.name().toLowerCase());
	}

	/**
	 * @return the numberOfStages
	 */
	public int getNumberOfStages() {
		return numberOfStages;
	}

	/**
	 * Counts the number
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// int stageCount = 0;
		// System.out.println("BY STAGES: ");
		// for (FileDataSource source : FileDataSource.values()) {
		// for (int i = 0; i < source.getNumberOfStages(); i++) {
		// System.out.println("Global Stage: " + (i + 1 + stageCount) + " ==> "
		// + source.name() + " Local Stage: "
		// + (i + 1));
		// }
		// stageCount += source.getNumberOfStages();
		// }
		// System.out.println("Total # of stages: " + stageCount + "\n\n");
		// stageCount = 0;
		// System.out.println("SINGLE STAGE PER SOURCE:");
		// for (FileDataSource source : FileDataSource.values()) {
		// System.out.println("SGE index: " + (stageCount + 1) + " ==> " +
		// source.name());
		// stageCount++;
		// }

		List<String> autoDownloadSources = new ArrayList<String>();
		List<String> manualDownloadSources = new ArrayList<String>();

		for (FileDataSource fds : values()) {
			if (fds.requiresManualDownload()) {
				manualDownloadSources.add(fds.name());
			} else {
				autoDownloadSources.add(fds.name());
			}
		}

		Collections.sort(autoDownloadSources);
		Collections.sort(manualDownloadSources);

		for (String name : autoDownloadSources) {
			System.out.println("DS: " + name);
		}
		System.out.println("DS: ==== BELOW REQUIRE MANUAL DOWNLOAD OF DATA SOURCE FILE ====");
		for (String name : manualDownloadSources) {
			System.out.println("DS: ==== " + name);
		}
	}

}
