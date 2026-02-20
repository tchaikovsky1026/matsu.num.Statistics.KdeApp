/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.20
 */
package matsu.num.statistics.kdeapp.format;

import static java.util.stream.Collectors.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * データの区切り文字を表現する値クラス.
 * 
 * <p>
 * 区切り文字の値に基づく equality, comparability を提供する.
 * </p>
 * 
 * <p>
 * 区切り文字として扱える文字は次の通りである.
 * </p>
 * 
 * <ul>
 * <li>ASCII 1文字</li>
 * </ul>
 * 
 * @author Matsuura Y.
 */
public final class Separator implements Comparable<Separator> {

    private final char value;

    /**
     * 唯一のコンストラクタ. <br>
     * 引数はバリデーションされない.
     */
    private Separator(char value) {
        this.value = value;
    }

    /**
     * このインスタンスが扱う区切り文字を {@code char} 型として返す.
     * 
     * @return 区切り文字
     */
    public char charValue() {
        return value;
    }

    /**
     * このインスタンスの区切り文字を {@link String} 型で返す.
     * 
     * @return 区切り文字
     */
    public String asString() {
        return String.valueOf(value);
    }

    /**
     * 与えられたインスタンスとの等価性を判定する.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Separator target)) {
            return false;
        }

        return this.value == target.value;
    }

    /**
     * このインスタンスのハッシュコードを返す.
     */
    @Override
    public int hashCode() {
        return Character.hashCode(value);
    }

    /**
     * このインスタンスの文字列表現を返す.
     * 
     * <p>
     * 文字列表現は規定されていない. <br>
     * おそらく次のような形である. <br>
     * {@code Separator('%separator')}
     * </p>
     */
    @Override
    public String toString() {
        return "%s(\'%s\')"
                .formatted(getClass().getSimpleName(), charValue());
    }

    /**
     * 与えられたインスタンスとの比較を行う. <br>
     * {@link Comparable#compareTo(Object)} と同一の例府外スロー条件である.
     */
    @Override
    public int compareTo(Separator o) {
        return Character.compare(this.value, o.value);
    }

    /**
     * 与えた文字列から区切り文字を得る. <br>
     * 引数不正の場合, 例外 ({@link IllegalArgumentException}) をスローする.
     * 
     * <p>
     * 区切り文字の与え方は,
     * このクラスが扱える1文字のみを与えるほか,
     * エスケープシーケンスとして与えることが許されている. <br>
     * 列挙定数で用意されている.
     * </p>
     * 
     * @param s 文字列
     * @return 区切り文字, 不正なら {@code null}
     * @throws IllegalArgumentException 引数を解釈できない場合
     * @throws NullPointerException 引数がnull
     */
    public static Separator from(String s) {

        // エスケープシーケンスを確かめる
        Character escape = MapperHolder.mapper.get(s);
        if (Objects.nonNull(escape)) {
            return new Separator(escape.charValue());
        }

        if (s.length() == 1) {
            // ASCII かどうかを判定して返す.
            char c = s.charAt(0);
            if (c <= 0x7F) {
                return new Separator(c);
            }
        }

        throw new IllegalArgumentException(
                "invalid separator: \"%s\"".formatted(s));
    }

    /**
     * エスケープシーケンスを表現する列挙定数.
     */
    public static enum EscapeSequence {

        /**
         * タブ. <br>
         * "\t" という文字列がタブ1文字を表現する.
         */
        TAB("\\t", '\t'),

        /**
         * バックスラッシュ. <br>
         * "\\" という文字列がバックスラッシュ1文字を表現する.
         */
        BACKSLASH("\\\\", '\\');

        private final String representation;
        private final char charValue;

        private EscapeSequence(String representation, char charValue) {
            this.representation = representation;
            this.charValue = charValue;
        }

        /**
         * エスケープシーケンスとしての文字列を返す.
         * 
         * @return エスケープシーケンスとしての文字列
         */
        public String representation() {
            return representation;
        }

        /**
         * エスケープシーケンスがあらわす文字を返す.
         * 
         * @return 文字
         */
        public char toChar() {
            return charValue;
        }
    }

    /**
     * エスケープシーケンスにおける, representation から charValue へのマッパホルダ.
     */
    private static final class MapperHolder {

        static final Map<String, Character> mapper;

        static {
            mapper = Arrays.stream(EscapeSequence.values())
                    .collect(toMap(EscapeSequence::representation, EscapeSequence::toChar));
        }
    }
}
