/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.9
 */
package matsu.num.statistics.kdeapp.kde1d;

import java.util.Objects;

import matsu.num.statistics.kdeapp.format.Separator;
import matsu.num.statistics.kerneldensity.output.FormattableKdeResult1D;
import matsu.num.statistics.kerneldensity.output.Kde1dCharSVTextFormatter;

/**
 * XY 型 (1行が1値を表す縦持ち形式) フォーマッターのミュータブルなビルダ.
 * 
 * @author Matsuura Y.
 */
final class XyTypeFormatterBuilder {

    private volatile Separator separator;
    private volatile String labelPrefix;

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
    XyTypeFormatterBuilder(Separator separator) {
        this.separator = Objects.requireNonNull(separator);
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
    XyTypeFormatterBuilder setSeparator(Separator separator) {
        this.separator = Objects.requireNonNull(separator);
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
    XyTypeFormatterBuilder enableLabel(char labelPrefix) {
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
     * @param labelPrefix ラベルの先頭に付与する文字
     * @return {@code this}
     * @throws NullPointerException 引数がnullの場合
     */
    XyTypeFormatterBuilder enableLabel(String labelPrefix) {
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
    XyTypeFormatterBuilder disableLabel() {
        this.labelPrefix = null;
        return this;
    }

    /**
     * フォーマッターをビルドする.
     * 
     * @return フォーマッター
     */
    WritingFormatter build() {
        Kde1dCharSVTextFormatter innerFormatter = Objects.isNull(labelPrefix)
                ? Kde1dCharSVTextFormatter.labelless(separator.charValue())
                : Kde1dCharSVTextFormatter.withLabelEscaped(separator.charValue(), labelPrefix);

        return new WritingFormatter() {
            @Override
            public Iterable<String> format(FormattableKdeResult1D kde1dResult) {
                return kde1dResult.formatted(innerFormatter);
            }
        };
    }
}
