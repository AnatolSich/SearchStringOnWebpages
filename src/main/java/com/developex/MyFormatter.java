package com.developex;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

class MyFormatter extends Formatter {
    // this method is called for every log records
    public String format(LogRecord rec) {

        Date dat = new Date(rec.getMillis());
        String format = "%1$tb %1$td, %1$tY %1$tl:%1$tM:%1$tS %1$tp %2$s %4$s: %5$s%6$s%n";
        String source;
        if (rec.getSourceClassName() != null) {
            source = rec.getSourceClassName();
            if (rec.getSourceMethodName() != null) {
                source += " " + rec.getSourceMethodName();
            }
        } else {
            source = rec.getLoggerName();
        }
        String message = formatMessage(rec);
        String throwable = "";
        if (rec.getThrown() != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            pw.println();
            rec.getThrown().printStackTrace(pw);
            pw.close();
            throwable = sw.toString();
        }
        System.out.println(message);
        return String.format(format,
                dat,
                source,
                rec.getLoggerName(),
                rec.getLevel().toString(),
                message,
                throwable);

    }

}
