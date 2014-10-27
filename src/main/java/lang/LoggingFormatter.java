package lang;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LoggingFormatter extends Formatter {

    private static int nameSize = 20;
    private static String formatString = "[%-" + nameSize + "s] %s%n";

    @Override
    public String format(LogRecord record) {
        String name = record.getLoggerName();
        if (name.length() >= nameSize) {
            name = "..." + name.substring(name.length() - nameSize + 3);
        }
        return String.format(formatString, name, record.getMessage()).toString();
    }
}
