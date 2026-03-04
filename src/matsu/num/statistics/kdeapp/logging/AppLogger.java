/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.3
 */
package matsu.num.statistics.kdeapp.logging;

import static java.util.logging.Level.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * このアプリケーションの全体のロギングを扱う.
 * 
 * <p>
 * エントリーポイントの最初で, 初期化メソッドを呼ぶようにすること. <br>
 * 初期化されない間は, ログ出力されない.
 * </p>
 * 
 * @author Matsuura Y.
 */
public final class AppLogger {

    private static final String DIR_NAME = "logs";

    private static volatile boolean initialized = false;
    private static volatile boolean debugMode = false;

    private final Logger logger;

    /**
     * 非公開のコンストラクタ.
     * 
     * @throws NullPointerException null
     */
    private AppLogger(Logger logger) {
        super();
        this.logger = Objects.requireNonNull(logger);
    }

    /**
     * INFO レベルのログを出力する.
     * 
     * @param msg メッセージ
     */
    public void info(String msg) {
        if (initialized) {
            logger.info(msg);
        }
    }

    /**
     * SEVERE レベルのログを出力する.
     * 
     * <p>
     * スローされた例外のロギングの場合,
     * {@link #severe(String, Throwable)} が適切である.
     * </p>
     * 
     * @param msg メッセージ
     */
    public void severe(String msg) {
        if (initialized) {
            logger.severe(msg);
        }
    }

    /**
     * メッセージと該当例外について, SEVERE レベルのログを出力する.
     * 
     * <p>
     * 例外に関する詳細なメッセージがスローされるかどうかは,
     * Debug モードかどうかに従う.
     * </p>
     * 
     * @param msg メッセージ
     * @param thrown 該当例外
     */
    public void severe(String msg, Throwable thrown) {
        if (initialized) {
            if (debugMode) {
                logger.log(Level.SEVERE, msg, thrown);
            } else {
                logger.severe(msg);
            }
        }
    }

    /**
     * ロギングを初期化する.
     * 
     * <p>
     * 必ず, アプリケーションの起動時に初期化すること.
     * </p>
     * 
     * @param appName アプリケーションの名前
     */
    public static synchronized void init(String appName) {
        initHelper(appName, false);
    }

    /**
     * ロギングを Debug モードとして初期化する.
     * 
     * <p>
     * 必ず, アプリケーションの起動時に初期化すること.
     * </p>
     * 
     * @param appName アプリケーションの名前
     */
    public static synchronized void initWithDebugMode(String appName) {
        initHelper(appName, true);
    }

    /**
     * 初期化のヘルパメソッド.
     * appName に null を渡しても良い.
     */
    private static void initHelper(String appName, boolean debugMode) {
        try {
            if (appName == null) {
                appName = "unknown";
            }
            LoggerInitializer.exe(appName);
            AppLogger.debugMode = debugMode;
            initialized = true;
        } catch (Exception e) {
            // おそらく, スローされるのはIOExceptionのみである.
            System.err.println("Logging initialization failed");
        }
    }

    /**
     * 与えた名前を持つロガーを返す.
     * 
     * @param name ロガーの名前
     * @return 名前を持つロガー
     * @throws NullPointerException 引数がnullの場合
     */
    public static AppLogger getLogger(String name) {
        return new AppLogger(Logger.getLogger(name));
    }

    /**
     * クラスに対する名前のロガーを取得する.
     * 
     * @param clazz クラス
     * @return クラスに対する名前のロガー
     * @throws NullPointerException 引数がnullの場合
     */
    public static AppLogger getLogger(Class<?> clazz) {
        Package p = clazz.getPackage();
        if (Objects.isNull(p)) {
            return getLogger(clazz.getName());
        }

        String packageName = p.getName();
        final String filter = "matsu.num.statistics.kdeapp";
        final String replace = "kdeapp";

        // フィルタ文字列が先頭でない場合は置換しないようにした.
        // (置換のみ目的の場合は if 文は不要)
        if (packageName.startsWith(filter)) {
            packageName = packageName.replace(filter, replace);
        }

        return getLogger(packageName);
    }

    /**
     * ロガーの初期化を行う.
     */
    private static final class LoggerInitializer {

        private LoggerInitializer() {
            // インスタンス化不可
        }

        /**
         * ロギングを初期化する.
         * 
         * @param appName アプリケーションの名前
         */
        static void exe(String appName) throws IOException {
            Logger root = Logger.getLogger("");
            root.setUseParentHandlers(false);

            // ハンドラをいったん削除
            removeAllHandlers(root);

            // ファイルハンドラの用意
            if (!DIR_NAME.isBlank()) {
                Files.createDirectories(Path.of(DIR_NAME));
            }
            FileHandler fh = new FileHandler(logFileName(appName));
            fh.setFormatter(new LogFormatter());

            // コンソールハンドラの用意
            ConsoleHandler ch = new ConsoleHandler();
            ch.setFormatter(new LogFormatter());
            ch.setLevel(WARNING);

            root.addHandler(fh);
            root.addHandler(ch);
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
    }
}
