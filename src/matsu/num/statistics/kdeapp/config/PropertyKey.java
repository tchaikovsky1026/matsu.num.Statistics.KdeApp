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
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    private final String propertyName;
    private final Class<T> valueType;

    /**
     * 唯一のコンストラクタ.
     *
     * @param propertyName プロパティ名
     * @param valueType 値の型
     * @throws IllegalArgumentException 文字列表現に空白を含む場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    private PropertyKey(String propertyName, Class<T> valueType) {
        this.valueType = Objects.requireNonNull(valueType);
        this.propertyName = propertyName;

        if (propertyName.chars().anyMatch(Character::isWhitespace)) {
            throw new IllegalArgumentException(this.toString() + " includes white space");
        }
    }

    /**
     * このインスタンスのプロパティ名を返す.
     * 
     * @return プロパティ名
     */
    public String propertyName() {
        return propertyName;
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
     * このインスタンスの文字列表現を返す.
     */
    @Override
    public String toString() {
        return propertyName();
    }

    /**
     * プロパティキーを返す.
     * 
     * @param <T> 扱う値の型
     * @param propertyName プロパティ名
     * @param valueType 値の型
     * @return プロパティキー
     * @throws IllegalArgumentException 文字列表現に空白を含む場合
     * @throws NullPointerException 引数がnullの場合
     */
    public static <T> PropertyKey<T> of(String propertyName, Class<T> valueType) {
        return new PropertyKey<T>(propertyName, valueType);
    }

    /**
     * 与えたクラス (clazz) に定義されている {@link PropertyKey} 型のstaticフィールドの値を得る. <br>
     * ただし, clazz とフィールドは {@code public} でなければならない.
     * 
     * @param clazz clazz
     * @return clazz クラスに定義された public static な {@link PropertyKey} フィールドの値
     * @throws NullPointerException 引数にnullを含む場合
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
                PropertyKey<?> p = type.cast(f.get(null));
                constantFieldSet.add(p);
            } catch (IllegalAccessException | ClassCastException ignore) {
                //無関係なフィールドなら無視する
            }
        }
        return constantFieldSet;
    }

    /**
     * プロパティキーへのマッピングが可能なオブジェクトの集合についてマッピングを行い,
     * 「プロパティキーが異なるならプロパティ名も異なる」ことを確認する.
     * 
     * @param <T> プロパティキーへのマッピングができるオブジェクトの型
     * @param objetcs オブジェクトの集合
     * @param mapper プロパティキーへのマッパ
     * @throws IllegalArgumentException プロパティ名に重複がある場合
     * @throws NullPointerException 引数にnullを含む場合
     */
    public static <T> void requireNoNameDuplicates(
            Collection<? extends T> objetcs,
            Function<? super T, ? extends PropertyKey<?>> mapper) {

        // 含まれるオブジェクトから, 重複のないキーを取り出す
        Set<PropertyKey<?>> keySet = objetcs.stream()
                .map(mapper)
                .collect(Collectors.toSet());

        Set<String> nameSet = new HashSet<>();
        for (PropertyKey<?> key : keySet) {
            if (!nameSet.add(key.propertyName())) {
                throw new IllegalArgumentException("duplicate property name: " + key.propertyName());
            }
        }
    }
}
