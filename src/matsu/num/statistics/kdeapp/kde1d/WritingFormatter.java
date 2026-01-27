/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.1.25
 */
package matsu.num.statistics.kdeapp.kde1d;

import java.util.Objects;

import matsu.num.statistics.kerneldensity.output.FormattableKdeResult1D;
import matsu.num.statistics.kerneldensity.output.Kde1dCharSVTextFormatter;

/**
 * kde1dの結果出力のフォーマッターを扱う.
 * 
 * @author Matsuura Y.
 */
final class WritingFormatter {

    private final char separator;
    private final String labelHeader;

    private final Kde1dCharSVTextFormatter formatter;

    /**
     * ビルダから呼ばれる.
     */
    private WritingFormatter(Builder builder) {
        super();

        this.separator = builder.separator;
        this.labelHeader = builder.labelHeader;

        this.formatter = createFormatter();
    }

    /**
     * この書き込みパラメータからフォーマッターを構築する.
     */
    private Kde1dCharSVTextFormatter createFormatter() {
        return Objects.isNull(labelHeader)
                ? Kde1dCharSVTextFormatter.labelless(separator)
                : Kde1dCharSVTextFormatter.withLabelEscaped(separator, labelHeader);
    }

    /**
     * kde1dの計算結果をフォーマットして文字列の形で返す.
     * 
     * @param kde1dResult 計算結果
     * @return 文字列変換後
     */
    Iterable<String> format(FormattableKdeResult1D kde1dResult) {
        return kde1dResult.formatted(formatter);
    }

    /**
     * フォーマッターのミュータブルなビルダ.
     */
    static final class Builder {

        private volatile char separator;
        private volatile String labelHeader;

        /**
         * デフォルトの設定でビルダインスタンスを立ち上げる.
         * 
         * <p>
         * デフォルトは, 区切り文字がタブで, ラベル無しである.
         * </p>
         */
        Builder() {
            separator = '\t';
            labelHeader = null;
        }

        /**
         * コピーコンストラクタ.
         * 
         * @throws NullPointerException 引数がnullの場合
         */
        Builder(Builder src) {
            this.separator = src.separator;
            this.labelHeader = src.labelHeader;
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
         */
        Builder setSeparator(char separator) {
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
         * @param labelHeader ラベルの先頭に付与する文字
         * @return {@code this}
         */
        Builder enableLabel(char labelHeader) {
            return this.enableLabel(String.valueOf(labelHeader));
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
         * @param labelHeader ラベルの先頭に付与する文字
         * @return {@code this}
         * @throws NullPointerException 引数がnullの場合
         */
        Builder enableLabel(String labelHeader) {
            this.labelHeader = Objects.requireNonNull(labelHeader);
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
            this.labelHeader = null;
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
