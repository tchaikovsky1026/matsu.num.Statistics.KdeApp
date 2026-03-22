/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.22
 */
package matsu.num.statistics.kdeapp.kde2d;

import static matsu.num.statistics.kdeapp.kde2d.Commands.*;

import matsu.num.statistics.kdeapp.command.ComponentConstructor;
import matsu.num.statistics.kdeapp.command.ConsoleParameters;
import matsu.num.statistics.kdeapp.kde2d.task.WritingFormatter;
import matsu.num.statistics.kdeapp.kde2d.task.WritingFormatter.Builder;

/**
 * {@link WritingFormatter} の構築器.
 * 
 * @author Matsuura Y.
 */
final class FormatterConstructor implements ComponentConstructor<WritingFormatter> {

    /**
     * 唯一のコンストラクタ.
     */
    FormatterConstructor() {
    }

    /**
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public WritingFormatter apply(ConsoleParameters interpreter) {
        Builder<? extends Builder<?>> builder = interpreter.valueOf(OUTPUT_FORMAT_TYPE)
                .orElse(FormatterBuilderSupplier.XYZ)
                .createBuilder();

        // OUTPUT_NO_LABEL はチェックしていない
        interpreter.valueOf(OUTPUT_LABEL_PREFIX)
                .ifPresent(header -> builder.enableLabel(header));
        interpreter.valueOf(OUTPUT_SEPARATOR)
                .ifPresent(separator -> builder.setSeparator(separator));
        return builder.build();
    }
}
