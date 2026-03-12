/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.12
 */
package matsu.num.statistics.kdeapp.kde2d;

import static matsu.num.statistics.kdeapp.kde2d.Commands.*;

import java.io.PrintStream;
import java.util.Objects;

import matsu.num.statistics.kdeapp.command.ComponentConstructor;
import matsu.num.statistics.kdeapp.command.ConsoleParameters;
import matsu.num.statistics.kdeapp.kde2d.task.ResultDisplayPrinter;
import matsu.num.statistics.kdeapp.kde2d.task.ResultWriter;

/**
 * {@link ResultDisplayPrinter} の構築器.
 * 
 * @author Matsuura Y.
 */
final class PrinterConstructor implements ComponentConstructor<ResultWriter> {

    private final PrintStream out;
    private final PrintStream err;

    /**
     * 唯一のコンストラクタ.
     * 
     * @param out System.out
     * @param err System.err
     * @throws NullPointerException 引数がnull
     */
    PrinterConstructor(PrintStream out, PrintStream err) {
        super();
        this.out = Objects.requireNonNull(out);
        this.err = Objects.requireNonNull(err);
    }

    /**
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public ResultWriter apply(ConsoleParameters interpreter) {
        ResultWriter writer = ResultWriter.nullWriter();

        if (!interpreter.contains(ECHO_OFF)) {
            writer = writer.andThen(new ResultDisplayPrinter(out, err));
        }

        return writer;
    }
}
