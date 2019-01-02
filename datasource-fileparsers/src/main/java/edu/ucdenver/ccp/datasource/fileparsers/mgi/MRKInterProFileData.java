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

import java.util.HashSet;
import java.util.Set;

import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.InterProID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MgiGeneID;

/**
 * This class is deprecated. The MRK_InterPro.rpt file is no long available on the MGI FTP site
 * Data representation of contents of MRK_InterPro.rpt file
 * 
 * @author Bill Baumgartner
 * 
 */
@Deprecated
public class MRKInterProFileData extends SingleLineFileRecord {
	public static final String RECORD_NAME_PREFIX = "MRK_INTERPRO_FILE_RECORD_";
	private final MgiGeneID mgiAccessionID;

	private final String markerSymbol;

	private final Set<InterProID> interProAccessionIDs;

	public MRKInterProFileData(MgiGeneID mgiAccessionID, String markerSymbol,
			Set<InterProID> interProAccessionIDs, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.mgiAccessionID = mgiAccessionID;
		this.markerSymbol = markerSymbol;
		this.interProAccessionIDs = interProAccessionIDs;
	}

	

	public MgiGeneID getMgiAccessionID() {
		return mgiAccessionID;
	}

	public String getMarkerSymbol() {
		return markerSymbol;
	}

	public Set<InterProID> getInterProAccessionIDs() {
		return interProAccessionIDs;
	}

	public static MRKInterProFileData parseMRKInterProLine(Line line) {
		String[] toks = line.getText().split("\\t");

		MgiGeneID mgiAccessionID = new MgiGeneID(toks[0]);
		String markerSymbol = new String(toks[1]);
		Set<InterProID> interProAccessionIDs = new HashSet<InterProID>();
		for (String interProIDStr : toks[2].split(" "))
			interProAccessionIDs.add(new InterProID(interProIDStr));

		return new MRKInterProFileData(mgiAccessionID, markerSymbol, interProAccessionIDs, line.getByteOffset(), line.getLineNumber());

	}

}
