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

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Objects;

import matsu.num.statistics.kdeapp.exception.OutputException;

/**
 * 結果のディスプレイ出力を扱う.
 * 
 * @author Matsuura Y.
 */
public abstract class ResultDisplay {

    /**
     * 標準出力によるディスプレイ出力を返す.
     * 
     * @param out System.out
     * @param err System.err
     * @return stdディスプレイ
     * @throws NullPointerException 引数がnullを含む場合
     */
    public static ResultDisplay stdout(PrintStream out, PrintStream err) {
        return new StdOutput(out, err);
    }

    /**
     * null-ディスプレイ出力を返す.
     * 
     * @return null-ディスプレイ
     */
    public static ResultDisplay nullDisplay() {
        return new ResultDisplay() {
            @Override
            public void write(WritableKde1dResult result, WritingFormatter writingFormatter) {
                // 何もしない.
            }
        };
    }

    /**
     * 非公開のコンストラクタ. <br>
     * ネストしたクラスからの継承のみ許可.
     */
    private ResultDisplay() {

    }

    /**
     * 結果を表示させる.
     * 
     * @param result 結果
     * @param writingFormatter フォーマッタ
     * @throws OutputException 例外が発生した場合
     * @throws NullPointerException 引数がnull (スローされない場合がある)
     */
    public abstract void write(WritableKde1dResult result, WritingFormatter writingFormatter);

    /**
     * 標準出力.
     */
    private static final class StdOutput extends ResultDisplay {

        private final PrintStream out;
        @SuppressWarnings("unused")
        private final PrintStream err;

        /**
         * @param out System.out
         * @param err System.err
         * @throws NullPointerException 引数がnullを含む場合
         */
        StdOutput(PrintStream out, PrintStream err) {
            super();
            this.out = Objects.requireNonNull(out);
            this.err = Objects.requireNonNull(err);
        }

        /**
         * @throws OutputException {@inheritDoc }
         * @throws NullPointerException {@inheritDoc }
         */
        @Override
        public void write(WritableKde1dResult result, WritingFormatter writingFormatter) {
            if (result.write(new PrintWriter(out), writingFormatter)) {
                throw new OutputException("System.out");
            }
        }
    }
}
