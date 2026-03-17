/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.17
 */
package matsu.num.statistics.kdeapp.help;

import java.util.Objects;

/**
 * コマンドのカテゴリを表現するクラス. <br>
 * イミュータブルである.
 * 
 * <p>
 * このクラスのインスタンスは, 与えた {@code id} インスタンスの equality を自身の equality 判定に用いる. <br>
 * このクラスが表すカテゴリ名は, {@code id} インスタンスの {@code toString} に一致する.
 * </p>
 * 
 * @author Matsuura Y.
 */
public final class CommandCategory {

    private final Object id;

    /**
     * 非公開のコンストラクタ.
     * 
     * @param id id
     * @throws NullPointerException 引数がnullの場合
     */
    private CommandCategory(Object id) {
        this.id = Objects.requireNonNull(id);
    }

    /**
     * このインスタンスのカテゴリ名を返す.
     * 
     * <p>
     * カテゴリ名は, {@code id} インスタンスの {@code toString} に一致する.
     * </p>
     * 
     * @return このインスタンスのカテゴリ名
     */
    public String categoryName() {
        return this.id.toString();
    }

    /**
     * 与えたインスタンスとの等価性を判定する.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof CommandCategory target)) {
            return false;
        }

        return this.id.equals(target.id);
    }

    /**
     * このインスタンスのハッシュコードを返す.
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * このインスタンスの文字列表現を返す.
     * 
     * <p>
     * 文字列表現は仕様が確定していない. <br>
     * おそらく次のようなものだろう. <br>
     * {@code Category(%id)}
     * </p>
     */
    @Override
    public String toString() {
        return "Category(%s)".formatted(id);
    }

    /**
     * ID となるインスタンスから, カテゴリインスタンスを構築する.
     * 
     * <p>
     * 構築されたインスタンスのカテゴリ名 {@link #categoryName()} は,
     * {@code id} インスタンスの {@code toString} に一致する. <br>
     * {@code id} インスタンスはイミュータブルでなければならない.
     * </p>
     * 
     * @param id id
     * @return インスタンス
     * @throws NullPointerException 引数がnullの場合
     */
    public static CommandCategory from(Object id) {
        return new CommandCategory(id);
    }
}
