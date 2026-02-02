/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.2
 */
package matsu.num.statistics.kdeapp.kde1d;

import static matsu.num.statistics.kdeapp.kde1d.ConsoleOptionCommand.*;

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
     * @throws InvalidParameterException {@inheritDoc }
     */
    @Override
    public WritingFormatter construct(ConsoleParameterInterpreter interpreter) {

        WritingFormatter.Builder builder = new WritingFormatter.Builder()
                .disableLabel()
                .setSeparator('\t');

        interpreter.valueOf(LABEL_HEADER)
                .ifPresent(opHeader -> builder.enableLabel(opHeader.get()));

        interpreter.valueOf(SEPARATOR)
                .ifPresent(opSeparator -> builder.setSeparator(opSeparator.get().charAt(0)));

        return builder.build();
    }
}
