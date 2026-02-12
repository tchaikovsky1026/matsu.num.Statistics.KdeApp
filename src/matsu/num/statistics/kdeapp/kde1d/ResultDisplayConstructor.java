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

import static matsu.num.statistics.kdeapp.kde1d.ResultDisplay.*;

import java.io.PrintStream;
import java.util.Objects;

import matsu.num.statistics.kdeapp.kde1d.command.ConsoleParameterInterpreter;
import matsu.num.statistics.kdeapp.kde1d.command.NoArgumentCommand;

/**
 * {@link ResultDisplay} の構築器.
 * 
 * @author Matsuura Y.
 */
final class ResultDisplayConstructor implements ComponentConstructor<ResultDisplay> {

    private final PrintStream out;
    private final PrintStream err;

    /**
     * 唯一のコンストラクタ.
     * 
     * @param out System.out
     * @param err System.err
     * @throws NullPointerException 引数がnull
     */
    ResultDisplayConstructor(PrintStream out, PrintStream err) {
        super();
        this.out = Objects.requireNonNull(out);
        this.err = Objects.requireNonNull(err);
    }

    /**
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public ResultDisplay construct(ConsoleParameterInterpreter interpreter) {

        return interpreter.contains(NoArgumentCommand.ECHO_OFF)
                ? nullDisplay()
                : stdout(out, err);
    }
}
