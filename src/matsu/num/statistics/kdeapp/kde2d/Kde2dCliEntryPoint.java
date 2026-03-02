/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.2
 */
package matsu.num.statistics.kdeapp.kde2d;

import matsu.num.statistics.kdeapp.exception.ApplicationException;
import matsu.num.statistics.kdeapp.logging.AppLogger;

/**
 * 2次元カーネル密度推定のエントリーポイント.
 * 
 * @author Matsuura Y.
 */
public final class Kde2dCliEntryPoint {

    private Kde2dCliEntryPoint() {
        // インスタンス化不可
        throw new AssertionError();
    }

    /**
     * エントリーポイント.
     * 
     * @param args パラメータ
     */
    public static void main(String[] args) {
        int exitCode = 0;
        try {
            AppLogger.init("kde2d");
            LoggerHolder.LOGGER.info("start kde2d");
            exitCode = new Kde2dCliWithStyle050().run(args);
        } catch (ApplicationException ae) {
            exitCode = ae.getExitCode();
            LoggerHolder.LOGGER.severe("throws " + ae.toStringForLogging());
        } catch (Exception e) {
            exitCode = 1;
            // 不明な例外はスタックトレースを記録する
            LoggerHolder.LOGGER.severe("throws unknown exception", e);
        } finally {
            LoggerHolder.LOGGER.info("end kde2d, exit-code=" + exitCode);
            System.exit(exitCode);
        }
    }

    private static final class LoggerHolder {
        static final AppLogger LOGGER = AppLogger.getLogger(Kde2dCliEntryPoint.class);
    }
}
