/*
 Copyright (c) 2012, Regents of the University of Colorado
 All rights reserved.

 Redistribution and use in source and binary forms, with or without modification, 
 are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this 
    list of conditions and the following disclaimer.
   
 * Redistributions in binary form must reproduce the above copyright notice, 
    this list of conditions and the following disclaimer in the documentation 
    and/or other materials provided with the distribution.
   
 * Neither the name of the University of Colorado nor the names of its 
    contributors may be used to endorse or promote products derived from this 
    software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
 ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
