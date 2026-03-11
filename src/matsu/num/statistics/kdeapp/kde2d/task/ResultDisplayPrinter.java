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

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Objects;

import matsu.num.statistics.kdeapp.exception.OutputException;

/**
 * 結果のディスプレイ出力を扱う.
 * 
 * @author Matsuura Y.
 */
public final class ResultDisplayPrinter implements ResultWriter {

    private final PrintStream out;
    @SuppressWarnings("unused")
    private final PrintStream err;

    /**
     * 標準出力を構築するコンストラクタ.
     * 
     * <p>
     * 引数には, {@code System.out}, {@code System.err} を渡す.
     * </p>
     * 
     * @param out System.out
     * @param err System.err
     * @throws NullPointerException 引数がnullを含む場合
     */
    public ResultDisplayPrinter(PrintStream out, PrintStream err) {
        super();
        this.out = Objects.requireNonNull(out);
        this.err = Objects.requireNonNull(err);
    }

    /**
     * @throws OutputException {@inheritDoc }
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public void write(WritableKde2dResult result, WritingFormatter writingFormatter) {
        if (result.write(new PrintWriter(out), writingFormatter)) {
            throw new OutputException("System.out");
        }
    }
}
