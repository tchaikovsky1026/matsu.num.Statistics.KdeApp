/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.22
 */
package matsu.num.statistics.kdeapp.config;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * コンフィグのプロパティ.
 * 
 * @author Matsuura Y.
 */
public final class ConfigProperty {

    private final Map<PropertyKey<?>, Object> map;

    private ConfigProperty(Map<PropertyKey<?>, Object> map) {
        this.map = map;
    }

    /**
     * キーに対する値を返す.
     * 
     * @param <T> value type
     * @param key key
     * @return 登録されている値
     * @throws NoSuchElementException キーが登録されていない場合
     * @throws NullPointerException 引数がnull
     */
    public <T> T get(PropertyKey<T> key) {
        T out = key.cast(map.get(Objects.requireNonNull(key)));
        if (Objects.isNull(out)) {
            throw new NoSuchElementException("no value of " + key);
        }
        return out;
    }

    /**
     * 自身をベースとして, それを引数でオーバーライドした新しいコンフィグプロパティを返す.
     * 
     * @param overrides オーバーライドするプロパティ
     * @return オーバーライド後のプロパティ
     * @throws NullPointerException 引数がnullの場合
     */
    public ConfigProperty withOverrides(ConfigProperty overrides) {
        Map<PropertyKey<?>, Object> outMap = new HashMap<>();
        outMap.putAll(this.map);
        outMap.putAll(overrides.map);
        return new ConfigProperty(outMap);
    }

    /**
     * 自身を元に, 足りないプロパティをデフォルトで補完した新しいコンフィグプロパティを返す.
     * 
     * @param defaults デフォルトプロパティ
     * @return 補完後のプロパティ
     * @throws NullPointerException 引数がnullの場合
     */
    public ConfigProperty withDefaults(ConfigProperty defaults) {
        return defaults.withOverrides(this);
    }

    /**
     * プロパティビルダ.
     * スレッドセーフでない.
     */
    public static final class Builder {

        private Map<PropertyKey<?>, Object> map;

        /**
         * 唯一のコンストラクタ.
         */
        public Builder() {
            super();
            map = new HashMap<>();
        }

        /**
         * キーと値を登録する.
         * 
         * <p>
         * メソッドチェーンできるように, this を返す.
         * </p>
         * 
         * @param <T> 値の型
         * @param key キー
         * @param value 値
         * @return this
         * @throws IllegalStateException 既にビルドされている場合
         * @throws NullPointerException 引数にnullが含まれる場合
         */
        public <T> Builder put(PropertyKey<T> key, T value) {
            validateIfCanBuild();
            // キャストを試みて型を喧噪する
            // ジェネリクスが適切に使われているなら, キャストは成功する
            map.put(key, key.cast(Objects.requireNonNull(value)));
            return this;
        }

        /**
         * ビルドする.
         * 
         * @return コンフィグプロパティ
         * @throws IllegalStateException 既にビルドされている場合
         */
        public ConfigProperty build() {
            validateIfCanBuild();
            Map<PropertyKey<?>, Object> buildMap = map;
            map = null;
            return new ConfigProperty(buildMap);
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
}
