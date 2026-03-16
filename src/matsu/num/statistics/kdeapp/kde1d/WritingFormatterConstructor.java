/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.10
 */
package matsu.num.statistics.kdeapp.kde1d;

import static matsu.num.statistics.kdeapp.kde1d.Commands.*;

import matsu.num.statistics.kdeapp.command.ComponentConstructor;
import matsu.num.statistics.kdeapp.command.ConsoleParameters;
import matsu.num.statistics.kdeapp.format.Separator;
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
final class WritingFormatterConstructor implements ComponentConstructor<WritingFormatter> {

    /**
     * 唯一のコンストラクタ.
     */
    WritingFormatterConstructor() {
    }

    /**
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public WritingFormatter apply(ConsoleParameters interpreter) {

        var builder = new XyTypeFormatterBuilder(
                Separator.from("\t"));

        interpreter.valueOf(
                OUTPUT_LABEL_PREFIX)
                .ifPresent(header -> builder.enableLabel(header));

        interpreter.valueOf(OUTPUT_SEPARATOR)
                .ifPresent(separator -> builder.setSeparator(separator));

        return builder.build();
    }
}
