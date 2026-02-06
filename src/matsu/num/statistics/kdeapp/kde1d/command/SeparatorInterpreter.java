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

import static java.util.stream.Collectors.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * 区切り文字の解釈を行うクラス.
 * 
 * <p>
 * ルールは次の通りである. <br>
 * エスケープシーケンスは, 列挙定数で用意されている.
 * </p>
 * 
 * <ul>
 * <li>ASCII 1文字</li>
 * <li>エスケープシーケンス</li>
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

        if (s.length() == 1) {
            // ASCII かどうかを判定して返す.
            char c = s.charAt(0);
            return c <= 0x7F ? c : null;
        }

        // バックスラッシュ1文字はここまで到達しない
        if (s.startsWith("\\")) {
            return EscapeSequence.from(s);
        }

        return null;
    }

    /**
     * エスケープシーケンスは, バックスラッシュで開始されることを前提とする.
     * 
     * @author Matsuura Y.
     */
    static enum EscapeSequence {

        /**
         * タブ.
         */
        TAB("\\t", '\t'),

        /**
         * キャリッジリターン.
         */
        CR("\\r", '\r'),

        /**
         * ラインフィード.
         */
        LF("\\n", '\n'),

        /**
         * バックスラッシュ.
         */
        BACKSLASH("\\\\", '\\');

        private final String stringValue;
        private final char charValue;

        private EscapeSequence(String stringValue, char charValue) {
            this.stringValue = stringValue;
            this.charValue = charValue;
        }

        String stringValue() {
            return stringValue;
        }

        char charValue() {
            return charValue;
        }

        /**
         * 文字列をエスケープシーケンスとして文字を取得する.
         * 
         * @param s 文字列
         * @return 文字, 該当する物がない場合はnull
         * @throws NullPointerException 引数がnull
         */
        static Character from(String s) {
            return MapperHolder.mapper.get(
                    Objects.requireNonNull(s));
        }

        /**
         * String から char へのマッパホルダ.
         */
        private static final class MapperHolder {

            static final Map<String, Character> mapper;

            static {
                mapper = Arrays.stream(EscapeSequence.values())
                        .collect(toMap(EscapeSequence::stringValue, EscapeSequence::charValue));
            }
        }
    }
}
