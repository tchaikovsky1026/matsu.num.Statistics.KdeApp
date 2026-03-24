/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.23
 */
package matsu.num.statistics.kdeapp.config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * プロパティのキー (プロパティ名) を扱う.
 * 
 * <p>
 * インスタンスの identity に基づく equalty を提供する.
 * </p>
 * 
 * @author Matsuura Y.
 * @param <T> このプロパティが扱う値の型
 */
public final class PropertyKey<T> {

    private final Class<T> valueType;

    private PropertyKey(Class<T> valueType) {
        this.valueType = Objects.requireNonNull(valueType);
    }

    /**
     * 安全に T 型にキャストする.
     * 
     * @param valueObj キャストするインスタンス
     * @return キャストされたobj
     * @throws ClassCastException キャストに失敗した場合
     */
    public T cast(Object valueObj) {
        return this.valueType.cast(valueObj);
    }

    /**
     * 等価性を判定する.
     */
    @Override
    public boolean equals(Object obj) {
        // Object の equals に従う
        return super.equals(obj);
    }

    /**
     * ハッシュコードを返す.
     */
    @Override
    public int hashCode() {
        // Object の hashCode に従う
        return super.hashCode();
    }

    /**
     * プロパティキーを返す.
     * 
     * @param <T> 扱う値の型
     * @param valueType 値の型
     * @return プロパティキー
     * @throws NullPointerException 引数がnullの場合
     */
    public static <T> PropertyKey<T> of(Class<T> valueType) {
        return new PropertyKey<T>(valueType);
    }

    /**
     * 与えたクラス (clazz) に定義されている {@link PropertyKey} 型のstaticフィールドの値を得る. <br>
     * ただし, clazz とフィールドは {@code public} でなければならない.
     * 
     * @param clazz clazz
     * @return clazz クラスに定義された public static な {@link PropertyKey} フィールドの値
     */
    public static Set<PropertyKey<?>> constantsOf(Class<?> clazz) {
        Set<PropertyKey<?>> constantFieldSet = new HashSet<>();
        @SuppressWarnings("rawtypes")
        Class<PropertyKey> type = PropertyKey.class;

        // staticかつ互換性のあるフィールドのみが対象
        for (Field f : clazz.getFields()) {
            if ((f.getModifiers() & Modifier.STATIC) == 0) {
                continue;
            }
            try {
                constantFieldSet.add(type.cast(f.get(null)));
            } catch (IllegalAccessException | ClassCastException ignore) {
                //無関係なフィールドなら無視する
            }
        }
        return constantFieldSet;
    }
}
