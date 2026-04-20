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

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Property から Resolver を生成することに関する, 設計図を表現する.
 * 
 * <p>
 * この設計図は, プロパティリスト (コンテナ) から必要な {@link PropertyKey}
 * に対する値を読み出し, Resolver を生成することを責務とする. <br>
 * 利用側は, {@link #compute(PropertyContainer)}
 * メソッドにプロパティリストを渡すことで Resolver が得られる;
 * ただし, プロパティリストが設計図を「発火」しなかった場合は構築を行わない.
 * </p>
 * 
 * <p>
 * 設計図の生成には, {@link #of(ResolverKey, Set, Function)} メソッドを用いる.
 * </p>
 * 
 * <p>
 * 型情報として, {@link #compute(PropertyContainer)} メソッドで返る Resolver に対する
 * {@link ResolverKey} を渡す.
 * </p>
 * 
 * <p>
 * {@link #compute(PropertyContainer)} メソッドで設計図が「発火」するかどうかを制御するために,
 * 引数 {@code triggers} でトリガーとなる {@link PropertyKey} を渡す. <br>
 * {@link #compute(PropertyContainer)} メソッドの引数である {@link PropertyContainer}
 * のキーに,
 * トリガーとなる {@link PropertyKey} が<b>1個も含まれない</b>場合は「発火」が起こらず空が返る. <br>
 * <b>1個以上含む</b>場合は「発火」し, 構築を試みる. <br>
 * このトリガー {@link PropertyKey} により,
 * 「Property を指定していない」と「Property の指定が不正 &middot; 不足」を識別する.
 * </p>
 * 
 * <p>
 * 具体的な構築処理は, 引数
 * {@code computer: Function<}{@link PropertyContainer}{@code , T>}
 * で渡す. <br>
 * 呼び出し側は, {@code computer} の実装を用意する必要がある (ラムダ式の形でよい). <br>
 * {@code computer} は, 引数としてプロパティリスト (key-value のマップ) を受け取り,
 * 必要な {@link PropertyKey} に対する値を抽出し, Resolver を構築するという関数である.
 * </p>
 * 
 * @author Matsuura Y.
 * @param <T> Resolver の型
 */
public final class ResolverDesign<T> {

    private final ResolverKey<T> resolverKey;
    private final Set<PropertyKey> triggers;
    private final Function<? super PropertyContainer, ? extends T> computer;

    /**
     * @param resolverKey ResolverKey
     * @param triggers この設計図を発火させるためのプロパティキー
     * @param computer Property から Resolver を生成するための関数
     * @throws NullPointerException 引数にnullを含む場合
     */
    private ResolverDesign(ResolverKey<T> resolverKey, Set<PropertyKey> triggers,
            Function<? super PropertyContainer, ? extends T> computer) {
        this.resolverKey = resolverKey;
        this.triggers = triggers;
        this.computer = computer;
    }

    /**
     * この設計図により生成される Resolver に対応した {@link ResolverKey} を返す.
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
     * 例外 ({@link IllegalArgumentException}) をスローする.
     * </p>
     * 
     * @param properties プロパティリスト
     * @return Resolver 得られた Resolver, 発火されなかった場合は空
     * @throws IllegalArgumentException 構築に失敗した場合
     * @throws NullPointerException 引数にnullを含む場合
     */
    Optional<T> compute(PropertyContainer properties) {
        if (triggers.stream().anyMatch(
                key -> properties.contains(key))) {
            try {
                return Optional.of(computer.apply(properties));
            } catch (RuntimeException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        } else {
            return Optional.empty();
        }
    }

    /**
     * 設計図を生成する.
     * 
     * <p>
     * 生成に関する概要はクラス説明を参照すること.
     * </p>
     * 
     * <p>
     * 設計図の構築のための関数 (computer) についての契約は次である. <br>
     * 必要な Property value がそろっていない場合や {@link PropertyKey} に対する値が不正の場合,
     * {@link RuntimeException} のサブタイプをスローするようにする. <br>
     * {@link PropertyKey} に対する値の {@code null} チェックは不要である. <br>
     * スローした {@link RuntimeException} には, メッセージを整備すること.
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
            Function<? super PropertyContainer, ? extends T> computer) {
        return new ResolverDesign<>(
                Objects.requireNonNull(resolverKey),
                Set.copyOf(triggers),
                Objects.requireNonNull(computer));
    }
}
