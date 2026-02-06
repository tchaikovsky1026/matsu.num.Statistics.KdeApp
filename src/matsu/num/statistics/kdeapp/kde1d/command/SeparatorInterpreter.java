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

/**
 * 区切り文字の解釈を行うクラス.
 * 
 * <p>
 * ルールは次の通りである.
 * </p>
 * 
 * <ul>
 * <li>ASCII 1文字</li>
 * <li>エスケープ文字</li>
 * </ul>
 * 
 * @author Matsuura Y.
 */
final class SeparatorInterpreter {

    private SeparatorInterpreter() {
        // インスタンス化不可
        throw new AssertionError();
    }

    /**
     * 与えた文字列から区切り文字を得る. <br>
     * 引数不正の場合, {@code null} を返す.
     * 
     * @param s 文字列
     * @return 区切り文字, 不正なら {@code null}
     * @throws NullPointerException 引数がnull
     */
    static Character from(String s) {
        if (s.length() == 0) {
            return null;
        }

        return s.charAt(0);
    }
}
