/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.5
 */
package matsu.num.statistics.kdeapp.kde2d;

import java.util.Objects;

import matsu.num.statistics.kdeapp.format.Separator;
import matsu.num.statistics.kdeapp.kde2d.format.BlockFlattening;
import matsu.num.statistics.kerneldensity.output.FormattableKdeResult2D;
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

    /**
     * 区切り文字を与えて, ビルダインスタンスを立ち上げる.
     * 
     * <p>
     * デフォルトは, blankGap = 1 である.
     * </p>
     * 
     * @param separator 区切り文字
     * @throws NullPointerException 引数がnullの場合
     */
    XyzBlockTypeFormatterBuilder(Separator separator) {
        this.separator = Objects.requireNonNull(separator);
        blankGap = 1;
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
     * フォーマッターをビルドする.
     * 
     * @return フォーマッター
     */
    WritingFormatter build() {
        return createFormatter(separator, blankGap);
    }

    /**
     * フォーマッターを生成するstaticメソッド.
     * build メソッドから呼ばれることを想定.
     */
    private static WritingFormatter createFormatter(Separator separator, int blankGap) {
        Kde2dFormatter<Iterable<Iterable<String>>> innerFormatter =
                StructuredCharSVTextFormatter.of(separator.charValue());
        BlockFlattening flattening = new BlockFlattening(blankGap);

        return new WritingFormatter() {
            @Override
            public Iterable<String> format(FormattableKdeResult2D kde2dResult) {
                return flattening.apply(kde2dResult.formatted(innerFormatter));
            }
        };
    }
}
