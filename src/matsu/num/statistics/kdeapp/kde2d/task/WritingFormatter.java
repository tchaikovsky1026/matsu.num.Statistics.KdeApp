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
}
