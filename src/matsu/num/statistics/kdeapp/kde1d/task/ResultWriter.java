/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.11
 */
package matsu.num.statistics.kdeapp.kde1d.task;

import java.util.Objects;

import matsu.num.statistics.kdeapp.exception.OutputException;

/**
 * 結果の出力を扱うインターフェース.
 * 
 * @author Matsuura Y.
 */
public interface ResultWriter {

    /**
     * 結果を出力する.
     * 
     * @param result 結果
     * @param writingFormatter フォーマッタ
     * @throws OutputException 例外が発生した場合
     * @throws NullPointerException 引数がnull (スローされない場合がある)
     */
    public abstract void write(WritableKde1dResult result, WritingFormatter writingFormatter);

    /**
     * 自身の
     * {@link #write(WritableKde1dResult, WritingFormatter)}
     * の次に, 引数の
     * {@link #write(WritableKde1dResult, WritingFormatter)}
     * を実行するような
     * {@link ResultWriter} を返す.
     * 
     * @param after 次に実行するライター
     * @return 結合したライター
     * @throws NullPointerException 引数がnullの場合
     */
    public default ResultWriter andThen(ResultWriter after) {
        Objects.requireNonNull(after);

        // 片方がnull出力の場合は結合しない
        if (after == NullWriter.SINGLETON) {
            return this;
        }
        if (this == NullWriter.SINGLETON) {
            return after;
        }

        final ResultWriter before = this;
        return new ResultWriter() {
            @Override
            public void write(WritableKde1dResult result, WritingFormatter writingFormatter) {
                before.write(result, writingFormatter);
                after.write(result, writingFormatter);
            }
        };
    }

    /**
     * null-出力を返す.
     * 
     * <p>
     * null-出力とは, 何もしない出力のことである.
     * </p>
     * 
     * @return null-出力
     */
    public static ResultWriter nullWriter() {
        return NullWriter.SINGLETON;
    }
}
