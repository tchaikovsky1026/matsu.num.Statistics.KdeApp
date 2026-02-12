/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.10
 */
package matsu.num.statistics.kdeapp.kde1d;

import static matsu.num.statistics.kdeapp.kde1d.command.ArgumentRequiringCommand.*;

import matsu.num.statistics.kdeapp.kde1d.command.ConsoleParameterInterpreter;

/**
 * {@link OutputTarget} の構築器.
 * 
 * @author Matsuura Y.
 */
final class OutputTargetsConstructor implements ComponentConstructor<OutputTarget> {

    static final OutputTargetsConstructor INSTANCE = new OutputTargetsConstructor();

    private OutputTargetsConstructor() {
    }

    /**
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public OutputTarget construct(ConsoleParameterInterpreter interpreter) {

        /*
         * OUTPUT_FORCE -> forceOutput
         * OUTPUT -> regularOutput
         * empty -> nullOutput
         */
        
        return interpreter.valueOf(OUTPUT_FORCE_FILE_PATH)
                .map(OutputTarget::forceOutput)
                .orElse(interpreter.valueOf(OUTPUT_FILE_PATH)
                        .map(OutputTarget::regularOutput)
                        .orElse(OutputTarget.nullOutput()));
    }
}
