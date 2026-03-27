/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.28
 */
package matsu.num.statistics.kdeapp.comp;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * モジュールコンポーネントの Resolve に関わるキー (Resolver 名) を扱う.
 * 
 * <p>
 * インスタンスの identity に基づく equalty を提供する.
 * </p>
 * 
 * @author Matsuura Y.
 * @param <T> この Resolver が扱う (提供する) 値の型
 */
public final class ResolverKey<T> {

    private final String resolverName;
    private final Class<T> valueType;

    /**
     * 唯一のコンストラクタ.
     *
     * @param resolverName プロパティ名
     * @param valueType 値の型
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    private ResolverKey(String resolverName, Class<T> valueType) {
        this.valueType = Objects.requireNonNull(valueType);
        this.resolverName = resolverName;
    }

    /**
     * 安全に T 型にキャストする.
     * 
     * @param valueObj キャストするインスタンス
     * @return キャストされたobj
     * @throws ClassCastException キャストに失敗した場合
     */
    T cast(Object valueObj) {
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
        return resolverName;
    }

    /**
     * Resolver キーを返す.
     * 
     * @param <T> 扱う値の型
     * @param resolverName Resolver 名
     * @param valueType 値の型トークン
     * @return Resolver キー
     * @throws NullPointerException 引数がnullの場合
     */
    public static <T> ResolverKey<T> of(String resolverName, Class<T> valueType) {
        return new ResolverKey<T>(resolverName, valueType);
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
     * @deprecated resolverName は文字列をキーとしないので, 正規化の必要がない.
     */
    @Deprecated(forRemoval = true)
    public static <T> void requireNoNameDuplicates(
            Collection<? extends T> objetcs,
            Function<? super T, ? extends ResolverKey<?>> mapper) {

        // 含まれるオブジェクトから, 重複のないキーを取り出す
        Set<ResolverKey<?>> keySet = objetcs.stream()
                .map(mapper)
                .collect(Collectors.toSet());

        Set<String> nameSet = new HashSet<>();
        for (ResolverKey<?> key : keySet) {
            if (!nameSet.add(key.resolverName)) {
                throw new IllegalArgumentException("duplicate property name: " + key.resolverName);
            }
        }
    }
}
