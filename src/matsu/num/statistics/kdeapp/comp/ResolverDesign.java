/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.4.4
 */
package matsu.num.statistics.kdeapp.comp;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Property から Resolver を生成することに関する, 設計図を表現する.
 * 
 * @author Matsuura Y.
 * @param <T> Resolver の型
 */
public final class ResolverDesign<T> {

    private final ResolverKey<T> resolverKey;
    private final Set<PropertyKey> triggers;
    private final Function<? super Map<PropertyKey, String>, ? extends T> computer;

    /**
     * @param resolverKey ResolverKey
     * @param triggers この設計図を発火させるためのプロパティキー
     * @param computer Property から Resolver を生成するための関数
     * @throws NullPointerException 引数にnullを含む場合
     */
    private ResolverDesign(ResolverKey<T> resolverKey, Set<PropertyKey> triggers,
            Function<? super Map<PropertyKey, String>, ? extends T> computer) {
        this.resolverKey = resolverKey;
        this.triggers = triggers;
        this.computer = computer;
    }

    /**
     * この設計図の {@link ResolverKey} を返す.
     * 
     * @return {@link ResolverKey}
     */
    ResolverKey<T> resolverKey() {
        return resolverKey;
    }

    /**
     * プロパティリスト (key-value のマップ) を与えて Resolver を生成する.
     * 
     * <p>
     * 与えられたプロパティリストに trigger を1個も含まない場合は, 発火されずに空が返る
     * (Resolver の構築を試みない). <br>
     * 1個以上含む場合は構築を試みる. <br>
     * 構築に失敗した場合 (プロパティが不足, あるいはプロパティの値が不正の場合),
     * 例外をスローする.
     * </p>
     * 
     * @param properties プロパティリスト
     * @return Resolver 得られた Resolver, 発火されなかった場合
     * @throws IllegalArgumentException 構築に失敗した場合
     * @throws NullPointerException 引数にnullを含む場合
     */
    Optional<T> compute(Map<PropertyKey, String> properties) {
        if (triggers.stream().anyMatch(
                key -> properties.keySet().contains(key))) {
            return Optional.of(
                    computer.apply(properties));
        } else {
            return Optional.empty();
        }
    }

    /**
     * 設計図を構築する.
     * 
     * <p>
     * 設計図の構築の際に, Property から Resolver を生成するための関数 (computer) を与える. <br>
     * これは, 引数としてプロパティリスト (key-value のマップ) を受け取り,
     * 必要な {@link PropertyKey} に対する値を抽出し, Resolver を構築するという関数である. <br>
     * 必要な Property がそろっていない場合や {@link PropertyKey} に対する値が不正の場合,
     * {@link IllegalArgumentException} をスローするようにする. <br>
     * {@link PropertyKey} に対する値の {@code null} チェックは不要である. <br>
     * スローした {@link IllegalArgumentException} には,
     * メッセージを整備すること.
     * </p>
     * 
     * <p>
     * この設計図 (あるいは computer) が発火するのは,
     * トリガーとなる {@link PropertyKey} を1個以上含む場合である. <br>
     * このトリガー {@link PropertyKey} により,
     * 「Property を指定していない」と「Property の指定が不正」を識別する.
     * </p>
     * 
     * @param <T> Resolver の型
     * @param resolverKey {@link ResolverKey}
     * @param triggers この設計図を発火させるための {@link PropertyKey}
     * @param computer Property から Resolver を生成するための関数
     * @return 設計図
     * @throws NullPointerException 引数にnullを含む場合
     */
    public static <T> ResolverDesign<T> of(
            ResolverKey<T> resolverKey, Set<PropertyKey> triggers,
            Function<? super Map<PropertyKey, String>, ? extends T> computer) {
        return new ResolverDesign<>(
                Objects.requireNonNull(resolverKey),
                Set.copyOf(triggers),
                Objects.requireNonNull(computer));
    }
}
