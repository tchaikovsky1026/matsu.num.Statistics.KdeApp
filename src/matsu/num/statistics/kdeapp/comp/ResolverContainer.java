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

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Resolver のコンテナ: {@link ResolverKey} から値へのマップを扱う.
 * 
 * @author Matsuura Y.
 */
public final class ResolverContainer {

    private final Map<ResolverKey<?>, Object> map;

    private ResolverContainer(Map<ResolverKey<?>, Object> map) {
        this.map = Map.copyOf(map);
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
    public <T> T get(ResolverKey<? extends T> key) {
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
    public ResolverContainer withOverrides(ResolverContainer overrides) {
        Map<ResolverKey<?>, Object> outMap = new HashMap<>();
        outMap.putAll(this.map);
        outMap.putAll(overrides.map);
        return new ResolverContainer(outMap);
    }

    /**
     * 自身を元に, 足りないプロパティをデフォルトで補完した新しいコンフィグプロパティを返す.
     * 
     * @param defaults デフォルトプロパティ
     * @return 補完後のプロパティ
     * @throws NullPointerException 引数がnullの場合
     */
    public ResolverContainer withDefaults(ResolverContainer defaults) {
        return defaults.withOverrides(this);
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

        private volatile Map<ResolverKey<?>, Object> map;

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
         * @param <T> 値の型
         * @param key キー
         * @param value 値
         * @return すでに登録されていた場合は古い値, 未登録の場合はnull
         * @throws IllegalStateException 既にビルドされている場合
         * @throws NullPointerException 引数にnullが含まれる場合
         */
        public <T> T put(ResolverKey<T> key, T value) {
            validateIfCanBuild();

            // キャストを試みて型を検査する
            // ジェネリクスが適切に使われているなら, キャストは成功する
            return key.cast( // 登録済みの場合は古い値が戻る
                    map.put(key, key.cast(Objects.requireNonNull(value))));
        }

        /**
         * ビルドする.
         * 
         * @return コンテナ
         * @throws IllegalStateException 既にビルドされている場合
         */
        public ResolverContainer build() {
            validateIfCanBuild();
            Map<ResolverKey<?>, Object> buildMap = map;
            map = null;
            return new ResolverContainer(buildMap);
        }

        /** このビルダの状態を検証する. */
        private void validateIfCanBuild() {
            if (Objects.isNull(map)) {
                throw new IllegalStateException("already built");
            }
        }
    }
}
