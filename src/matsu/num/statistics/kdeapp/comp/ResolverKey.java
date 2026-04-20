/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.4.18
 */
package matsu.num.statistics.kdeapp.comp;

import java.util.Objects;

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
     * @param resolverName Resolver 名
     * @param valueType 値の型
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    private ResolverKey(String resolverName, Class<T> valueType) {
        this.valueType = Objects.requireNonNull(valueType);
        this.resolverName = Objects.requireNonNull(resolverName);
    }

    /**
     * 安全に T 型にキャストする. <br>
     * {@code null} を渡してもよい
     * ({@code null} が返る).
     * 
     * @param valueObj キャストするインスタンス
     * @return キャストされたobj
     * @throws ClassCastException キャストに失敗した場合
     */
    T cast(Object valueObj) {
        return this.valueType.cast(valueObj);
    }

    /** 等価性を判定する. */
    @Override
    public boolean equals(Object obj) {
        // Object の equals に従う
        return super.equals(obj);
    }

    /** ハッシュコードを返す. */
    @Override
    public int hashCode() {
        // Object の hashCode に従う
        return super.hashCode();
    }

    /** このインスタンスの文字列表現を返す. */
    @Override
    public String toString() {
        return resolverName;
    }

    /**
     * Resolver キーを返す.
     * 
     * <p>
     * Resolver 名を渡すようになっているが,
     * インスタンスの equality や識別には使用されない. <br>
     * 主に, {@link #toString} による文字列生成に使用される. <br>
     * したがって, Resolver 名に対する規約は {@code null} 制約以外には持たない.
     * </p>
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
}
