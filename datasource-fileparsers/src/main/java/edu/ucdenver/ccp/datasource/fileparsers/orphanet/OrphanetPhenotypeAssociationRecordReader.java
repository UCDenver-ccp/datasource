/**

 * 
 */
package edu.ucdenver.ccp.datasource.fileparsers.orphanet;

/*
 * #%L
 * Colorado Computational Pharmacology's datasource
 * 							project
 * %%
 * Copyright (C) 2012 - 2020 Regents of the University of Colorado
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
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.datasource.fileparsers.orphanet.OrphanetPhenotypeAssociationRecord.AssociatedPhenotype;
import edu.ucdenver.ccp.datasource.fileparsers.orphanet.OrphanetPhenotypeAssociationRecord.Frequency;

/**
 * @author Colorado Computational Pharmacology, UC Denver;
 *         ccpsupport@ucdenver.edu
 * 
 */
public class OrphanetPhenotypeAssociationRecordReader implements Iterator<OrphanetPhenotypeAssociationRecord> {

	private static final Logger logger = Logger.getLogger(OrphanetPhenotypeAssociationRecordReader.class);

	private OrphanetPhenotypeAssociationRecord nextRecord = null;

	private static final String DISORDER = "Disorder";
	private static final String ID = "id";

	private static final String ORPHA_CODE = "OrphaCode";

	private static final String DISORDER_TYPE = "DisorderType";

	private static final String HPO_DISORDER_ASSOCIATION = "HPODisorderAssociation";

	private static final String HPO_ID = "HPOId";

	private static final String HPO_TERM = "HPOTerm";

	private static final String HPO_FREQUENCY = "HPOFrequency";

	private static final String NAME = "Name";

	private XMLEventReader eventReader;

	public OrphanetPhenotypeAssociationRecordReader(File xmlFile) throws IOException {
		try {
			XMLInputFactory factory = XMLInputFactory.newInstance();
			eventReader = factory.createXMLEventReader(new FileReader(xmlFile));
			nextRecord = parseNextRecord();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

	private OrphanetPhenotypeAssociationRecord parseNextRecord() throws XMLStreamException {
		OrphanetPhenotypeAssociationRecord record = null;
		AssociatedPhenotype assocPheno = null;
		while (eventReader.hasNext()) {
			XMLEvent xmlEvent = eventReader.nextEvent();

			if (xmlEvent.isStartElement()) {
				StartElement startElement = xmlEvent.asStartElement();
				switch (startElement.getName().getLocalPart()) {
				case DISORDER:
					String disorderId = getAttribute(startElement, ID);
					int characterOffset = startElement.getLocation().getCharacterOffset();
					record = new OrphanetPhenotypeAssociationRecord(disorderId, characterOffset);
					break;
				case ORPHA_CODE:
					Characters orphaCodeDataEvent = (Characters) eventReader.nextEvent();
					record.setOrphaId(orphaCodeDataEvent.getData());
					
					// assumes Name comes before HPODisorderAssociationList
					String disorderName = getName();
					record.setDisorderName(disorderName);
					break;
				case DISORDER_TYPE:
					String disorderType = getName();
					record.setDisorderType(disorderType);
					break;
				case HPO_DISORDER_ASSOCIATION:
					if (assocPheno != null) {
						throw new IllegalStateException(
								"associated phenotype should be null at this point. Make sure previous was closed.");
					}
					assocPheno = new AssociatedPhenotype();
					break;
				case HPO_ID:
					Characters hpoIdDataEvent = (Characters) eventReader.nextEvent();
					assocPheno.setPhenotypeId(hpoIdDataEvent.getData());
					break;
				case HPO_TERM:
					Characters hpoTermDataEvent = (Characters) eventReader.nextEvent();
					assocPheno.setPhenotypeName(hpoTermDataEvent.getData());
					break;
				case HPO_FREQUENCY:
					String frequencyStr = getName();
					switch (frequencyStr) {
					case "Excluded (0%)":
						assocPheno.setFrequency(Frequency.EXCLUDED_0);
						break;
					case "Very rare (<4-1%)":
						assocPheno.setFrequency(Frequency.VERY_RARE_4_1);
						break;
					case "Occasional (29-5%)":
						assocPheno.setFrequency(Frequency.OCCASIONAL_29_5);
						break;
					case "Frequent (79-30%)":
						assocPheno.setFrequency(Frequency.FREQUENT_79_30);
						break;
					case "Very frequent (99-80%)":
						assocPheno.setFrequency(Frequency.VERY_FREQUENT_99_80);
						break;
					case "Obligate (100%)":
						assocPheno.setFrequency(Frequency.OBLIGATE_100);
						break;
					case "Very rare (":
						assocPheno.setFrequency(Frequency.VERY_RARE_4_1);
						break;
						
					default:
						throw new IllegalArgumentException("Unexpected frequency string: " + frequencyStr);
					}
					break;
				default:
					

				}

			} else if (xmlEvent.isEndElement()) {
				EndElement endElement = xmlEvent.asEndElement();
				switch (endElement.getName().getLocalPart()) {
				case DISORDER:
					return record;
				case DISORDER_TYPE:
					break;
				case HPO_DISORDER_ASSOCIATION:
					record.addAssociatedPhenotype(assocPheno);
					assocPheno = null;
					break;
				case HPO_ID:
					break;
				case HPO_TERM:
					break;
				case HPO_FREQUENCY:
					break;
				}
			}

		}
		// no more XML so return null;
		return null;
	}

	/**
	 * cycle to the next <Name> element and return its string
	 * 
	 * @return
	 * @throws XMLStreamException
	 */
	private String getName() throws XMLStreamException {
		while (eventReader.hasNext()) {
			XMLEvent xmlEvent = eventReader.nextEvent();

			if (xmlEvent.isStartElement()) {
				StartElement startElement = xmlEvent.asStartElement();

				if (NAME.equalsIgnoreCase(startElement.getName().getLocalPart())) {
					Characters hpoTermDataEvent = (Characters) eventReader.nextEvent();
					return hpoTermDataEvent.getData();
				}
			}
		}
		throw new IllegalStateException("Did not find a <Name> element to parse!");
	}

	/**
	 * @param startElement
	 * @param attributeName
	 * @return the attribute from a start element
	 */
	private String getAttribute(StartElement startElement, String attributeName) {
		@SuppressWarnings("unchecked")
		Iterator<Attribute> iterator = startElement.getAttributes();

		while (iterator.hasNext()) {
			Attribute attribute = iterator.next();
			QName name = attribute.getName();
			if (attributeName.equalsIgnoreCase(name.getLocalPart())) {
				return attribute.getValue();
			}
		}
		return null;
	}

	@Override
	public boolean hasNext() {
		return nextRecord != null;
	}

	@Override
	public OrphanetPhenotypeAssociationRecord next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		OrphanetPhenotypeAssociationRecord recordToReturn = nextRecord;
		try {
			nextRecord = parseNextRecord();
		} catch (XMLStreamException e) {
			throw new RuntimeException("Unable to parse next record", e);
		}
		return recordToReturn;
	}

	public static void main(String[] args) {
		File xmlFile = new File(
				"/Users/bill/projects/ncats-translator/biolink-text-mining/hpo/orphanet/en_product4.xml");
		try {
			OrphanetPhenotypeAssociationRecordReader rr = new OrphanetPhenotypeAssociationRecordReader(xmlFile);

			while (rr.hasNext()) {
				OrphanetPhenotypeAssociationRecord record = rr.next();
				System.out.println(record);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
