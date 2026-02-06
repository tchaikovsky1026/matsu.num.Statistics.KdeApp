/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.6
 */
package matsu.num.statistics.kdeapp.kde1d.command;

import java.util.ArrayList;
import java.util.List;

/**
 * コンソールオプションコマンドの列挙を表現するクラス.
 * 
 * @author Matsuura Y.
 */
public abstract sealed class ConsoleOptionCommand
        permits ArgumentRequiringCommand, NoArgumentCommand {

    private final String enumString;

    private final String commandString;
    private final List<String> expressions;

    /**
     * 唯一のコンストラクタ.
     * 
     * @param enumString インスタンスの文字列表現
     * @param commandString コマンドの正式な文字列表現
     * @param asString 正式表現以外の文字列表現
     */
    ConsoleOptionCommand(String enumString, String commandString, String... asString) {

        this.enumString = enumString;

        this.commandString = commandString;

        this.expressions = new ArrayList<>();
        this.expressions.add(commandString);
        this.expressions.addAll(List.of(asString));

        if (this.expressions.stream().anyMatch(String::isBlank)) {
            throw new AssertionError(this.toString() + ": blank string expression");
        }
    }

    /**
     * コマンドの正式な文字列表現を返す.
     * 
     * @return 正式な文字列表現
     */
    public final String commandString() {
        return commandString;
    }

    /**
     * コマンド文字列表現のリストを得る. <br>
     * おそらくイミュータブルである.
     * 
     * @return 文字列表現リスト
     */
    final List<String> expressions() {
        return List.copyOf(expressions);
    }

    /**
     * このインスタンスの文字列表現を返す.
     */
    @Override
    public final String toString() {
        return this.enumString;
    }
}
