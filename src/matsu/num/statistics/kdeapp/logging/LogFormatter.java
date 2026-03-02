/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.2
 */
package matsu.num.statistics.kdeapp.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * ロギングのフォーマッターを表現する.
 * 
 * @author Matsuura Y.
 */
final class LogFormatter extends Formatter {

    /**
     * タイムスタンプのフォーマット
     */
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault());

    /**
     * 唯一のコンストラクタ.
     */
    LogFormatter() {
        super();
    }

    @Override
    public String format(LogRecord record) {

        String timestamp = DATE_FORMAT.format(Instant.ofEpochMilli(record.getMillis()));

        StringBuilder sb = new StringBuilder();
        sb.append(timestamp);
        sb.append(" [");
        sb.append(record.getLoggerName());
        sb.append("] ");
        sb.append(record.getLevel().getName());
        sb.append(" - ");
        sb.append(formatMessage(record));
        sb.append('\n');

        // 例外情報
        Throwable thrown = record.getThrown();
        if (Objects.nonNull(thrown)) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            thrown.printStackTrace(pw);
            pw.flush();

            sb.append(sw.toString());
        }

        return sb.toString();
    }
}
