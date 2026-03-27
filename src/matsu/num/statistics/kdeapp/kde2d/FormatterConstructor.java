/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.24
 */
package matsu.num.statistics.kdeapp.kde2d;

import static matsu.num.statistics.kdeapp.kde2d.Properties.*;

import matsu.num.statistics.kdeapp.comp.ConfigProperty;
import matsu.num.statistics.kdeapp.kde2d.task.WritingFormatter;
import matsu.num.statistics.kdeapp.kde2d.task.WritingFormatter.Builder;

/**
 * {@link WritingFormatter} の構築器.
 * 
 * @author Matsuura Y.
 */
final class FormatterConstructor {

    /**
     * 唯一のコンストラクタ.
     */
    FormatterConstructor() {
    }

    /**
     * @throws NullPointerException {@inheritDoc }
     */
    WritingFormatter apply(ConfigProperty property) {
        Builder<? extends Builder<?>> builder =
                property.get(OUTPUT_FORMAT_TYPE)
                        .createBuilder();

        property.get(OUTPUT_LABEL_PREFIX).accept(builder);
        builder.setSeparator(property.get(OUTPUT_SEPARATOR));
        return builder.build();
    }
}
