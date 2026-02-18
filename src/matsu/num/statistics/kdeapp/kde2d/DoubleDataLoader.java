/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.17
 */
package matsu.num.statistics.kdeapp.kde2d;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * {@code double} 値配列としてのデータを構築するローダー.
 * 
 * @author Matsuura Y.
 */
final class DoubleDataLoader {

    private final DoubleColumnDoubleLineParser parser;

    /**
     * 文字列を {@code double} 値に変換するパーサーを与えて,
     * ローダーを構築する.
     * 
     * @param parser パーサー
     * @throws NullPointerException 引数がnull
     */
    DoubleDataLoader(DoubleColumnDoubleLineParser parser) {
        super();
        this.parser = Objects.requireNonNull(parser);
    }

    /**
     * 2カラムの数値データである文字列についての, 1行ごとの文字列ストリームから,
     * {@code double} 値を解析し, 2次元配列として返す. <br>
     * 結果は, 列優先 (double[2][recordSize]) の形式である.
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
    public double[][] load(IOSupplier<Stream<String>> linesSupplier) throws IOException {
        try (Stream<String> lines = linesSupplier.get()) {
            List<Double> formerColumn = new ArrayList<>();
            List<Double> latterColumn = new ArrayList<>();

            // データの方向を転置するための処理
            lines.map(s -> parser.parse(s))
                    .flatMap(Optional::stream)
                    .forEach(d -> {
                        formerColumn.add(d[0]);
                        latterColumn.add(d[1]);
                    });
            return new double[][] {
                    formerColumn.stream().mapToDouble(Double::doubleValue).toArray(),
                    latterColumn.stream().mapToDouble(Double::doubleValue).toArray()
            };
        } catch (NumberFormatException e) {
            throw new IOException("illegal format: " + e.getMessage());
        }
    }
}
