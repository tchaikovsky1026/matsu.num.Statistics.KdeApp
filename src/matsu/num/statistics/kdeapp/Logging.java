/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.26
 */
package matsu.num.statistics.kdeapp;

import static java.util.logging.Level.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * プロジェクト (アプリケーション) 全体のロギングを扱う.
 * 
 * <p>
 * エントリーポイントの最初で, 初期化メソッドを呼ぶようにすること.
 * </p>
 * 
 * @author Matsuura Y.
 */
public final class Logging {

    private static final String DIR_NAME = "logs";

    private static boolean initialized = false;

    private Logging() {
        // インスタンス化不可
    }

    /**
     * ロギングを初期化する.
     * 
     * <p>
     * 必ず, アプリケーションの最初に初期化すること.
     * </p>
     * 
     * @param appName アプリケーションの名前
     */
    public static synchronized void init(String appName) {
        if (appName == null) {
            appName = "unknown";
        }

        // 2度の初期化は不能
        if (initialized) {
            RuntimeException e = new IllegalStateException("already initialized");
            Logger.getLogger(Logging.class.getName())
                    .log(WARNING, "Logging initialization failed", e);
            return;
        }

        // ルートロガーが構築できたかに関わらず, 初期化完了とする.
        initialized = true;

        try {
            Logger root = Logger.getLogger("");
            root.setUseParentHandlers(false);

            // ハンドラをいったん削除
            removeAllHandlers(root);

            // ファイルハンドラの用意
            if (!DIR_NAME.isBlank()) {
                Files.createDirectories(Path.of(DIR_NAME));
            }
            FileHandler fh = new FileHandler(logFileName(appName));
            fh.setFormatter(new MyFormatter());

            // コンソールハンドラの用意
            ConsoleHandler ch = new ConsoleHandler();
            ch.setFormatter(new MyFormatter());
            ch.setLevel(WARNING);

            root.addHandler(fh);
            root.addHandler(ch);
        } catch (IOException e) {
            Logger.getLogger(
                    Logging.class.getName())
                    .log(WARNING, "Logging initialization failed", e);
        }
    }

    /**
     * ロガーのハンドラを削除する非公開メソッド.
     */
    private static void removeAllHandlers(Logger logger) {
        Handler[] handlers = logger.getHandlers();
        for (Handler h : handlers) {
            logger.removeHandler(h);
        }
    }

    /**
     * ログファイルのファイル名 (ディレクトリ名を含む) を構築する.
     * 
     * <p>
     * ディレクトリは logs であり,
     * タイムスタンプが追加される.
     * </p>
     * 
     * @param appName アプリケーション名
     * @return ファイル名
     */
    private static String logFileName(String appName) {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss"));

        String prefix = DIR_NAME.isBlank()
                ? ""
                : DIR_NAME + "/";
        return prefix + appName + "-" + timestamp + ".log";
    }

    private static final class MyFormatter extends Formatter {

        private static final DateTimeFormatter DATE_FORMAT =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        .withZone(ZoneId.systemDefault());

        /**
         * 唯一のコンストラクタ.
         */
        MyFormatter() {
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
            if (thrown != null) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                thrown.printStackTrace(pw);
                pw.flush();

                sb.append(sw.toString());
            }

            return sb.toString();
        }
    }
}
