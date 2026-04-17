/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.4.18
 */
package matsu.num.statistics.kdeapp.kde1d;

import java.io.PrintStream;
import java.util.Objects;

import matsu.num.statistics.kdeapp.comp.ResolverContainer;
import matsu.num.statistics.kdeapp.kde1d.task.ResultDisplayPrinter;
import matsu.num.statistics.kdeapp.kde1d.task.ResultWriter;

/**
 * {@link ResultDisplayPrinter} の構築器.
 * 
 * @author Matsuura Y.
 */
final class PrinterConstructor {

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
    ResultWriter apply(ResolverContainer property) {
        return property.require(Resolvers.ECHO).get(out, err);
    }
}
