/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.21
 */
package matsu.num.statistics.kdeapp.format;

/**
 * コメント開始文字列を表現する値クラス.
 * 
 * <p>
 * 文字列に基づく equality, comparability を提供する.
 * </p>
 * 
 * <p>
 * コメント開始文字列は, 前後にブランクを持たない.
 * </p>
 * 
 * @author Matsuura Y.
 */
public final class CommentPrefix implements Comparable<CommentPrefix> {

    private final String value;

    /**
     * 唯一のコンストラクタ. <br>
     * 引数はバリデーションされない.
     */
    private CommentPrefix(String value) {
        this.value = value;
    }

    /**
     * このインスタンスのコメント開始文字列を {@link String} 型で返す.
     * 
     * @return コメント開始文字列
     */
    public String asString() {
        return value;
    }

    /**
     * 与えられた文字列が, このインスタンスが扱う prefix で開始されているかを判断する.
     * 
     * @param line 検証する文字列
     * @return この prefix で開始されている場合は true
     * @throws NullPointerException 引数がnullの場合
     */
    public boolean matches(String line) {
        return line.startsWith(this.value);
    }

    /**
     * 与えられたインスタンスとの等価性を判定する.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof CommentPrefix target)) {
            return false;
        }

        return this.value.equals(target.value);
    }

    /**
     * このインスタンスのハッシュコードを返す.
     */
    @Override
    public int hashCode() {
        return value.hashCode();
    }

    /**
     * このインスタンスの文字列表現を返す.
     * 
     * <p>
     * 文字列表現は規定されていない. <br>
     * おそらく次のような形である. <br>
     * {@code CommentPrefix("%prefix")}
     * </p>
     */
    @Override
    public String toString() {
        return "%s(\"%s\")"
                .formatted(getClass().getSimpleName(), asString());
    }

    /**
     * 与えられたインスタンスとの比較を行う. <br>
     * {@link Comparable#compareTo(Object)} と同一の例外スロー条件である.
     */
    @Override
    public int compareTo(CommentPrefix o) {
        return this.value.compareTo(o.value);
    }

    /**
     * 与えられた文字列をコメント開始文字列とするインスタンスを返す.
     * 
     * <p>
     * コメント開始文字列として次を満たさなければならない.
     * </p>
     * 
     * <ul>
     * <li>前後にブランクを持たない.</li>
     * <li>空文字でない.</li>
     * </ul>
     * 
     * @param s コメント開始文字列の候補
     * @return インスタンス
     * @throws IllegalArgumentException 引数が不正の場合
     * @throws NullPointerException 引数がnull
     */
    public static CommentPrefix of(String s) {

        if (!s.equals(s.strip())) {
            throw new IllegalArgumentException("lead or trail white space");
        }
        if (s.isEmpty()) {
            throw new IllegalArgumentException("blank");
        }
        return new CommentPrefix(s);
    }
}
