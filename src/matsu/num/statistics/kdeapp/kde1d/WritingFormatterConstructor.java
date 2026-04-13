/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.29
 */
package matsu.num.statistics.kdeapp.kde1d;

import static matsu.num.statistics.kdeapp.kde1d.Resolvers.*;

import java.util.NoSuchElementException;

import matsu.num.statistics.kdeapp.comp.ResolverContainer;
import matsu.num.statistics.kdeapp.exception.ProgrammingBugException;
import matsu.num.statistics.kdeapp.kde1d.task.WritingFormatter;
import matsu.num.statistics.kdeapp.kde1d.task.XyTypeFormatterBuilder;

/**
 * {@link WritingFormatterBk} の構築器.
 * 
 * <p>
 * 出力フォーマットは, 2 columns である. <br>
 * デフォルトは区切り文字が tab だが, オプションで変更可能. <br>
 * デフォルトはラベル無しだが, オプションで変更可能.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class WritingFormatterConstructor {

    /**
     * 唯一のコンストラクタ.
     */
    WritingFormatterConstructor() {
    }

    /**
     * @throws NullPointerException {@inheritDoc }
     */
    WritingFormatter apply(ResolverContainer property) {
        try {
            var builder = new XyTypeFormatterBuilder(property.get(OUTPUT_SEPARATOR));
            property.get(OUTPUT_LABEL_PREFIX_SETTING).accept(builder);
            return builder.build();
        } catch (NoSuchElementException e) {
            throw new ProgrammingBugException(e.getMessage());
        }
    }
}
