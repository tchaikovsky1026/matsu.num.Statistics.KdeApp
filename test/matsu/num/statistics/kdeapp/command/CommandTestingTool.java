/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.23
 */
package matsu.num.statistics.kdeapp.command;

/**
 * コマンドに関するテスティングツール.
 * 
 * @author Matsuura Y.
 */
public final class CommandTestingTool {

    private CommandTestingTool() {
        // インスタンス化不可
        throw new AssertionError();
    }

    /**
     * 与えられたコマンドの文字列表現を列挙する.
     * 
     * @param commands コマンド
     * @throws NullPointerException 引数や引数の要素がnullの場合
     */
    public static void stdout(Iterable<? extends ConsoleOptionCommand<?>> commands) {
        for (ConsoleOptionCommand<?> c : commands) {
            System.out.println(c + ":");
            for (String s : c.representations()) {
                System.out.println("  " + s);
            }
        }
    }
}
