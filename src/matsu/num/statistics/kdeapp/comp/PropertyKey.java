/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.4.13
 */
package matsu.num.statistics.kdeapp.comp;

import java.util.Objects;

/**
 * プロパティキーを扱う.
 * 
 * <p>
 * プロパティとは, config ファイル等で指定される Key-Value の組み合わせであり,
 * Key, Value とも {@code String} である.
 * </p>
 * 
 * <p>
 * このクラスは, プロパティ名による equality, comparability を提供する.
 * </p>
 * 
 * @author Matsuura Y.
 */
public final class PropertyKey implements Comparable<PropertyKey> {

    private final String propertyName;

    /**
     * 非公開の唯一のコンストラクタ.
     * 
     * @param propertyName プロパティ名
     * @throws IllegalArgumentException 不正な場合
     * @throws NullPointerException 引数がnullの場合
     */
    private PropertyKey(String propertyName) {
        validatePropertyName(propertyName);
        this.propertyName = propertyName;
    }

    /**
     * プロパティ名を返す.
     * 
     * @return プロパティ名
     */
    String name() {
        return propertyName;
    }

    /**
     * 文字列がプロパティ名として適切かを検証する.
     * 
     * @param propertyName プロパティ名
     * @throws IllegalArgumentException 不正な場合
     * @throws NullPointerException 引数がnullの場合
     */
    private static void validatePropertyName(String propertyName) {
        Objects.requireNonNull(propertyName);
    }

    /** 与えたインスタンスが自身と等価かどうかを判定する. */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof PropertyKey target)) {
            return false;
        }

        return this.propertyName.equals(target.propertyName);
    }

    /** このインスタンスのハッシュコードを返す. */
    @Override
    public int hashCode() {
        return propertyName.hashCode();
    }

    /** このインスタンスの文字列表現を返す. */
    @Override
    public String toString() {
        return propertyName;
    }

    /**
     * 与えたインスタンスと自身とを比較する.
     * 
     * @throws NullPointerException 引数がnullの場合
     */
    @Override
    public int compareTo(PropertyKey other) {
        return this.propertyName.compareTo(other.propertyName);
    }

    /**
     * プロパティ名を与えて, プロパティキーを構築する.
     * 
     * @param propertyName プロパティ名
     * @return プロパティキー
     * @throws IllegalArgumentException 不正な場合
     * @throws NullPointerException 引数がnullの場合
     */
    public static PropertyKey of(String propertyName) {
        return new PropertyKey(propertyName);
    }
}
