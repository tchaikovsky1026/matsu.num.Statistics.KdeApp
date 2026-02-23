/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.23
 */
package matsu.num.statistics.kdeapp.kde2d;

import static matsu.num.statistics.kdeapp.kde2d.Commands.*;

import matsu.num.statistics.kdeapp.command.ComponentConstructor;
import matsu.num.statistics.kdeapp.command.ConsoleParameters;

/**
 * {@link ResultOutput} の構築器.
 * 
 * @author Matsuura Y.
 */
final class ResultOutputConstructor implements ComponentConstructor<ResultOutput> {

    /**
     * 唯一のコンストラクタ.
     */
    ResultOutputConstructor() {
    }

    /**
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public ResultOutput apply(ConsoleParameters interpreter) {

        /*
         * OUTPUT_FORCE -> forceOutput
         * OUTPUT -> regularOutput
         * empty -> nullOutput
         */

        return interpreter.valueOf(OUTPUT_FORCE_FILE_PATH)
                .map(ResultOutput::forceOutput)
                .orElse(
                        interpreter.valueOf(OUTPUT_FILE_PATH)
                                .map(ResultOutput::regularOutput)
                                .orElse(ResultOutput.nullOutput()));
    }
}
