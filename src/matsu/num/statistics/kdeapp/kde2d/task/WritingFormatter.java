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

/**
 * kde2dの結果出力のフォーマッターを扱う.
 * 
 * @author Matsuura Y.
 */
public interface WritingFormatter {

    /**
     * kde2dの計算結果をフォーマットして文字列の形で返す.
     * 
     * <p>
     * {@link WritableKde2dResult} から呼ばれることを想定している. <br>
     * 引数は {@code null} でないことが保証されている.
     * </p>
     * 
     * @param kde2dResult 計算結果
     * @return 文字列変換後
     */
    public abstract Iterable<String> format(FormattableKdeResult2D kde2dResult);

    /**
     * フォーマッターのビルダ.
     * 
     * @param <T> 自身の型, 具象クラスの型でバインドする.
     */
    public static abstract class Builder<T extends Builder<T>> {

        private volatile Separator separator;
        private volatile String labelPrefix;

        /**
         * 唯一のコンストラクタ.
         * 
         * @param separator 区切り文字
         * @throws NullPointerException 引数がnullの場合
         */
        protected Builder(Separator separator) {
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
        public final T setSeparator(Separator separator) {
            this.separator = Objects.requireNonNull(separator);
            return this.self();
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
        public final T enableLabel(char labelPrefix) {
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
        public final T enableLabel(String labelPrefix) {
            this.labelPrefix = Objects.requireNonNull(labelPrefix);
            return this.self();
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
        public final T disableLabel() {
            this.labelPrefix = null;
            return this.self();
        }

        /**
         * {@code this} を返す. <br>
         * このクラスと継承先からしか呼んではいけない.
         * 
         * @return this
         */
        protected abstract T self();

        /**
         * フォーマッターをビルドする.
         * 
         * @return フォーマッター
         */
        public final WritingFormatter build() {
            return this.buildConcrete(separator, labelPrefix);
        }

        /**
         * {@link #build()} の具体的処理を扱う.
         * 
         * 
         * @implSpec
         *               ビルドに必要な抽象クラスに定義されたフィールドは, 引数として渡される. <br>
         *               具象独自のフィールドは, 具象クラスのフィールドを直接参照すればよい.
         * 
         * @param separator 区切り文字 (non-null)
         * @param labelPrefix ラベルPrefix (ラベル出力しないなら null が与えられる)
         * @return フォーマッター
         */
        protected abstract WritingFormatter buildConcrete(Separator separator, String labelPrefix);
    }
}
