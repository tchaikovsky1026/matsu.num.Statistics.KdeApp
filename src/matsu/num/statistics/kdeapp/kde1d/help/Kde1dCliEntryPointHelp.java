/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.18
 */
package matsu.num.statistics.kdeapp.kde1d.help;

/**
 * kde1d のヘルプ表示用エントリーポイント.
 * 
 * @author Matsuura Y.
 */
public final class Kde1dCliEntryPointHelp {

    private Kde1dCliEntryPointHelp() {
        // インスタンス化不可
        throw new AssertionError();
    }

    /**
     * エントリーポイント.
     * 
     * @param args ignored
     */
    public static void main(String[] args) {
        new Kde1dHelpDisplayRunner().run();
    }
}
