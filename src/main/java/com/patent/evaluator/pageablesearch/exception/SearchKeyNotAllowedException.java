package com.patent.evaluator.pageablesearch.exception;

/**
 * If filtering by a not allowed field is requested, this exception is thrown
 */
public class SearchKeyNotAllowedException extends RuntimeException {

    public SearchKeyNotAllowedException(String key) {
        super("Search key not allowed: " + key);
    }

}