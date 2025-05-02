package com.example.ToDo.Helper;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;

public class LuceneHelper {
    public static void main(String[] args) throws ParseException {
        StandardAnalyzer analyzer = new StandardAnalyzer();// Create an instance of StandardAnalyzer
        QueryParser parser = new QueryParser("content", analyzer);// Create an instance of QueryParser

        String userQuery = "hello world";// The user's query
        Query query = parser.parse(userQuery);// Parse the user's query

        System.out.println(query.toString());// Print the parsed query
    }
}
