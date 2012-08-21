package edu.ucdenver.ccp.datasource.identifiers.network.neo4j.index;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

import edu.ucdenver.ccp.common.string.StringConstants;

/**
 * This Lucene TokenFilter removes all colons and replaces them with underscores. Use of this filter
 * is an attempt to avoid conflicts with Lucene's special treatment of the colon character (it is
 * used to signify field names within a query)
 * 
 * @author bill
 * 
 */
public final class ColonFilter extends TokenFilter {

	private TermAttribute termAtt;

	protected ColonFilter(TokenStream input) {
		super(input);
		termAtt = addAttribute(TermAttribute.class);

	}

	@Override
	public final boolean incrementToken() throws IOException {
		if (input.incrementToken()) {
			final char[] buffer = termAtt.termBuffer();
			final int length = termAtt.termLength();
			for (int i = 0; i < length; i++)
				if (buffer[i] == StringConstants.COLON.charAt(0))
					buffer[i] = StringConstants.UNDERSCORE.charAt(0);
			return true;
		} else
			return false;
	}

}
