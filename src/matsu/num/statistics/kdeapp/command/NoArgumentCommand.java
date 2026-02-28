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

/**
 * 引数をとらないコマンドを扱うクラス.
 * 
 * @author Matsuura Y.
 */
public final class NoArgumentCommand extends ConsoleOptionCommand {

    /**
     * 内部から呼ばれる唯一のコンストラクタ.
     * 
     * @param enumString インスタンスの文字列表現
     * @param commandRepresentation コマンドの正式な文字列表現
     * @param otherRepresentations 正式表現以外の文字列表現
     * @throws IllegalArgumentException ブランクを含む場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    private NoArgumentCommand(String enumString,
            String commandRepresentation, String... otherRepresentations) {
        super(enumString, commandRepresentation, otherRepresentations);
    }

    /**
     * インスタンスを構築する.
     * 
     * @param enumString インスタンスの文字列表現
     * @param commandRepresentation コマンドの正式な文字列表現
     * @param otherRepresentations 正式表現以外の文字列表現
     * @return インスタンス
     * @throws IllegalArgumentException 文字列がブランクを含む場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public static NoArgumentCommand of(
            String enumString,
            String commandRepresentation, String... otherRepresentations) {

        return new NoArgumentCommand(enumString, commandRepresentation, otherRepresentations);
    }
}
