/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.3
 */
package matsu.num.statistics.kdeapp.kde2d;

/**
 * デバッグ用の2次元カーネル密度推定のエントリーポイント.
 * 
 * @author Matsuura Y.
 */
public final class Kde2dCliEntryPointDebug {

    private Kde2dCliEntryPointDebug() {
        // インスタンス化不可
        throw new AssertionError();
    }

    /**
     * エントリーポイント.
     * 
     * @param args パラメータ
     */
    public static void main(String[] args) {
        Kde2dCliAppBody.exe(args, true);
    }
}
