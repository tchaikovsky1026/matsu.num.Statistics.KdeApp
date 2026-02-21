/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.21
 */
package matsu.num.statistics.kdeapp.kde1d;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import matsu.num.statistics.kdeapp.format.LineFilter;

/**
 * {@code double} 値配列としてのデータを構築するローダー.
 * 
 * @author Matsuura Y.
 */
final class DoubleDataLoader {

    private final LineFilter lineFilter;

    /**
     * 文字列フィルタを与えて,
     * {@code double} 値を取得するローダーを構築する.
     * 
     * @param lineFilter 文字列フィルタ
     * @throws NullPointerException 引数がnull
     */
    DoubleDataLoader(LineFilter lineFilter) {
        super();
        this.lineFilter = Objects.requireNonNull(lineFilter);
    }

    /**
     * 1行ごとの文字列ストリームから, {@code double} 値を解析し,
     * 配列として返す.
     * 
     * <p>
     * 実行には, ストリームのサプライヤ ({@link IOSupplier}) を渡す. <br>
     * このメソッド内でストリームが生成され, クローズ処理が実行される.
     * </p>
     * 
     * <p>
     * このメソッドは実用においては, <br>
     * {@code () -> Files.lines(inputPath)} <br>
     * を渡されることを主に想定している.
     * </p>
     * 
     * @param linesSupplier supplier
     * @return double[]
     * @throws IOException {@link IOSupplier} によるストリームの生成で例外が発生した場合,
     *             文字列フォーマットが不正の場合
     * @throws NullPointerException 引数やストリームの要素にnullを含む場合
     */
    public double[] load(IOSupplier<Stream<String>> linesSupplier) throws IOException {
        try (Stream<String> lines = linesSupplier.get()) {
            return lines
                    .map(line -> lineFilter.apply(line, Double::parseDouble))
                    .flatMap(Optional::stream)
                    .mapToDouble(Double::doubleValue)
                    .toArray();
        } catch (NumberFormatException e) {
            throw new IOException("illegal number format: " + e.getMessage());
        }
    }
}
