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
public final class XyzTypeFormatterBuilder {

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
    public XyzTypeFormatterBuilder(Separator separator) {
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
    public XyzTypeFormatterBuilder setSeparator(Separator separator) {
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
    public XyzTypeFormatterBuilder enableLabel(char labelPrefix) {
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
    public XyzTypeFormatterBuilder enableLabel(String labelPrefix) {
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
    public XyzTypeFormatterBuilder disableLabel() {
        this.labelPrefix = null;
        return this;
    }

    /**
     * フォーマッターをビルドする.
     * 
     * @return フォーマッター
     */
    public WritingFormatter build() {
        return createFormatter(separator, labelPrefix);
    }

    /**
     * フォーマッターを生成するstaticメソッド.
     * build メソッドから呼ばれることを想定.
     */
    private static WritingFormatter createFormatter(Separator separator, String labelPrefix) {
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
