package com.patent.evaluator.pageablesearch.util;

import java.time.Instant;
import java.util.Date;

public interface DateParser {

    Date parse(String dateString);

    Instant parseAsInstant(String dateString);
}
