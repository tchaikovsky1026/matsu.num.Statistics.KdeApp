/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.12
 */
package matsu.num.statistics.kdeapp.kde2d.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import matsu.num.statistics.kdeapp.base.ThrowableSupplier;
import matsu.num.statistics.kdeapp.format.DelimitedLineParser;

/**
 * {@code double} 値配列としてのデータを構築するローダー.
 * 
 * @author Matsuura Y.
 */
final class DoubleDataLoader {

    private final DelimitedLineParser parser;

    /**
     * 文字列を {@code double} 値に変換するパーサーを与えて,
     * ローダーを構築する.
     * 
     * @param parser パーサー
     * @throws NullPointerException 引数がnull
     */
    DoubleDataLoader(DelimitedLineParser parser) {
        super();
        this.parser = Objects.requireNonNull(parser);
    }

    /**
     * 2カラムの数値データである文字列についての, 1行ごとの文字列ストリームから,
     * {@code double} 値を解析し, 2次元配列として返す. <br>
     * 結果は, 列優先 (double[columns][recordSize]) の形式である.
     * 
     * <p>
     * 実行には, ストリームのサプライヤ ({@link ThrowableSupplier}) を渡す. <br>
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
     * @throws IOException {@link ThrowableSupplier} によるストリームの生成で例外が発生した場合,
     *             文字列フォーマットが不正の場合
     * @throws NullPointerException 引数やストリームの要素にnullを含む場合
     */
    public double[][] load(
            ThrowableSupplier<
                    ? extends Stream<? extends String>,
                    ? extends IOException> linesSupplier)
            throws IOException {

        try (Stream<? extends String> lines = linesSupplier.get()) {

            // 結果格納用のリスト
            List<List<Double>> columnData = new ArrayList<>();
            IntStream.range(0, parser.columns())
                    .forEach(i -> columnData.add(new ArrayList<>()));

            // データの方向を転置するための処理
            lines.map(s -> parser.apply(s, Double::parseDouble, Double.class))
                    .flatMap(Optional::stream)
                    .forEach(d -> {
                        for (int i = 0, len = parser.columns(); i < len; i++) {
                            columnData.get(i).add(d[i]);
                        }
                    });
            return columnData.stream()
                    .map(list -> list.stream().mapToDouble(Double::doubleValue).toArray())
                    .toArray(double[][]::new);
        } catch (IllegalArgumentException e) {
            throw new IOException("illegal format: " + e.getMessage());
        }
    }
}
