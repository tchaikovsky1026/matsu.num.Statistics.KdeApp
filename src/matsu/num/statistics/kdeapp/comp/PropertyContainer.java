/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.31
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

    private final Map<PropertyKey, String> map;

    /**
     * 唯一のコンストラクタ.
     * 
     * <p>
     * 引数はそのまま代入されるので, 世に出し元で参照漏洩, 安全なMapに詰め替えなど, 適切な処理をすること.
     * </p>
     * 
     * @param map マップ
     */
    private PropertyContainer(Map<PropertyKey, String> map) {
        this.map = map;
    }

    /**
     * 与えられた「設計図」を使って, プロパティを Resolver に変換する.
     * 
     * <p>
     * 自身が持つプロパティ群と設計図が完全にマッチしない場合,
     * 例外をスローする.
     * </p>
     * 
     * @param resolverDesign 設計図
     * @return ResolverContainer
     * @throws IllegalArgumentException 自身と設計図がマッチしない場合
     * @throws NullPointerException 引数がnullの場合
     */
    public ResolverContainer toResolvers(Object resolverDesign) {
        // メソッドの箱のみ用意している.
        return null;
    }

    /**
     * コンテナのビルダ.
     * スレッドセーフでない.
     */
    public static final class Builder {

        private final Map<String, PropertyKey> preparedKeysMap;

        private Map<PropertyKey, String> map;

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

            map = new HashMap<>();
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
            map = new HashMap<>(src.map);
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
            return map.put(key, Objects.requireNonNull(value));
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
            Map<PropertyKey, String> buildMap = map;
            map = null;
            return new PropertyContainer(buildMap);
        }

        /**
         * このビルダの状態を検証する.
         */
        private void validateIfCanBuild() {
            if (Objects.isNull(map)) {
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
