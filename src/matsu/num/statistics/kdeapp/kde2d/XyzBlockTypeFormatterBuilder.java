/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.7
 */
package matsu.num.statistics.kdeapp.kde2d;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import matsu.num.statistics.kdeapp.base.IterableFlattening;
import matsu.num.statistics.kdeapp.format.Separator;
import matsu.num.statistics.kdeapp.kde2d.format.BlockFlattening;
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
final class XyzBlockTypeFormatterBuilder {

    private volatile Separator separator;
    private volatile int blankGap;
    private volatile String labelPrefix;

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
    XyzBlockTypeFormatterBuilder(Separator separator) {
        this.separator = Objects.requireNonNull(separator);
        blankGap = 1;
        labelPrefix = null;
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
    XyzBlockTypeFormatterBuilder setSeparator(Separator separator) {
        this.separator = Objects.requireNonNull(separator);
        return this;
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
    XyzBlockTypeFormatterBuilder setBlankGap(int blankGap) {
        if (blankGap < 0) {
            throw new IllegalArgumentException("illegal blank gap: " + blankGap);
        }
        this.blankGap = blankGap;
        return this;
    }

    /**
     * ラベル出力に関し, 出力する設定に変更する
     * (ラベルの先頭に付与する文字を与える).
     * 
     * <p>
     * <i>
     * {@code this}
     * をリターンするので注意.
     * </i>
     * </p>
     * 
     * @param labelPrefix ラベルの先頭に付与する文字
     * @return {@code this}
     */
    XyzBlockTypeFormatterBuilder enableLabel(char labelPrefix) {
        return this.enableLabel(String.valueOf(labelPrefix));
    }

    /**
     * ラベル出力に関し, 出力する設定に変更する
     * (ラベルの先頭に付与する文字列を与える).
     * 
     * <p>
     * <i>
     * {@code this}
     * をリターンするので注意.
     * </i>
     * </p>
     * 
     * @param labelPrefix ラベルの先頭に付与する文字列
     * @return {@code this}
     * @throws NullPointerException 引数がnullの場合
     */
    XyzBlockTypeFormatterBuilder enableLabel(String labelPrefix) {
        this.labelPrefix = Objects.requireNonNull(labelPrefix);
        return this;
    }

    /**
     * {@code this} のラベル出力に関し, 出力しない設定に変更する.
     * 
     * <p>
     * <i>
     * {@code this}
     * をリターンするので注意.
     * </i>
     * </p>
     * 
     * @return {@code this}
     */
    XyzBlockTypeFormatterBuilder disableLabel() {
        this.labelPrefix = null;
        return this;
    }

    /**
     * フォーマッターをビルドする.
     * 
     * @return フォーマッター
     */
    WritingFormatter build() {
        return createFormatter(separator, blankGap, labelPrefix);
    }

    /**
     * フォーマッターを生成するstaticメソッド.
     * build メソッドから呼ばれることを想定.
     * 
     * <p>
     * labelPrefix が null の場合はラベル出力しない.
     * </p>
     */
    private static WritingFormatter createFormatter(Separator separator, int blankGap, String labelPrefix) {
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
