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

import java.util.Set;

/**
 * {@link java.util.Properties java.util.Properties} からプロパティを読み込み,
 * Resolver を構築する仕組み.
 * 
 * @author Matsuura Y.
 */
public final class StandardPropertyToResolvers {

    private final Set<PropertyKey> preparedKeys;
    private final Set<ResolverDesign<?>> resolverDesigns;

    /**
     * ローダーを構築する.
     * 
     * <p>
     * 構築時に, この構築器が扱うプロパティキーの候補と,
     * Resolver の設計図を渡す.
     * </p>
     * 
     * @param preparedKeys 候補プロパティキーのセット
     * @param resolverDesigns Resolver の設計図のセット
     * @throws NullPointerException 引数にnullを含む場合
     */
    public StandardPropertyToResolvers(Set<PropertyKey> preparedKeys, Set<ResolverDesign<?>> resolverDesigns) {
        super();
        this.preparedKeys = Set.copyOf(preparedKeys);
        this.resolverDesigns = Set.copyOf(resolverDesigns);
    }

    /**
     * 与えたプロパティ ({@link java.util.Properties java.util.Properties})
     * から ResolverContainer を構築する.
     * 
     * <p>
     * 与えたプロパティが, 候補に登録されていないキーを含む場合,
     * あるいは, 設計図に対して, 必要なプロパティが不足, または値が不正, の場合は
     * {@link IllegalArgumentException} をスローする
     * (例外メッセージは意味のある文字列である). <br>
     * ただし, 設計図に対して必要なプロパティを全く含まない場合は
     * Container に登録されないのみで例外はスローされない
     * ({@link ResolverDesign} の {@code triggers} で制御される) .
     * </p>
     * 
     * @param properties プロパティ
     * @return プロパティから構築された ResolverContainer
     * @throws IllegalArgumentException プロパティが不正の場合 (メソッド説明文)
     * @throws NullPointerException 引数がnullの場合
     */
    public ResolverContainer parse(java.util.Properties properties) {
        var propertyContainer = new PropertyContainer.StdApiReader(preparedKeys)
                .convert(properties);
        return propertyContainer.toResolvers(resolverDesigns);
    }
}
