package com.example.ToDo.Helper;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.util.Set;

public class MyAnalyzer extends Analyzer {

    private final CharArraySet stopWords;
    private final Set<String> protectedTerms;

    public MyAnalyzer(CharArraySet stopWords, Set<String> protectedTerms) {
        this.stopWords = stopWords;
        this.protectedTerms = protectedTerms;
    }


    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        WhitespaceTokenizer tokenizer = new WhitespaceTokenizer(); // break into individual tokens
        TokenStream tokenStream = new StopFilter(tokenizer, stopWords); // remove stop words
        tokenStream = new PorterStemFilter(tokenStream); // stem words
        return new TokenStreamComponents(tokenizer, tokenStream); // return the components
    }

    public String stem(String text){
        if(text == null || text.isEmpty()) return null;
        try(TokenStream tokenStream = tokenStream(null, text)){ // null is the field
            StringBuilder sb = new StringBuilder(); // collect the stemmed tokens
            CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class); // get the token text
            tokenStream.reset(); // reset the token stream
            while(tokenStream.incrementToken()){ // iterate over the tokens
                String term = charTermAttribute.toString(); // get the token text
                if(protectedTerms.contains(term)) continue; // skip the protected terms
                sb.append(term).append(" "); // append the stemmed token to the StringBuilder
            }
            tokenStream.end(); // end the token stream
            return sb.toString().trim(); // return the stemmed text
        }
        catch(IOException e){ // handle any exceptions
            return new RuntimeException(e).getMessage();
        }
    }
}
