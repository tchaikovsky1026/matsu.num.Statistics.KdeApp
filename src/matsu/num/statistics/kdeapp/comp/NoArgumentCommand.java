/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.23
 */
package matsu.num.statistics.kdeapp.comp;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * 引数をとらないコマンドを扱うクラス.
 * 
 * @author Matsuura Y.
 * @param <T> このコマンドによって得られる値の型
 */
public final class NoArgumentCommand<T> extends ConsoleOptionCommand<T> {

    private final Supplier<? extends T> supplier;

    /**
     * 内部から呼ばれる唯一のコンストラクタ.
     * 
     * <p>
     * サプライヤは例外をスローしてはならず,
     * {@code null} を返してはいけない.
     * </p>
     * 
     * @param enumString インスタンスの文字列表現
     * @param propertyKey 対応するプロパティキー
     * @param supplier このコマンドが指定されている場合に呼ばれる生成器
     * @param commandRepresentation コマンドの正式な文字列表現
     * @param otherRepresentations 正式表現以外の文字列表現
     * @throws IllegalArgumentException ブランクを含む場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    private NoArgumentCommand(String enumString, PropertyKey<T> propertyKey,
            Supplier<? extends T> supplier,
            String commandRepresentation, String... otherRepresentations) {
        super(enumString, propertyKey, commandRepresentation, otherRepresentations);

        this.supplier = Objects.requireNonNull(supplier);
    }

    /**
     * T型インスタンスを生成する.
     * 
     * @return インスタンス
     */
    final T get() {
        return supplier.get();
    }

    /**
     * インスタンスを構築する.
     * 
     * @param <T> このコマンドによって得られる値の型
     * @param enumString インスタンスの文字列表現
     * @param propertyKey 対応するプロパティキー
     * @param supplier このコマンドが指定されている場合に呼ばれる生成器
     * @param commandRepresentation コマンドの正式な文字列表現
     * @param otherRepresentations 正式表現以外の文字列表現
     * @return インスタンス
     * @throws IllegalArgumentException 文字列がブランクを含む場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public static <T> NoArgumentCommand<T> of(
            String enumString, PropertyKey<T> propertyKey,
            Supplier<? extends T> supplier,
            String commandRepresentation, String... otherRepresentations) {

        return new NoArgumentCommand<>(
                enumString, propertyKey, supplier,
                commandRepresentation, otherRepresentations);
    }
}
