/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.11
 */
package matsu.num.statistics.kdeapp.kde1d.command.rule;

import java.util.Set;

import matsu.num.statistics.kdeapp.kde1d.command.ConsoleOptionCommand;

/**
 * null-ルール (バリデーションしないルール) を表現する.
 * 
 * @author Matsuura Y.
 */
final class NullRule implements CommandAssignmentRule {

    /**
     * シングルトンインスタンス.
     */
    static final NullRule INSTANCE = new NullRule();

    /**
     * 唯一の非公開コンストラクタ.
     */
    private NullRule() {
        super();
    }

    @Override
    public void validate(Set<ConsoleOptionCommand> allCommands) {
        // バリデーションはしない, nullチェックもしない
    }
}
