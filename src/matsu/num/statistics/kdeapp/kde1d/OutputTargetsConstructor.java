/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.6
 */
package matsu.num.statistics.kdeapp.kde1d;

import static matsu.num.statistics.kdeapp.kde1d.command.ArgumentRequiringCommand.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import matsu.num.statistics.kdeapp.kde1d.command.ConsoleParameterInterpreter;

/**
 * {@link OutputTargets} の構築器.
 * 
 * @author Matsuura Y.
 */
final class OutputTargetsConstructor implements ComponentConstructor<OutputTargets> {

    private final PrintStream out;
    private final PrintStream err;

    /**
     * 唯一のコンストラクタ.
     * 標準入出力を与える.
     */
    OutputTargetsConstructor(PrintStream out, PrintStream err) {
        this.out = Objects.requireNonNull(out);
        this.err = Objects.requireNonNull(err);
    }

    /**
     * @throws InvalidParameterException {@inheritDoc }
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public OutputTargets construct(ConsoleParameterInterpreter interpreter) {
        List<String> filePaths = new ArrayList<>();
        interpreter.valueOf(OUTPUT_FORCE_FILE_PATH)
                .ifPresent(filePaths::add);

        return new OutputTargets(out, err, filePaths);
    }
}
