/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.29
 */
package matsu.num.statistics.kdeapp.kde2d;

import static matsu.num.statistics.kdeapp.kde2d.Resolvers.*;

import java.util.NoSuchElementException;

import matsu.num.statistics.kdeapp.comp.ResolverContainer;
import matsu.num.statistics.kdeapp.exception.ProgrammingBugException;
import matsu.num.statistics.kdeapp.format.Separator;
import matsu.num.statistics.kdeapp.kde2d.task.WritingFormatter;

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
    WritingFormatter apply(ResolverContainer property) {
        try {
            Separator separator = property.get(OUTPUT_SEPARATOR);
            var builder = property.get(OUTPUT_FORMATTER_TYPE).createBuilder(separator);
            property.get(OUTPUT_LABEL_PREFIX_SETTING).accept(builder);
            return builder.build();
        } catch (NoSuchElementException e) {
            throw new ProgrammingBugException(e.getMessage());
        }
    }
}
