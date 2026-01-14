/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.1.13
 */
package matsu.num.statistics.kdeapp.kde1d;

import java.io.IOException;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.stream.Stream;

/**
 * {@code double} 値配列としてのデータを構築するローダー.
 * 
 * @author Matsuura Y.
 */
final class DoubleDataLoader {

    private final DoubleLineParser parser;

    /**
     * 文字列を {@code double} 値に変換するパーサーを与えて,
     * ローダーを構築する.
     * 
     * @param parser パーサー
     * @throws NullPointerException 引数がnull
     */
    DoubleDataLoader(DoubleLineParser parser) {
        super();
        this.parser = Objects.requireNonNull(parser);
    }

    /**
     * 1行ごとの文字列ストリームから, {@code double} 値を解析し,
     * 配列として返す.
     * 
     * <p>
     * 実行には, ストリームのサプライヤ ({@link IOSupplier}) を渡す.
     * </p>
     * 
     * @param linesSupplier supplier
     * @return double[]
     * @throws IOException
     * @throws NullPointerException 引数やストリームの要素にnullを含む場合
     */
    public double[] load(IOSupplier<Stream<String>> linesSupplier) throws IOException {
        try (Stream<String> lines = linesSupplier.get()) {
            return lines
                    .map(s -> parser.parse(s))
                    .flatMapToDouble(OptionalDouble::stream)
                    .toArray();
        } catch (NumberFormatException e) {
            throw new IOException("illegal number format: " + e.getMessage());
        }
    }
}
