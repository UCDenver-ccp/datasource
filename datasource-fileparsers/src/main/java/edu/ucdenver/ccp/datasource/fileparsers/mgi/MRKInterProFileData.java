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
package edu.ucdenver.ccp.fileparsers.mgi;

import java.util.HashSet;
import java.util.Set;

import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.InterProID;
import edu.ucdenver.ccp.datasource.identifiers.mgi.MgiGeneID;

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
