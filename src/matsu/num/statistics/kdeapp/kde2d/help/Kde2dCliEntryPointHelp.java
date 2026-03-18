/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.18
 */
package matsu.num.statistics.kdeapp.kde2d.help;

/**
 * kde1d のヘルプ表示用エントリーポイント.
 * 
 * @author Matsuura Y.
 */
public final class Kde2dCliEntryPointHelp {

    private Kde2dCliEntryPointHelp() {
        // インスタンス化不可
        throw new AssertionError();
    }

    /**
     * エントリーポイント.
     * 
     * @param args ignored
     */
    public static void main(String[] args) {
        new Kde2dHelpDisplayRunner().run();
    }
}
