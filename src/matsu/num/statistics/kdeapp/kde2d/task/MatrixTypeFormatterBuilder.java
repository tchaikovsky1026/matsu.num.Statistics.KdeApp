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

import matsu.num.statistics.kdeapp.format.Separator;
import matsu.num.statistics.kerneldensity.output.FormattableKdeResult2D;
import matsu.num.statistics.kerneldensity.output.Kde2dFormatter;
import matsu.num.statistics.kerneldensity.output.MatrixCharSVTextFormatter;

/**
 * Matrix 型 (行列形式) フォーマッターのミュータブルなビルダ.
 * 
 * @author Matsuura Y.
 */
public final class MatrixTypeFormatterBuilder
        extends WritingFormatter.Builder<MatrixTypeFormatterBuilder> {

    /**
     * 区切り文字を与えて, ビルダインスタンスを立ち上げる.
     * 
     * @param separator 区切り文字
     * @throws NullPointerException 引数がnullの場合
     */
    public MatrixTypeFormatterBuilder(Separator separator) {
        super(separator);
    }

    @Override
    protected MatrixTypeFormatterBuilder self() {
        return this;
    }

    @Override
    protected WritingFormatter buildConcrete(Separator separator, String labelPrefix) {
        Kde2dFormatter<Iterable<String>> innerFormatter =
                MatrixCharSVTextFormatter.of(separator.charValue());

        return new WritingFormatter() {
            @Override
            public Iterable<String> format(FormattableKdeResult2D kde2dResult) {
                return kde2dResult.formatted(innerFormatter);
            }
        };
    }
}
