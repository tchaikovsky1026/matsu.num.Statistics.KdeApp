/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.5
 */
package matsu.num.statistics.kdeapp.kde1d;

import static matsu.num.statistics.kdeapp.kde1d.command.ArgumentRequiringCommand.*;

import matsu.num.statistics.kdeapp.kde1d.command.ConsoleParameterInterpreter;

/**
 * {@link WritingFormatter} の構築器.
 * 
 * @author Matsuura Y.
 */
final class WritingFormatterConstructor implements ComponentConstructor<WritingFormatter> {

    static final WritingFormatterConstructor INSTANCE = new WritingFormatterConstructor();

    /**
     * 非公開のコンストラクタ.
     */
    private WritingFormatterConstructor() {
    }

    /**
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public WritingFormatter construct(ConsoleParameterInterpreter interpreter) {

        WritingFormatter.Builder builder = new WritingFormatter.Builder()
                .disableLabel()
                .setSeparator('\t');

        interpreter.valueOf(LABEL_HEADER)
                .ifPresent(header -> builder.enableLabel(header));

        interpreter.valueOf(SEPARATOR)
                .ifPresent(separator -> builder.setSeparator(separator.charValue()));

        return builder.build();
    }
}
