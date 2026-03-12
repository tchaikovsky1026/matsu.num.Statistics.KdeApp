/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.3
 */
package matsu.num.statistics.kdeapp.kde1d;

import matsu.num.statistics.kdeapp.exception.ApplicationException;
import matsu.num.statistics.kdeapp.logging.AppLogger;

/**
 * 1次元カーネル密度推定のアプリケーションの本体.
 * 
 * @author Matsuura Y.
 */
final class Kde1dCliAppBody {

    private Kde1dCliAppBody() {
        // インスタンス化不可
        throw new AssertionError();
    }

    /**
     * エントリーポイントから直接呼び出し,
     * このメソッドの後には何も記述してはならない.
     * 
     * @param debugMode デバッグモードとするかどうか
     */
    static void exe(String[] args, boolean debugMode) {
        int exitCode = 0;
        try {
            if (debugMode) {
                AppLogger.initWithDebugMode("kde1d");
                LoggerHolder.LOGGER.info("debug mode");
            } else {
                AppLogger.init("kde1d");
            }
            LoggerHolder.LOGGER.info("start kde1d");
            exitCode = new Kde1dCliRunner().run(args);
        } catch (ApplicationException ae) {
            exitCode = ae.getExitCode();
            LoggerHolder.LOGGER.severe(ae.toStringForLogging(), ae);
        } catch (Exception e) {
            exitCode = 1;
            LoggerHolder.LOGGER.severe(e.getClass().getName(), e);
        } finally {
            LoggerHolder.LOGGER.info("end kde1d, exit-code=" + exitCode);
            System.exit(exitCode);
        }
    }

    private static final class LoggerHolder {
        static final AppLogger LOGGER = AppLogger.getLogger(Kde1dCliEntryPointDebug.class);
    }
}
