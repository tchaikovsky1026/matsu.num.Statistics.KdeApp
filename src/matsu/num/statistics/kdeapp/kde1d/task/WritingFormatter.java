/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.10
 */
package matsu.num.statistics.kdeapp.kde1d.task;

import matsu.num.statistics.kerneldensity.output.FormattableKdeResult1D;

/**
 * kde1dの結果出力のフォーマッターを扱う.
 * 
 * @author Matsuura Y.
 */
public interface WritingFormatter {

    /**
     * kde1dの計算結果をフォーマットして文字列の形で返す.
     * 
     * <p>
     * {@link WritableKde1dResult} から呼ばれることを想定している. <br>
     * 引数は {@code null} でないことが保証されている.
     * </p>
     * 
     * @param kde1dResult 計算結果
     * @return 文字列変換後
     */
    public abstract Iterable<String> format(FormattableKdeResult1D kde1dResult);
}
