/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.27
 */
package matsu.num.statistics.kdeapp.kde1d;

import java.util.logging.Level;
import java.util.logging.Logger;

import matsu.num.statistics.kdeapp.Logging;
import matsu.num.statistics.kdeapp.exception.ApplicationException;

/**
 * 1次元カーネル密度推定のエントリーポイント.
 * 
 * @author Matsuura Y.
 */
public final class Kde1dCliEntryPoint {

    private Kde1dCliEntryPoint() {
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
            Logging.init("kde1d");
            LoggerHolder.LOGGER.info("start kde1d");
            exitCode = new Kde1dCliWithStyle020().run(args);
        } catch (ApplicationException ae) {
            exitCode = ae.getExitCode();
            LoggerHolder.LOGGER.severe("throws " + ae.toStringForLogging());
        } catch (Exception e) {
            exitCode = 1;
            // 不明な例外はスタックトレースを記録する
            LoggerHolder.LOGGER.log(Level.SEVERE, "throws unknown exception", e);
        } finally {
            LoggerHolder.LOGGER.info("end kde1d, exit-code=" + exitCode);
            System.exit(exitCode);
        }
    }

    private static final class LoggerHolder {
        static final Logger LOGGER = Logger.getLogger(Kde1dCliEntryPoint.class.getPackageName());
    }
}
