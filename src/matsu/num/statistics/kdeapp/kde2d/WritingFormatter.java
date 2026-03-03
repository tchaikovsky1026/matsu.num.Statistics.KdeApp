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
import matsu.num.statistics.kerneldensity.output.Kde2dCharSVTextFormatter;
import matsu.num.statistics.kerneldensity.output.Kde2dFormatter;
import matsu.num.statistics.kerneldensity.output.MatrixCharSVTextFormatter;

/**
 * kde2dの結果出力のフォーマッターを扱う.
 * 
 * @author Matsuura Y.
 */
final class WritingFormatter {

    private final Kde2dFormatter<Iterable<String>> formatter;

    /**
     * ビルダから呼ばれる.
     */
    private WritingFormatter(Kde2dFormatter<Iterable<String>> formatter) {
        super();
        this.formatter = formatter;
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
     * Long 型 (1行が1値を表す縦持ち形式) フォーマッターのミュータブルなビルダ.
     */
    static final class LongTypeBuilder {

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
        LongTypeBuilder(Separator separator) {
            this.separator = Objects.requireNonNull(separator);
            labelPrefix = null;
        }

        /**
         * コピーコンストラクタ.
         * 
         * @throws NullPointerException 引数がnullの場合
         */
        LongTypeBuilder(LongTypeBuilder src) {
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
        LongTypeBuilder setSeparator(Separator separator) {
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
        LongTypeBuilder enableLabel(char labelPrefix) {
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
        LongTypeBuilder enableLabel(String labelPrefix) {
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
        LongTypeBuilder disableLabel() {
            this.labelPrefix = null;
            return this;
        }

        /**
         * フォーマッターをビルドする.
         * 
         * @return フォーマッター
         */
        WritingFormatter build() {
            return new WritingFormatter(createFormatter());
        }

        /**
         * この書き込みパラメータからフォーマッターを構築する.
         */
        private Kde2dCharSVTextFormatter createFormatter() {
            return Objects.isNull(labelPrefix)
                    ? Kde2dCharSVTextFormatter.labelless(separator.charValue())
                    : Kde2dCharSVTextFormatter.withLabelEscaped(separator.charValue(), labelPrefix);
        }
    }

    /**
     * Matrix 型 (行列形式) フォーマッターのミュータブルなビルダ.
     */
    static final class MatrixTypeBuilder {

        private volatile Separator separator;

        /**
         * 区切り文字を与えて, ビルダインスタンスを立ち上げる.
         * 
         * @param separator 区切り文字
         * @throws NullPointerException 引数がnullの場合
         */
        MatrixTypeBuilder(Separator separator) {
            this.separator = Objects.requireNonNull(separator);
        }

        /**
         * コピーコンストラクタ.
         * 
         * @throws NullPointerException 引数がnullの場合
         */
        MatrixTypeBuilder(MatrixTypeBuilder src) {
            this.separator = src.separator;
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
        MatrixTypeBuilder setSeparator(Separator separator) {
            this.separator = Objects.requireNonNull(separator);
            return this;
        }

        /**
         * フォーマッターをビルドする.
         * 
         * @return フォーマッター
         */
        WritingFormatter build() {
            return new WritingFormatter(MatrixCharSVTextFormatter.of(separator.charValue()));
        }
    }
}
