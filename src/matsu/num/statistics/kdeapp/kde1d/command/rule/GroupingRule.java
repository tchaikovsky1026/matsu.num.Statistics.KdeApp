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

import static java.util.stream.Collectors.*;

import java.util.Objects;
import java.util.Set;

import matsu.num.statistics.kdeapp.kde1d.command.ConsoleOptionCommand;
import matsu.num.statistics.kdeapp.kde1d.exception.InvalidParameterException;

/**
 * コマンドの集合に対して作用するルールを扱う.
 * 
 * @author Matsuura Y.
 */
abstract sealed class GroupingRule implements CommandAssignmentRule {

    /**
     * コマンドの集合のうち, 0個または1個指定されるべきコマンドであることを要求するルールを作成する.
     * 
     * @param managedCommands 管理されるコマンド
     * @return ルール
     * @throws IllegalArgumentException 引数が空の場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    static GroupingRule singleOptionalRule(ConsoleOptionCommand... managedCommands) {
        return new SingleOptionalRule(managedCommands);
    }

    /**
     * コマンドの集合のうち, 1個指定されるべきコマンドであることを要求するルールを作成する.
     * 
     * @param managedCommands 管理されるコマンド
     * @return ルール
     * @throws IllegalArgumentException 引数が空の場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    static GroupingRule singleRequiredRule(ConsoleOptionCommand... managedCommands) {
        return new SingleRequiredRule(managedCommands);
    }

    private final Set<ConsoleOptionCommand> managedCommands;

    /**
     * 唯一のコンストラクタ.
     * 
     * <p>
     * 必要ない限り, 非公開とする.
     * </p>
     * 
     * @throws IllegalArgumentException 引数が空の場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    private GroupingRule(ConsoleOptionCommand... managedCommands) {
        super();
        this.managedCommands = Set.of(managedCommands);

        if (this.managedCommands.stream().anyMatch(Objects::isNull)) {
            throw new NullPointerException("include-null");
        }
        if (this.managedCommands.isEmpty()) {
            throw new IllegalArgumentException("empty");
        }
    }

    /**
     * @throws InvalidParameterException {@inheritDoc }
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public final void validate(Set<ConsoleOptionCommand> allCommands) {
        // managedOptions との共通部分 (興味あるコマンド) でバリデーション
        this.validateConcrete(
                this.managedCommands.stream()
                        .filter(allCommands::contains)
                        .collect(toSet()));
    }

    /**
     * バリデーションの具体的な処理を記述する抽象メソッド. <br>
     * 外部からコールしてはならない.
     * 
     * <p>
     * 内部からコールされたとき, 引数には次の共通部分が与えられる. <br>
     * - {@link #validate(Set)} で与えられた集合 <br>
     * - このインスタンスが管理対象とするコマンドの集合
     * </p>
     * 
     * @implSpec
     *               バリデーションの結果, 不正であった場合は,
     *               {@linkplain InvalidParameterException} をスローする.
     * 
     * @param interestedCommands オプションで指定されたうちの, 興味あるコマンド
     * @throws InvalidParameterException パラメータが不正であった場合
     */
    abstract void validateConcrete(Set<ConsoleOptionCommand> interestedCommands);

    /**
     * 管理対象のコマンドを列挙した文字列表現を返す.
     * 
     * @return コマンド文字列表現
     */
    final String managedCommands() {
        return this.managedCommands.stream()
                .map(c -> "<" + c.commandString() + ">")
                .collect(joining(", "));
    }

    /**
     * 0個または1個指定されるべきルールを表現する.
     */
    private static final class SingleOptionalRule extends GroupingRule {

        /**
         * @throws IllegalArgumentException 引数が空の場合
         * @throws NullPointerException 引数にnullが含まれる場合
         */
        SingleOptionalRule(ConsoleOptionCommand... managedCommands) {
            super(managedCommands);
        }

        /**
         * @throws InvalidParameterException {@inheritDoc }
         */
        @Override
        void validateConcrete(Set<ConsoleOptionCommand> interestedCommands) {
            if (interestedCommands.size() >= 2) {
                throw new InvalidParameterException(
                        "exclusive commands: " + managedCommands());
            }
        }
    }

    /**
     * 1個は必ず指定されるべきルールを表現する.
     */
    private static final class SingleRequiredRule extends GroupingRule {

        /**
         * @throws IllegalArgumentException 引数が空の場合
         * @throws NullPointerException 引数にnullが含まれる場合
         */
        SingleRequiredRule(ConsoleOptionCommand... managedCommands) {
            super(managedCommands);
        }

        /**
         * @throws InvalidParameterException {@inheritDoc }
         */
        @Override
        void validateConcrete(Set<ConsoleOptionCommand> interestedCommands) {
            if (interestedCommands.size() != 1) {
                throw new InvalidParameterException(
                        "required and exclusive commands: " + managedCommands());
            }
        }
    }
}
