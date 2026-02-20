/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.20
 */
package matsu.num.statistics.kdeapp.kde2d;

import java.util.Objects;

import matsu.num.statistics.kdeapp.format.Separator;
import matsu.num.statistics.kerneldensity.output.FormattableKdeResult2D;
import matsu.num.statistics.kerneldensity.output.Kde2dCharSVTextFormatter;

/**
 * kde2dの結果出力のフォーマッターを扱う.
 * 
 * @author Matsuura Y.
 */
final class WritingFormatter {

    private final Separator separator;
    private final String labelPrefix;

    private final Kde2dCharSVTextFormatter formatter;

    /**
     * ビルダから呼ばれる.
     */
    private WritingFormatter(Builder builder) {
        super();

        this.separator = builder.separator;
        this.labelPrefix = builder.labelPrefix;

        this.formatter = createFormatter();
    }

    /**
     * この書き込みパラメータからフォーマッターを構築する.
     */
    private Kde2dCharSVTextFormatter createFormatter() {
        return Objects.isNull(labelPrefix)
                ? Kde2dCharSVTextFormatter.labelless(separator.charValue())
                : Kde2dCharSVTextFormatter.withLabelEscaped(separator.charValue(), labelPrefix);
    }

    /**
     * kde2dの計算結果をフォーマットして文字列の形で返す.
     * 
     * @param kde2dResult 計算結果
     * @return 文字列変換後
     */
    Iterable<String> format(FormattableKdeResult2D kde2dResult) {
        return kde2dResult.formatted(formatter);
    }

    /**
     * フォーマッターのミュータブルなビルダ.
     */
    static final class Builder {

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
        Builder(Separator separator) {
            this.separator = Objects.requireNonNull(separator);
            labelPrefix = null;
        }

        /**
         * コピーコンストラクタ.
         * 
         * @throws NullPointerException 引数がnullの場合
         */
        Builder(Builder src) {
            this.separator = src.separator;
            this.labelPrefix = src.labelPrefix;
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
        Builder setSeparator(Separator separator) {
            this.separator = separator;
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
        Builder enableLabel(char labelPrefix) {
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
        Builder enableLabel(String labelPrefix) {
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
        Builder disableLabel() {
            this.labelPrefix = null;
            return this;
        }

        /**
         * フォーマッターをビルドする.
         * 
         * @return フォーマッター
         */
        WritingFormatter build() {
            return new WritingFormatter(this);
        }
    }
}
