/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.17
 */
package matsu.num.statistics.kdeapp.kde2d.command.rule;

import java.util.Objects;
import java.util.Set;

import matsu.num.statistics.kdeapp.exception.IllegalParameterException;
import matsu.num.statistics.kdeapp.kde2d.command.ConsoleOptionCommand;

/**
 * kde2dの禁止されたコマンドを表現するルール.
 * 
 * @author Matsuura Y.
 */
final class ProhibitedCommandRule implements CommandAssignmentRule {

    private final ConsoleOptionCommand prohibitedCommand;

    /**
     * 非公開のコンストラクタ.
     * 
     * <p>
     * 引数はバリデーションされていない(nullチェックも)
     * </p>
     */
    private ProhibitedCommandRule(ConsoleOptionCommand prohibitedCommand) {
        super();
        this.prohibitedCommand = prohibitedCommand;
    }

    /**
     * @throws IllegalParameterException {@inheritDoc }
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public void validate(Set<ConsoleOptionCommand> allCommands) {
        if (allCommands.contains(prohibitedCommand)) {
            throw new IllegalParameterException(
                    "prohibited command: " + prohibitedCommand.commandString());
        }
    }

    /**
     * 与えたコマンドを禁止するルールを作成する.
     * 
     * @param prohibitedCommand 禁止コマンド
     * @return 禁止コマンドルール
     * @throws NullPointerException 引数がnullの場合
     */
    static ProhibitedCommandRule of(ConsoleOptionCommand prohibitedCommand) {
        return new ProhibitedCommandRule(
                Objects.requireNonNull(prohibitedCommand));
    }
}
