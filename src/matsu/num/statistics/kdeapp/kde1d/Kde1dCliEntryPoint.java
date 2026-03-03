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
        Kde1dCliAppBody.exe(args, false);
    }
}
