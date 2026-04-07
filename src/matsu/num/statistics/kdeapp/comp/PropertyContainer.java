/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.4.7
 */
package matsu.num.statistics.kdeapp.comp;

import static java.util.stream.Collectors.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

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
     * 引数はそのまま代入されるので, 世に出し元で参照漏洩, 安全なMapに詰め替えなど, 適切な処理をすること.
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
     * 例外をスローする. <br>
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
     * 与えられたキーに対する値を取得する. <br>
     * このメソッドは, {@link ResolverDesign} の computer の実装に用いることを想定している.
     * 
     * <p>
     * キーが登録されていない場合は例外 {@link IllegalArgumentException} をスローする. <br>
     * 例外メッセージは次である. <br>
     * {@literal "require key: <key>"}
     * </p>
     * 
     * @param key キー
     * @return 値
     * @throws NullPointerException 引数がnullの場合
     */
    public String getOrThrow(PropertyKey key) {
        String value = properties.get(Objects.requireNonNull(key));
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("require key: " + key);
        }
        return value;
    }

    /**
     * 与えられたキーに対する値を加工して取得する. <br>
     * このメソッドは, {@link ResolverDesign} の computer の実装に用いることを想定している.
     * 
     * <p>
     * キーが登録されていない場合は {@link #getOrThrow(PropertyKey)} と同様の例外をスローする. <br>
     * キーが登録されているが, 加工に失敗した場合,
     * 例外 {@link IllegalArgumentException} をスローする. <br>
     * 例外メッセージは次である. <br>
     * {@literal "Unexpected value: <key>=<value>"}
     * </p>
     * 
     * <p>
     * 引数として加工方法を {@link Function} で与える. <br>
     * この {@link Function} は, 加工できない場合は
     * {@link IllegalArgumentException}
     * をスローするようにすること.
     * </p>
     * 
     * @param <R> 加工後の型
     * @param key キー
     * @param converter 加工方法, 加工できない場合は
     *            {@link IllegalArgumentException}
     *            をスローする
     * @return 加工された値
     * @throws NullPointerException 引数がnullの場合
     */
    public <R> R convertOrThrow(PropertyKey key, Function<? super String, ? extends R> converter) {
        String value = getOrThrow(key);
        try {
            return converter.apply(value);
        } catch (IllegalArgumentException iae) {
            throw new IllegalArgumentException(
                    "Unexpected value: " + key + "=" + value);
        }
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
     * コンテナのビルダ.
     * スレッドセーフでない.
     */
    public static final class Builder {

        private final Map<String, PropertyKey> preparedKeysMap;

        private Map<PropertyKey, String> properties;

        /**
         * 与えられたプロパティキーのセットを候補に持つ, コンテナのビルダを作成する.
         * 
         * @param preparedKeys 候補プロパティキーのセット
         * @throws NullPointerException 引数にnullが含まれる場合
         */
        public Builder(Set<PropertyKey> preparedKeys) {
            try {
                this.preparedKeysMap = preparedKeys.stream()
                        .collect(toMap(k -> k.name(), k -> k));
            } catch (IllegalStateException ise) {
                throw new IllegalArgumentException("key duplicates");
            }

            properties = new HashMap<>();
        }

        /**
         * 非公開のコピーコンストラクタ.
         * 
         * <p>
         * src はビルド前でなければならない (呼び出し元で確認すること).
         * </p>
         */
        private Builder(Builder src) {
            preparedKeysMap = src.preparedKeysMap;
            properties = new HashMap<>(src.properties);
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
         * このビルダのコピーを返す.
         * 
         * @return コピー
         * @throws IllegalStateException 既にビルドされている場合
         */
        Builder copy() {
            validateIfCanBuild();
            return new Builder(this);
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
     * 標準APIのプロパティ機能からプロパティコンテナの生成を行うクラス.
     */
    public static final class StdApiReader {

        private final Builder baseBuilder;

        /**
         * 与えられたプロパティキーのセットを候補に持つ, 標準APIコンテナのビルダを作成する.
         * 
         * @param preparedKeys 候補プロパティキーのセット
         * @throws NullPointerException 引数にnullが含まれる場合
         */
        public StdApiReader(Set<PropertyKey> preparedKeys) {
            this.baseBuilder = new Builder(preparedKeys);
        }

        /**
         * {@code java.util.Properties} からコンテナを生成する.
         * 
         * @param properties Properties
         * @return プロパティコンテナ
         */
        public PropertyContainer convert(java.util.Properties properties) {
            Builder copyBuilder = baseBuilder.copy();
            for (String propertyName : properties.stringPropertyNames()) {
                if (Objects.nonNull(
                        copyBuilder.put(propertyName, properties.getProperty(propertyName)))) {
                    throw new IllegalArgumentException("duplicated key: " + propertyName);
                }
            }
            return copyBuilder.build();
        }
    }
}
