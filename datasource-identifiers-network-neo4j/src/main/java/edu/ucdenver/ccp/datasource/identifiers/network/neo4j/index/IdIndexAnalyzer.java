package edu.ucdenver.ccp.datasource.identifiers.network.neo4j.index;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.WhitespaceTokenizer;

/**
 * The IdIndexAnalyzer splits on whitespace and replaces all colons with underscores to avoid
 * conflicting with Lucene's special treatment of colons in query strings (colons signify field
 * names in queries).
 * 
 * @author bill
 * 
 */
public final class IdIndexAnalyzer extends Analyzer {

	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		TokenStream stream = new WhitespaceTokenizer(reader);
		return new ColonFilter(stream);
	}

	private class SavedStreams {
		Tokenizer source;
		TokenStream result;
	}

	@Override
	public TokenStream reusableTokenStream(String fieldName, Reader reader) throws IOException {
		SavedStreams streams = (SavedStreams) getPreviousTokenStream();
		if (streams == null) {
			streams = new SavedStreams();
			streams.source = new WhitespaceTokenizer(reader);
			streams.result = new ColonFilter(streams.source);
			setPreviousTokenStream(streams);
		} else
			streams.source.reset(reader);
		return streams.result;
	}

}
