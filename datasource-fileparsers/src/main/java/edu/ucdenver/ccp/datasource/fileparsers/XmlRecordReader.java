/**
 * Copyright (C) 2011 Center for Computational Pharmacology, University of Colorado School of Medicine
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
package edu.ucdenver.ccp.datasource.fileparsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;

/**
 * XML Record reader.
 * 
 * @author malenkiy
 * 
 */
public abstract class XmlRecordReader extends RecordReader {

	/** xml file */
	private final File xmlFile;
	/** reader */
	private XMLEventReader xmlReader;

	/**
	 * Default constructor
	 * 
	 * @param xmlFile xml file
	 */
	public XmlRecordReader(File xmlFile) {
		this.xmlFile = xmlFile;
		try {
			initialize();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/** 
	 * Initialize xml reader 
	 * 
	 * @see edu.ucdenver.ccp.fileparsers.RecordReader#initialize()
	 */
	@Override
	protected void initialize() throws IOException {
		super.initialize();
		try {
			xmlReader = XMLInputFactory.newInstance().createXMLEventReader(new FileInputStream(xmlFile));
		} catch (XMLStreamException e) {
			throw new IOException(e);
		} catch (FactoryConfigurationError e) {
			throw new IllegalStateException(e);
		}
	}

	/** 
	 * Close xml reader
	 * 
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {
		try {
			xmlReader.close();
		} catch (XMLStreamException e) {
			throw new IOException(e);
		}
	}
	
	/**
	 * Get next xml event
	 * 
	 * @return event
	 */
	protected XMLEvent nextEvent() {
		if (peek() == null)
			return null;
		
		try {
			return xmlReader.nextEvent();
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Get next xml event
	 * 
	 * @return event
	 */
	protected XMLEvent nextTag() {
		if (peek() == null)
			return null;
		
		try {
			return xmlReader.nextTag();
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
	}	
	
	protected XMLEventReader getXmlReader() {
		return xmlReader;
	}
	
	protected XMLEvent peek() {
		try {
			return getXmlReader().peek();
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
	}

	/** Get current element's text */
	protected String getElementText() {
		XMLEvent nextEvent = nextEvent();
		if (nextEvent.isCharacters())
			return nextEvent.asCharacters().getData();
		
		return null;
	}
	
	/** Get next element's text */
	protected String getNextElementText() {
		XMLEvent e = null;
		while ((e = nextEvent()) != null) {
			if (e.isStartElement())
				return getElementText();
		}
		
		return null;
	}
	
	protected String getElementName(XMLEvent e) {
		String name = null;
		
		if (e.isStartElement()) {
			name = e.asStartElement().getName().getLocalPart();
		} if (e.isEndElement()) {
			name = e.asEndElement().getName().getLocalPart();
		}
		
		return name;
	}
	
	protected String getAttributeValue(XMLEvent e, String attName) {
		String result = null;
		Attribute att = e.asStartElement().getAttributeByName(new QName(attName));
		if (att != null)
			result = att.getValue();
		
		return result;
	}	
}
