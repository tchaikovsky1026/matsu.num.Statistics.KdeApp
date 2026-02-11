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
import matsu.num.statistics.kdeapp.kde1d.exception.InvalidParameterException;

/**
 * オプションコマンドの指定に関するルールを表現する.
 * 
 * @author Matsuura Y.
 */
public sealed interface CommandAssignmentRule permits GroupingRule, NullRule, CompositeRule {

    /**
     * 指定されたコマンドの集合がルールに合っているかどうかを検証する.
     * 
     * <p>
     * メソッドの実行中に引数が変更されることは想定されていない.
     * </p>
     * 
     * @param allCommands 指定されたコマンドの全体
     * @throws InvalidParameterException パラメータが不正であった場合
     * @throws NullPointerException 引数にnullが含まれる場合 (スローされない場合もある)
     */
    public abstract void validate(Set<ConsoleOptionCommand> allCommands);

    /**
     * コマンドの集合のうち, 0個または1個指定されるべきコマンドであることを要求するルールを作成する.
     * 
     * @param managedCommands 管理されるコマンド
     * @return ルール
     * @throws IllegalArgumentException 引数が空の場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public static CommandAssignmentRule singleOptionalRule(ConsoleOptionCommand... managedCommands) {
        return GroupingRule.singleOptionalRule(managedCommands);
    }

    /**
     * コマンドの集合のうち, 1個指定されるべきコマンドであることを要求するルールを作成する.
     * 
     * @param managedCommands 管理されるコマンド
     * @return ルール
     * @throws IllegalArgumentException 引数が空の場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public static CommandAssignmentRule singleRequiredRule(ConsoleOptionCommand... managedCommands) {
        return GroupingRule.singleRequiredRule(managedCommands);
    }

    /**
     * 与えたルールを結合した新しいルールを返す.
     * 
     * @param one ルールの1つ
     * @param others それ以外
     * @return 結合したルール
     * @throws NullPointerException nullを含む場合
     */
    public static CompositeRule composite(
            CommandAssignmentRule one, CommandAssignmentRule... others) {
        return CompositeRule.composite(one, others);
    }

    /**
     * null-ルール (バリデーションしないルール) を返す.
     * 
     * @return null-ルール
     */
    public static CommandAssignmentRule nullRule() {
        return NullRule.INSTANCE;
    }
}
