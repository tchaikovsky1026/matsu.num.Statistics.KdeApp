/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.18
 */
package matsu.num.statistics.kdeapp.command;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import matsu.num.statistics.kdeapp.exception.IllegalParameterException;

/**
 * オプションコマンドの指定に関するルールを表現する.
 * 
 * @author Matsuura Y.
 */
public sealed abstract class CommandAssignmentRule {

    /**
     * 指定されたコマンドの集合がルールに合っているかどうかを検証する.
     * 
     * <p>
     * メソッドの実行中に引数が変更されることは想定されていない.
     * </p>
     * 
     * @param allCommands 指定されたコマンドの全体
     * @throws IllegalParameterException パラメータが不正であった場合
     * @throws NullPointerException 引数にnullが含まれる場合 (スローされない場合もある)
     */
    public abstract void validate(Set<? extends ConsoleOptionCommand> allCommands);

    /**
     * コマンドの集合のうち, 0個または1個指定されるべきコマンドであることを要求するルールを作成する.
     * 
     * @param one 管理されるコマンドのうちの一つ
     * @param others それ以外
     * @return ルール
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public static CommandAssignmentRule singleOptionalRule(
            ConsoleOptionCommand one, ConsoleOptionCommand... others) {
        return new SingleOptionalRule(one, others);
    }

    /**
     * コマンドの集合のうち, 1個指定されるべきコマンドであることを要求するルールを作成する.
     * 
     * @param one 管理されるコマンドのうちの一つ
     * @param others それ以外
     * @return ルール
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public static CommandAssignmentRule singleRequiredRule(
            ConsoleOptionCommand one, ConsoleOptionCommand... others) {
        return new SingleRequiredRule(one, others);
    }

    /**
     * 与えたコマンドを禁止するルールを作成する.
     * 
     * @param prohibitedCommand 禁止コマンド
     * @return 禁止コマンドルール
     * @throws NullPointerException 引数がnullの場合
     */
    public static CommandAssignmentRule prohibitedCommandRule(ConsoleOptionCommand prohibitedCommand) {
        return new ProhibitedCommandRule(prohibitedCommand);
    }

    /**
     * 与えたルールを結合した新しいルールを返す.
     * 
     * @param one ルールの1つ
     * @param others それ以外
     * @return 結合したルール
     * @throws NullPointerException nullを含む場合
     */
    public static CommandAssignmentRule composite(
            CommandAssignmentRule one, CommandAssignmentRule... others) {
        return new CompositeRule(one, others);
    }

    /**
     * null-ルール (バリデーションしないルール) を返す.
     * 
     * @return null-ルール
     */
    public static CommandAssignmentRule nullRule() {
        return NullRule.INSTANCE;
    }

    /**
     * null-ルール (バリデーションしないルール) を表現する.
     */
    private static final class NullRule extends CommandAssignmentRule {

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
        public void validate(Set<? extends ConsoleOptionCommand> allCommands) {
            // バリデーションはしない, nullチェックもしない
        }
    }

    /**
     * 禁止されたコマンドを表現するルール.
     */
    private static final class ProhibitedCommandRule extends CommandAssignmentRule {

        private final ConsoleOptionCommand prohibitedCommand;

        /**
         * 与えたコマンドを禁止するルールを作成する.
         * 
         * @param prohibitedCommand 禁止コマンド
         * @throws NullPointerException 引数がnullの場合
         */
        ProhibitedCommandRule(ConsoleOptionCommand prohibitedCommand) {
            super();
            this.prohibitedCommand = prohibitedCommand;
        }

        /**
         * @throws IllegalParameterException {@inheritDoc }
         * @throws NullPointerException {@inheritDoc }
         */
        @Override
        public void validate(Set<? extends ConsoleOptionCommand> allCommands) {
            if (allCommands.contains(prohibitedCommand)) {
                throw new IllegalParameterException(
                        "prohibited command: " + prohibitedCommand.commandString());
            }
        }
    }

    /**
     * 複数のルールを結合した (バリデーションを "and" でつないだ) ルールを表現する.
     */
    private static final class CompositeRule extends CommandAssignmentRule {

        private final Set<CommandAssignmentRule> ruleElements;

        /**
         * 与えたルールを結合した新しいルールを返す.
         * 
         * @param one ルールの1つ
         * @param others それ以外
         * @throws NullPointerException nullを含む場合
         */
        CompositeRule(CommandAssignmentRule one, CommandAssignmentRule... others) {
            super();

            List<CommandAssignmentRule> rules = new ArrayList<>(List.of(one));
            rules.addAll(List.of(others));

            // 入れ子になる構造を展開する
            Set<CommandAssignmentRule> expandedRules = rules.stream()
                    .flatMap(
                            rule -> rule instanceof CompositeRule target
                                    ? target.ruleElements.stream()
                                    : Stream.of(rule))
                    .collect(Collectors.toSet());

            this.ruleElements = expandedRules;
        }

        /**
         * @throws IllegalParameterException {@inheritDoc}
         * @throws NullPointerException {@inheritDoc}
         */
        @Override
        public void validate(Set<? extends ConsoleOptionCommand> allCommands) {
            ruleElements.stream()
                    .forEach(rule -> rule.validate(allCommands));
        }
    }

    /**
     * コマンドの集合に対して作用するルールを扱う.
     */
    private static abstract sealed class GroupingRule extends CommandAssignmentRule {

        private final Set<ConsoleOptionCommand> managedCommands;

        /**
         * 唯一のコンストラクタ.
         * 
         * <p>
         * 必要ない限り, 非公開とする.
         * </p>
         * 
         * @throws NullPointerException 引数にnullが含まれる場合
         */
        GroupingRule(ConsoleOptionCommand one, ConsoleOptionCommand... others) {
            super();

            List<ConsoleOptionCommand> list = new ArrayList<>();
            list.add(one);
            list.addAll(List.of(others));
            this.managedCommands = Set.copyOf(list);
        }

        /**
         * @throws IllegalParameterException {@inheritDoc }
         * @throws NullPointerException {@inheritDoc }
         */
        @Override
        public final void validate(Set<? extends ConsoleOptionCommand> allCommands) {
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
         *               {@linkplain IllegalParameterException} をスローする.
         * 
         * @param interestedCommands オプションで指定されたうちの, 興味あるコマンド
         * @throws IllegalParameterException パラメータが不正であった場合
         */
        abstract void validateConcrete(Set<? extends ConsoleOptionCommand> interestedCommands);

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
    }

    /**
     * 0個または1個指定されるべきルールを表現する.
     */
    private static final class SingleOptionalRule extends GroupingRule {

        /**
         * @throws NullPointerException 引数にnullが含まれる場合
         */
        SingleOptionalRule(ConsoleOptionCommand one, ConsoleOptionCommand... others) {
            super(one, others);
        }

        /**
         * @throws IllegalParameterException {@inheritDoc }
         */
        @Override
        void validateConcrete(Set<? extends ConsoleOptionCommand> interestedCommands) {
            if (interestedCommands.size() >= 2) {
                throw new IllegalParameterException(
                        "exclusive commands: " + managedCommands());
            }
        }
    }

    /**
     * 1個は必ず指定されるべきルールを表現する.
     */
    private static final class SingleRequiredRule extends GroupingRule {

        /**
         * @throws NullPointerException 引数にnullが含まれる場合
         */
        SingleRequiredRule(ConsoleOptionCommand one, ConsoleOptionCommand... others) {
            super(one, others);
        }

        /**
         * @throws IllegalParameterException {@inheritDoc }
         */
        @Override
        void validateConcrete(Set<? extends ConsoleOptionCommand> interestedCommands) {
            if (interestedCommands.size() != 1) {
                throw new IllegalParameterException(
                        "required and exclusive commands: " + managedCommands());
            }
        }
    }
}
