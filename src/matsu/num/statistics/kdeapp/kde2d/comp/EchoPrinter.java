/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.29
 */
package matsu.num.statistics.kdeapp.kde2d.comp;

import java.io.PrintStream;
import java.util.function.BiFunction;

import matsu.num.statistics.kdeapp.kde2d.task.ResultDisplayPrinter;
import matsu.num.statistics.kdeapp.kde2d.task.ResultWriter;

/**
 * ディスプレイ出力の ON/OFF に関する Resolver.
 * 
 * @author Matsuura Y.
 */
public enum EchoPrinter {

    /**
     * ディスプレイ出力を行うことを表現する.
     */
    ON((out, err) -> new ResultDisplayPrinter(out, err)),

    /**
     * ディスプレイ出力を行わないことを表現する.
     */
    OFF((out, err) -> ResultWriter.nullWriter());

    private final BiFunction<PrintStream, PrintStream, ResultWriter> getter;

    /**
     * @param getter {@code (out, err) -> ResultWriter}
     */
    private EchoPrinter(BiFunction<PrintStream, PrintStream, ResultWriter> getter) {
        this.getter = getter;
    }

    /**
     * ディスプレイ出力用 {@link ResultWriter} を返す.
     * 
     * @param out System.out
     * @param err System.err
     * @return ResultWriter
     * @throws NullPointerException 引数がnullの場合 (必ずではない)
     */
    public ResultWriter get(PrintStream out, PrintStream err) {
        return getter.apply(out, err);
    }
}
