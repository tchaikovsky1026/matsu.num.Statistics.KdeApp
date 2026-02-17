/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.17
 */
package matsu.num.statistics.kdeapp.kde2d;

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
     * @throws Exception 例外スロー時
     */
    public static void main(String[] args) throws Exception {
        System.exit(new Kde2dCliWithStyle050().run(args));
    }
}
