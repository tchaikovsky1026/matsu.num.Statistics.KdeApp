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
import java.util.function.Function;

/**
 * {@link Optional} を模倣した,
 * key-value マップで key に対する value が存在しないかもしれないことを表現する. <br>
 * イミュータブル, スレッドセーフである.
 * 
 * <p>
 * 特別な equality は提供しない.
 * </p>
 * 
 * @author Matsuura Y.
 * @param <V> 値の型
 */
public final class LookupResult<V> {

    private final Object key;
    private final V value;

    /**
     * 非公開の唯一のコンストラクタ. <br>
     * value に {@code null} を渡した場合は空を意味する.
     * 
     * @throws NullPointerException keyがnull
     */
    private LookupResult(Object key, V value) {
        this.key = Objects.requireNonNull(key);
        this.value = value;
    }

    /**
     * キーに対する値を持つコンテナを返す.
     * 
     * @param <V> 値の型
     * @param key キー
     * @param value 値
     * @return key-value コンテナ
     * @throws NullPointerException 引数にnullを含む場合
     */
    public static <V> LookupResult<V> of(Object key, V value) {
        return ofNullable(key, Objects.requireNonNull(value));
    }

    /**
     * 値が空であることを表現するコンテナを返す.
     * 
     * @param <V> 値の型
     * @param key キー
     * @return 空コンテナ
     * @throws NullPointerException キーがnullの場合
     */
    public static <V> LookupResult<V> empty(Object key) {
        return ofNullable(key, null);
    }

    /**
     * 与えた値を用いてコンテナを返し, 値が {@code null} の場合は空を返す.
     * 
     * @param <V> 値の型
     * @param key キー
     * @param value 値
     * @return key-value コンテナ, {@code value} が {@code null} ならば空
     * @throws NullPointerException キーがnullの場合
     */
    public static <V> LookupResult<V> ofNullable(Object key, V value) {
        return new LookupResult<V>(key, value);
    }

    /** (テスト用) 空である場合はtrue */
    boolean isEmpty() {
        return Objects.isNull(value);
    }

    /** (テスト用) 空でない場合はtrue */
    boolean isPresent() {
        return Objects.nonNull(value);
    }

    /**
     * コンテナが持つ値を取得する. <br>
     * 空の場合は例外 ({@link IllegalStateException}) がスローされる.
     * 
     * <p>
     * このメソッドは, {@link #getOrThrow(Function)} の例外型固定版である.
     * </p>
     * 
     * @return コンテナが持つ値
     * @throws IllegalStateException 空の場合
     */
    public V get() {
        return getOrThrow(IllegalStateException::new);
    }

    /**
     * コンテナが持つ値を取得する. <br>
     * 空の場合は例外がスローされる.
     * 
     * <p>
     * スローされる例外は, 引数で与えたファンクションにより生成される. <br>
     * このファンクションは {@code String} を引数に取り,
     * 例外メッセージを例外インスタンスに変換する関数である. <br>
     * 次のような実装を与えればよい. <br>
     * {@code s -> new Exception(s)} <br>
     * {@code null} を返してはならない.
     * </p>
     * 
     * <p>
     * スローされた例外のメッセージは規定されていない. <br>
     * おそらく次のようなものだろう. <br>
     * {@code "Missing key (require key): <key>"}
     * </p>
     * 
     * @param <X> 空のときにスローされる例外の型
     * @param exFactoryIfEmpty 例外の生成器, 空の場合に呼ばれる
     * @return コンテナが持つ値
     * @throws X 空の場合
     * @throws NullPointerException 引数が null の場合,
     *             Function が null を生成した場合
     *             (常にではない)
     */
    public <X extends Exception> V getOrThrow(
            Function<? super String, ? extends X> exFactoryIfEmpty) throws X {
        if (Objects.isNull(value)) {
            throw exFactoryIfEmpty.apply("Missing key (require key): " + key);
        }
        return value;
    }

    /**
     * コンテナが持つ値を mapper で変換した結果を, 新しいコンテナとして返す. <br>
     * コンテナが空の場合は空が返る. <br>
     * 変換に失敗した場合は例外をスローする.
     * 
     * <p>
     * このメソッドは, {@link #mapOrThrow(Function, Function)} の例外型固定版である.
     * </p>
     * 
     * @param <V2> 変換後の型
     * @param mapper 値の変換器
     * @return 変換後の値を持つコンテナ, 自身が空の場合は空
     * @throws IllegalStateException 変換に失敗した場合
     * @throws NullPointerException 引数が null の場合,
     *             Function が null を生成した場合
     *             (常にではない)
     */
    public <V2> LookupResult<V2> map(
            Function<? super V, ? extends V2> mapper) {
        return mapOrThrow(mapper, IllegalStateException::new);
    }

    /**
     * コンテナが持つ値を mapper で変換した結果を, 新しいコンテナとして返す. <br>
     * コンテナが空の場合は空が返る. <br>
     * 変換に失敗した場合は例外をスローする.
     * 
     * <p>
     * 変換 {@code mapper:Function} を適用したときに例外
     * ({@link RuntimeException}) をスローした場合,
     * 例外生成器 ({@code exFactoryIfFailed}) により例外翻訳を行う. <br>
     * 元の例外のメッセージは無視され,
     * 翻訳された例外のメッセージはおそらく次のようなものだろう. <br>
     * {@code "Unexpected value: [<key>: <value>]"} <br>
     * 例外生成器には次のような実装を与えればよい. <br>
     * {@code s -> new Exception(s)} <br>
     * {@code mapper},
     * {@code exFactoryIfFailed}
     * は {@code null} を返してはならない.
     * </p>
     * 
     * @param <V2> 変換後の型
     * @param <X> 変換に失敗した場合にスローされる例外の型
     * @param mapper 値の変換器
     * @param exFactoryIfFailed 変換に失敗した場合スローする例外生成器
     * @return 変換後の値を持つコンテナ, 自身が空の場合は空
     * @throws X 変換に失敗した場合
     * @throws NullPointerException 引数が null の場合,
     *             Function が null を生成した場合
     *             (常にではない)
     */
    public <V2, X extends Exception> LookupResult<V2> mapOrThrow(
            Function<? super V, ? extends V2> mapper,
            Function<? super String, ? extends X> exFactoryIfFailed) throws X {
        if (Objects.isNull(value)) {
            // クラスがイミュータブル, かつthisが空コンテナなので, キャストは問題ない
            @SuppressWarnings("unchecked")
            LookupResult<V2> out = (LookupResult<V2>) this;
            return out;
        }

        V2 dest;
        try {
            dest = mapper.apply(value);
        } catch (RuntimeException e) {
            throw exFactoryIfFailed.apply("Unexpected value: [" + key + ": " + value + "]");
        }
        // destがnullの場合はここでNullPointerException
        return of(key, dest);
    }

    /**
     * このインスタンスの value をオプショナルとして返す.
     * 
     * @return value を持つオプショナル
     */
    public Optional<V> toOptional() {
        return Optional.ofNullable(value);
    }
}
