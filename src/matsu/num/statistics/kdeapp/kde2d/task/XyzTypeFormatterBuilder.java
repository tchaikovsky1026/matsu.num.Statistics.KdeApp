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

import java.util.Objects;

import matsu.num.statistics.kdeapp.format.Separator;
import matsu.num.statistics.kerneldensity.output.FormattableKdeResult2D;
import matsu.num.statistics.kerneldensity.output.Kde2dCharSVTextFormatter;
import matsu.num.statistics.kerneldensity.output.Kde2dFormatter;

/**
 * XYZ 型 (1行が1値を表す縦持ち形式) フォーマッターのミュータブルなビルダ.
 * 
 * @author Matsuura Y.
 */
public final class XyzTypeFormatterBuilder
        extends WritingFormatter.Builder<XyzTypeFormatterBuilder> {

    /**
     * 区切り文字を与えて, ビルダインスタンスを立ち上げる.
     * 
     * <p>
     * デフォルトは, ラベル無しである.
     * </p>
     * 
     * @param separator 区切り文字
     * @throws NullPointerException 引数がnullの場合
     */
    public XyzTypeFormatterBuilder(Separator separator) {
        super(separator);
    }

    @Override
    protected XyzTypeFormatterBuilder self() {
        return this;
    }

    @Override
    protected WritingFormatter buildConcrete(Separator separator, String labelPrefix) {
        Kde2dFormatter<Iterable<String>> innerFormatter =
                Objects.isNull(labelPrefix)
                        ? Kde2dCharSVTextFormatter.labelless(separator.charValue())
                        : Kde2dCharSVTextFormatter.withLabelEscaped(separator.charValue(), labelPrefix);

        return new WritingFormatter() {
            @Override
            public Iterable<String> format(FormattableKdeResult2D kde2dResult) {
                return kde2dResult.formatted(innerFormatter);
            }
        };
    }
}
