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

import matsu.num.statistics.kdeapp.help.CategorizationFormatter;

/**
 * ヘルプのディスプレイ表示.
 * 
 * @author Matsuura Y.
 */
final class Kde1dHelpDisplayRunner {

    /**
     * 唯一のコンストラクタ.
     */
    Kde1dHelpDisplayRunner() {
    }

    /**
     * ヘルプ表示を実行する.
     */
    void run() {
        for (String s : new CategorizationFormatter().format(CommandDescriptions.get())) {
            System.out.println(s);
        }
    }
}
