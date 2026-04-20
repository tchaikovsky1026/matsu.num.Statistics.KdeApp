/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.4.20
 */
package matsu.num.statistics.kdeapp.comp;

import static java.util.stream.Collectors.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * プロパティのコンテナ. <br>
 * key-value のマップに相当する.
 * 
 * @author Matsuura Y.
 */
public final class PropertyContainer {

    private final Map<PropertyKey, String> properties;

    /**
     * 唯一のコンストラクタ.
     * 
     * <p>
     * 引数はそのまま代入されるので, 呼び出し元で参照漏洩, 安全なMapに詰め替えなど, 適切な処理をすること.
     * </p>
     * 
     * @param properties マップ
     */
    private PropertyContainer(Map<PropertyKey, String> properties) {
        this.properties = properties;
    }

    /**
     * 与えられた「設計図」を使って, プロパティを Resolver に変換する.
     * 
     * <p>
     * 自身が持つプロパティ群と設計図が「マッチ」しない場合,
     * 例外 ({@link IllegalArgumentException}) をスローする. <br>
     * 「マッチ」の詳細については, {@link ResolverDesign} の説明を参照のこと.
     * </p>
     * 
     * @param resolverDesigns 設計図
     * @return ResolverContainer
     * @throws IllegalArgumentException 自身と設計図がマッチしない場合
     * @throws NullPointerException 引数がnullの場合
     */
    public ResolverContainer toResolvers(Set<ResolverDesign<?>> resolverDesigns) {
        var builder = new ResolverContainer.Builder();
        for (ResolverDesign<?> design : resolverDesigns) {
            computeResolver(builder, design);
        }
        return builder.build();
    }

    /**
     * このコンテナにキーが登録されているかを判定する. <br>
     * {@link ResolverDesign} から呼ばれることを想定する.
     * 
     * @param key プロパティキー
     * @return 登録されている場合はtrue
     * @throws NullPointerException 引数がnullの場合
     */
    boolean contains(PropertyKey key) {
        return properties.containsKey(Objects.requireNonNull(key));
    }

    /**
     * キーに対する値を含むコンテナを返す. <br>
     * キーが登録されていない場合は空が返る. <br>
     * このメソッドは, {@link ResolverDesign} の computer の実装に用いることを想定している.
     * 
     * @param key キー
     * @return 登録されている値, 登録されていない場合は空
     * @throws NullPointerException 引数がnullの場合
     */
    public LookupResult<String> find(PropertyKey key) {
        return LookupResult.ofNullable(key, properties.get(key));
    }

    /**
     * 設計図から Resolver の compute を試み, builder に登録する. <br>
     * compute が発火しなければ何もしない.
     * 
     * @param <T> Resolver の型
     * @param builder {@link ResolverContainer} のビルダ
     * @param resolverDesign 設計図
     * @throws IllegalArgumentException 発火されたが失敗した場合
     */
    private <T> void computeResolver(
            ResolverContainer.Builder builder, ResolverDesign<T> resolverDesign) {
        resolverDesign.compute(this).ifPresent(
                resolver -> builder.put(resolverDesign.resolverKey(), resolver));
    }

    /**
     * コンテナのビルダ. <br>
     * スレッドセーフでない.
     * 
     * <p>
     * このビルダは再利用不可である: ビルド後には使用できない.
     * </p>
     */
    public static final class Builder {

        private final Map<String, PropertyKey> preparedKeysMap;

        private volatile Map<PropertyKey, String> properties;

        /**
         * 与えられたプロパティキーのセットを候補に持つ, コンテナのビルダを作成する.
         * 
         * @param preparedKeys 候補プロパティキーのセット
         * @throws NullPointerException 引数にnullが含まれる場合
         */
        public Builder(Set<PropertyKey> preparedKeys) {
            // PropertyKey は name に基づく equality を持つので, この toMap は成功する
            // Set.copyOfにより安全性を強化
            this.preparedKeysMap = Set.copyOf(preparedKeys).stream()
                    .collect(toMap(k -> k.name(), k -> k));

            properties = new HashMap<>();
        }

        /**
         * プロパティ名とその値を登録する.
         * 
         * @param propertyName プロパティ名
         * @param value 値
         * @return すでに登録されていた場合は古い値, 未登録の場合はnull
         * @throws IllegalStateException 既にビルドされている場合
         * @throws IllegalArgumentException プロパティ名に対応するプロパティキーが存在しない場合
         * @throws NullPointerException 引数にnullが含まれる場合
         */
        public String put(String propertyName, String value) {
            validateIfCanBuild();
            PropertyKey key = preparedKeysMap.get(Objects.requireNonNull(propertyName));
            if (Objects.isNull(key)) {
                throw new IllegalArgumentException("unknown name: " + propertyName);
            }
            return properties.put(key, Objects.requireNonNull(value));
        }

        /**
         * ビルドする.
         * 
         * @return プロパティコンテナ
         * @throws IllegalStateException 既にビルドされている場合
         */
        public PropertyContainer build() {
            validateIfCanBuild();
            Map<PropertyKey, String> buildMap = properties;
            properties = null;
            return new PropertyContainer(Map.copyOf(buildMap));
        }

        /**
         * このビルダの状態を検証する.
         */
        private void validateIfCanBuild() {
            if (Objects.isNull(properties)) {
                throw new IllegalStateException("already built");
            }
        }
    }

    /**
     * 標準APIのプロパティ機能からプロパティコンテナの生成を行うクラス. <br>
     * このクラスはイミュータブルでスレッドセーフであり, インスタンスの再利用が可能である.
     */
    public static final class StdApiReader {

        private final Set<PropertyKey> preparedKeys;

        /**
         * 与えられたプロパティキーのセットを候補に持つ, 標準APIコンテナのビルダを作成する.
         * 
         * @param preparedKeys 候補プロパティキーのセット
         * @throws NullPointerException 引数にnullが含まれる場合
         */
        public StdApiReader(Set<PropertyKey> preparedKeys) {
            this.preparedKeys = Set.copyOf(preparedKeys);
        }

        /**
         * {@code java.util.Properties} からコンテナを生成する.
         * 
         * @param properties Properties
         * @return プロパティコンテナ
         */
        public PropertyContainer convert(java.util.Properties properties) {
            Builder builder = new Builder(preparedKeys);
            for (String propertyName : properties.stringPropertyNames()) {
                if (Objects.nonNull(
                        builder.put(propertyName, properties.getProperty(propertyName)))) {
                    throw new IllegalArgumentException("duplicated key: " + propertyName);
                }
            }
            return builder.build();
        }
    }
}
