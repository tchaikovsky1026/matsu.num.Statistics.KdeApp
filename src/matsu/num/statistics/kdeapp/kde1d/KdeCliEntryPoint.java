/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.1.21
 */
package matsu.num.statistics.kdeapp.kde1d;

/**
 * 1次元カーネル密度推定のエントリーポイント.
 * 
 * @author Matsuura Y.
 */
public final class KdeCliEntryPoint {

    /**
     * @param args パラメータ
     * @throws Exception 例外スロー時
     */
    public static void main(String[] args) throws Exception {
        System.exit(new Kde1dCli().run(args));
    }
}
