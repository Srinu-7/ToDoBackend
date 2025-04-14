package com.example.ToDo.Helper;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;

public class LuceneHelper {
    public static void main(String[] args) throws ParseException {
        StandardAnalyzer analyzer = new StandardAnalyzer();
        QueryParser parser = new QueryParser("content", analyzer);

        String userQuery = "hello world";
        Query query = parser.parse(userQuery);

        System.out.println(query.toString());
    }
}
