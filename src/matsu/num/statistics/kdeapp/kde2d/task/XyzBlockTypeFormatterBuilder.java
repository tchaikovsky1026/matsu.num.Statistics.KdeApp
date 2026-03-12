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

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import matsu.num.statistics.kdeapp.base.IterableFlattening;
import matsu.num.statistics.kdeapp.format.Separator;
import matsu.num.statistics.kerneldensity.output.FormattableKdeResult2D;
import matsu.num.statistics.kerneldensity.output.Kde2dCharSVTextFormatter;
import matsu.num.statistics.kerneldensity.output.Kde2dFormatter;
import matsu.num.statistics.kerneldensity.output.StructuredCharSVTextFormatter;

/**
 * XYZ-block 型 (1行が1値を表す縦持ち形式, かつメジャー値 (x値) のまとまりでブロック構造をとる)
 * フォーマッターのミュータブルなビルダ.
 * 
 * @author Matsuura Y.
 */
public final class XyzBlockTypeFormatterBuilder
        extends WritingFormatter.Builder<XyzBlockTypeFormatterBuilder> {

    private volatile int blankGap;

    /**
     * 区切り文字を与えて, ビルダインスタンスを立ち上げる.
     * 
     * <p>
     * デフォルトは, blankGap = 1,
     * ラベル無しである.
     * </p>
     * 
     * @param separator 区切り文字
     * @throws NullPointerException 引数がnullの場合
     */
    public XyzBlockTypeFormatterBuilder(Separator separator) {
        super(separator);
        blankGap = 1;
    }

    /**
     * ブランク行数に引数の値を用いるように変更する.
     * 
     * <p>
     * <i>
     * {@code this}
     * をリターンするので注意.
     * </i>
     * </p>
     * 
     * @param blankGap ブランク行数
     * @return {@code this}
     * @throws IllegalArgumentException ブランク行数が負の場合
     */
    public XyzBlockTypeFormatterBuilder setBlankGap(int blankGap) {
        if (blankGap < 0) {
            throw new IllegalArgumentException("illegal blank gap: " + blankGap);
        }
        this.blankGap = blankGap;
        return this.self();
    }

    @Override
    protected XyzBlockTypeFormatterBuilder self() {
        return this;
    }

    @Override
    protected WritingFormatter buildConcrete(Separator separator, String labelPrefix) {
        Kde2dFormatter<Iterable<Iterable<String>>> innerFormatter =
                StructuredCharSVTextFormatter.of(separator.charValue());
        BlockFlattening flattening = new BlockFlattening(blankGap);

        // ラベル行を作成するための関数
        /*
         * Kde2dCharSVTextFormatter を使い, 1行目だけを出力することでラベル行を取り出す.
         */
        Function<FormattableKdeResult2D, Iterable<String>> labelStringCreator =
                Objects.isNull(labelPrefix)
                        ? result -> Collections.emptyList()
                        : result -> {
                            String label = result.formatted(
                                    Kde2dCharSVTextFormatter.withLabelEscaped(
                                            separator.charValue(), labelPrefix))
                                    .iterator().next();
                            return List.of(label);
                        };

        return new WritingFormatter() {
            @Override
            public Iterable<String> format(FormattableKdeResult2D kde2dResult) {

                return IterableFlattening.flatten(
                        List.of(
                                labelStringCreator.apply(kde2dResult),
                                flattening.apply(kde2dResult.formatted(innerFormatter))));
            }
        };
    }
}
