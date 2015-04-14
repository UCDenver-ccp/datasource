/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.snomed;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.BasicConfigurator;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileWriterUtil;
import edu.ucdenver.ccp.common.file.FileWriterUtil.FileSuffixEnforcement;
import edu.ucdenver.ccp.common.file.FileWriterUtil.WriteMode;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineIterator;
import edu.ucdenver.ccp.common.string.RegExPatterns;

/**
 * Removes concepts that have been inactivated
 * 
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class SnomedRf2ConceptFileCleaner {

	public static void cleanConceptFile(File inputFile, File outputFile) throws IOException {
		System.out.println("Cleaning concept file...");
		Map<String, String> idToActiveDate = new HashMap<String, String>();
		Map<String, String> idToInActiveDate = new HashMap<String, String>();

		for (SnomedRf2ConceptFileRecordReader rr = new SnomedRf2ConceptFileRecordReader(inputFile); rr.hasNext();) {
			SnomedRf2ConceptFileRecord record = rr.next();
			String lineId = record.getConceptId();
			boolean isActive = record.isActive();
			String effectiveTime = record.getEffectiveTime();

			updateActiveHashes(idToActiveDate, idToInActiveDate, lineId, isActive, effectiveTime);
		}

		generateCleanFile(inputFile, outputFile, idToActiveDate, idToInActiveDate);

	}

	public static void cleanDescriptionFile(File inputFile, File outputFile) throws IOException {
		System.out.println("Cleaning description file...");
		Map<String, String> idToActiveDate = new HashMap<String, String>();
		Map<String, String> idToInActiveDate = new HashMap<String, String>();

		for (SnomedRf2DescriptionFileRecordReader rr = new SnomedRf2DescriptionFileRecordReader(inputFile); rr
				.hasNext();) {
			SnomedRf2DescriptionFileRecord record = rr.next();
			String lineId = record.getDescriptionId();
			boolean isActive = record.isActive();
			String effectiveTime = record.getEffectiveTime();

			updateActiveHashes(idToActiveDate, idToInActiveDate, lineId, isActive, effectiveTime);
		}

		generateCleanFile(inputFile, outputFile, idToActiveDate, idToInActiveDate);

	}

	public static void cleanRelationshipFile(File inputFile, File outputFile) throws IOException {
		System.out.println("Cleaning relationship file...");
		Map<String, String> idToActiveDate = new HashMap<String, String>();
		Map<String, String> idToInActiveDate = new HashMap<String, String>();

		for (SnomedRf2RelationshipFileRecordReader rr = new SnomedRf2RelationshipFileRecordReader(inputFile); rr
				.hasNext();) {
			SnomedRf2RelationshipFileRecord record = rr.next();
			String lineId = record.getRelationshipId();
			boolean isActive = record.isActive();
			String effectiveTime = record.getEffectiveTime();

			updateActiveHashes(idToActiveDate, idToInActiveDate, lineId, isActive, effectiveTime);
			
		}

//		for (Entry<String, String> entry : idToActiveDate.entrySet()) {
//			System.out.println("ACTIVE: " + entry.getKey() + " -- " + entry.getValue());
//		}
//		
//		for (Entry<String, String> entry : idToInActiveDate.entrySet()) {
//			System.out.println("INACTIVE: " + entry.getKey() + " -- " + entry.getValue());
//		}
		
		generateCleanFile(inputFile, outputFile, idToActiveDate, idToInActiveDate);
	}

	/**
	 * @param idToActiveDate
	 * @param idToInActiveDate
	 * @param lineId
	 * @param isActive
	 * @param lineTime
	 */
	private static void updateActiveHashes(Map<String, String> idToActiveDate, Map<String, String> idToInActiveDate,
			String lineId, boolean isActive, String lineTime) {
		
//		System.out.println("Updating hashes: " + lineId + " isActive: " +isActive + " time: " + lineTime);
		
		if (isActive) {
			if (idToActiveDate.containsKey(lineId)) {
				String activeTime = idToActiveDate.get(lineId);
				if (timeIsEqualToOrGreaterThan(lineTime, activeTime)) {
					idToActiveDate.remove(lineId);
					idToActiveDate.put(lineId, lineTime);
				}
			} else {
				idToActiveDate.put(lineId, lineTime);
			}
		} else {
			if (idToInActiveDate.containsKey(lineId)) {
//				System.out.println("Updating inactive for " + lineId);
				String storedInactiveTime = idToInActiveDate.get(lineId);
				if (timeIsEqualToOrGreaterThan(lineTime, storedInactiveTime)) {
					idToInActiveDate.remove(lineId);
					idToInActiveDate.put(lineId, lineTime);
				}
			} else {
				idToInActiveDate.put(lineId, lineTime);
			}
		}
		
		
		
	}

	/**
	 * @param inputFile
	 * @param outputFile
	 * @param idToActiveDate
	 * @param idToInActiveDate
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void generateCleanFile(File inputFile, File outputFile, Map<String, String> idToActiveDate,
			Map<String, String> idToInActiveDate) throws FileNotFoundException, IOException {
		BufferedWriter writer = FileWriterUtil.initBufferedWriter(outputFile, CharacterEncoding.UTF_8,
				WriteMode.OVERWRITE, FileSuffixEnforcement.OFF);

		try {
			for (StreamLineIterator iter = new StreamLineIterator(inputFile, CharacterEncoding.UTF_8); iter.hasNext();) {
				Line line = iter.next();
				String lineText = line.getText();
				if (line.getLineNumber() == 0) {
					writer.write(lineText);
					writer.newLine();
					continue;
				}
				String lineId = line.getText().split(RegExPatterns.TAB)[0];
				String lineTime = line.getText().split(RegExPatterns.TAB)[1];

				String activeTime = idToActiveDate.get(lineId);
				String inactiveTime = idToInActiveDate.get(lineId);

				if (activeTime == null) {
					System.out.println("Null active time: " + lineId);
					continue;
				}

//				System.out.println("lineId: " + lineId + " lineTime: " + lineTime + " activeTime: " + activeTime
//						+ " inactiveTime: " + inactiveTime);

				boolean printLine = false;
				if (inactiveTime == null && timeIsEqualToOrGreaterThan(lineTime, activeTime)) {
					printLine = true;
				} else if (inactiveTime != null && timeIsEqualToOrGreaterThan(activeTime, inactiveTime)
						&& timeIsEqualToOrGreaterThan(lineTime, activeTime)) {
					printLine = true;
				}

				if (printLine) {
					writer.write(lineText);
					writer.newLine();
				}
			}
		} finally {
			writer.close();
		}
	}

	public static boolean timeIsEqualToOrGreaterThan(String time1, String time2) {
//		System.out.println("Testing t1: " + time1 + " >= t2:" + time2);
		Integer t1 = Integer.parseInt(time1);
		Integer t2 = Integer.parseInt(time2);

		return t1 >= t2;
	}

	public static void main(String[] args) {
		BasicConfigurator.configure();
		File snomedDirectory = new File(
				"/Users/bill/Documents/snomed/SnomedCT_Release_INT_20130731/RF2Release/Full/Terminology");
		File snomedConceptFile = new File(snomedDirectory, "sct2_Concept_Full_INT_20130731.txt");
		File snomedDescriptionFile = new File(snomedDirectory, "sct2_Description_Full-en_INT_20130731.txt");
		File snomedRelationshipFile = new File(snomedDirectory, "sct2_Relationship_Full_INT_20130731.txt");
		File cleanedSnomedConceptFile = new File(snomedConceptFile.getAbsolutePath() + ".activeOnly");
		File cleanedSnomedDescriptionFile = new File(snomedDescriptionFile.getAbsolutePath() + ".activeOnly");
		File cleanedSnomedRelationshipFile = new File(snomedRelationshipFile.getAbsolutePath() + ".activeOnly");

		try {
			cleanConceptFile(snomedConceptFile, cleanedSnomedConceptFile);
			cleanDescriptionFile(snomedDescriptionFile, cleanedSnomedDescriptionFile);
			cleanRelationshipFile(snomedRelationshipFile, cleanedSnomedRelationshipFile);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
