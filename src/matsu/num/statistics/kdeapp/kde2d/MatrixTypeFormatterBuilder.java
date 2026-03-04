/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.3
 */
package matsu.num.statistics.kdeapp.kde2d;

import java.util.Objects;

import matsu.num.statistics.kdeapp.format.Separator;
import matsu.num.statistics.kerneldensity.output.FormattableKdeResult2D;
import matsu.num.statistics.kerneldensity.output.Kde2dFormatter;
import matsu.num.statistics.kerneldensity.output.MatrixCharSVTextFormatter;

/**
 * Matrix 型 (行列形式) フォーマッターのミュータブルなビルダ.
 * 
 * @author Matsuura Y.
 */
final class MatrixTypeFormatterBuilder {
    private volatile Separator separator;

    /**
     * 区切り文字を与えて, ビルダインスタンスを立ち上げる.
     * 
     * @param separator 区切り文字
     * @throws NullPointerException 引数がnullの場合
     */
    MatrixTypeFormatterBuilder(Separator separator) {
        this.separator = Objects.requireNonNull(separator);
    }

    /**
     * 区切り文字に引数の値を用いるように変更する.
     * 
     * <p>
     * <i>
     * {@code this}
     * をリターンするので注意.
     * </i>
     * </p>
     * 
     * @param separator 区切り文字
     * @return {@code this}
     * @throws NullPointerException 引数がnullの場合
     */
    MatrixTypeFormatterBuilder setSeparator(Separator separator) {
        this.separator = Objects.requireNonNull(separator);
        return this;
    }

    /**
     * フォーマッターをビルドする.
     * 
     * @return フォーマッター
     */
    WritingFormatter build() {
        return createFormatter(separator);
    }

    /**
     * フォーマッターを生成するstaticメソッド.
     * build メソッドから呼ばれることを想定.
     */
    private static WritingFormatter createFormatter(Separator separator) {
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
