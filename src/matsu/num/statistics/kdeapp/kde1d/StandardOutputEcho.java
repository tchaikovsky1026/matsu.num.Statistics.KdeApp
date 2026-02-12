/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.12
 */
package matsu.num.statistics.kdeapp.kde1d;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Objects;

import matsu.num.statistics.kdeapp.kde1d.exception.OutputException;

/**
 * 結果の標準出力を扱う.
 * 
 * @author Matsuura Y.
 */
final class StandardOutputEcho {

    private final PrintStream out;
    @SuppressWarnings("unused")
    private final PrintStream err;

    /**
     * @param out System.out
     * @param err System.err
     * @throws NullPointerException 引数がnullを含む場合
     */
    StandardOutputEcho(PrintStream out, PrintStream err) {
        this.out = Objects.requireNonNull(out);
        this.err = Objects.requireNonNull(err);
    }

    /**
     * 書き込みを実行する.
     * 
     * @param result 結果
     * @param writingFormatter フォーマッタ
     * @throws NullPointerException 引数がnull
     */
    void write(WritableKde1dResult result, WritingFormatter writingFormatter) {
        if (result.write(new PrintWriter(out), writingFormatter)) {
            throw new OutputException("System.out");
        }
    }
}
