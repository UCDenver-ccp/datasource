package edu.ucdenver.ccp.datasource.identifiers.ncbi.refseq;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.ucdenver.ccp.datasource.identifiers.ncbi.refseq.RefSeqID;



public class RefSeqIdTest {

	
	@Test
	public void testVersionRemoval() {
		RefSeqID expectedId = new RefSeqID("NP_047184");
		assertEquals("Version should have been removed automatically", expectedId, new RefSeqID("NP_047184.1"));
	}
	
	
	
	@SuppressWarnings("unused")
	@Test
	public void testValidRefSeqIdValidation() {
		new RefSeqID("AC_123456");
		new RefSeqID("AP_123456");
		new RefSeqID("NC_123456");
		new RefSeqID("NG_123456");
		new RefSeqID("NM_123456");
		new RefSeqID("NP_123456");
		new RefSeqID("NR_123456");
		new RefSeqID("NT_123456");
		new RefSeqID("NW_123456");
		new RefSeqID("NZ_ABCD12345678");
		new RefSeqID("XM_123456");
		new RefSeqID("XP_123456");
		new RefSeqID("XR_123456");
		new RefSeqID("YP_123456");
		new RefSeqID("ZP_12345678");
		new RefSeqID("NS_123456");
		new RefSeqID("XR_016092.");
		new RefSeqID("NZ_CH959310");
	}
	
	
	@SuppressWarnings("unused")
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidRefSeqIdValidation() {
		new RefSeqID("AB_123456");
	}
	
}
