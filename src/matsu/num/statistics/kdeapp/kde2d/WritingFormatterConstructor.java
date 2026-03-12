/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.12
 */
package matsu.num.statistics.kdeapp.kde2d;

import static matsu.num.statistics.kdeapp.kde2d.Commands.*;

import matsu.num.statistics.kdeapp.command.ComponentConstructor;
import matsu.num.statistics.kdeapp.command.ConsoleParameters;
import matsu.num.statistics.kdeapp.exception.ProgrammingBugException;
import matsu.num.statistics.kdeapp.format.Separator;
import matsu.num.statistics.kdeapp.kde2d.task.MatrixTypeFormatterBuilder;
import matsu.num.statistics.kdeapp.kde2d.task.OutputFormatType;
import matsu.num.statistics.kdeapp.kde2d.task.WritingFormatter;
import matsu.num.statistics.kdeapp.kde2d.task.WritingFormatter.Builder;
import matsu.num.statistics.kdeapp.kde2d.task.XyzBlockTypeFormatterBuilder;
import matsu.num.statistics.kdeapp.kde2d.task.XyzTypeFormatterBuilder;

/**
 * {@link WritingFormatter} の構築器.
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

        OutputFormatType formatType = interpreter.valueOf(OUTPUT_FORMAT_TYPE)
                .orElse(OutputFormatType.XYZ);

        Builder<? extends Builder<?>> builder;

        switch (formatType) {
            case XYZ: {
                builder = new XyzTypeFormatterBuilder(Separator.from("\t"));
                break;
            }
            case XYZ_BLOCK: {
                builder = new XyzBlockTypeFormatterBuilder(Separator.from("\t"))
                        .setBlankGap(1);
                break;
            }
            case MATRIX: {
                builder = new MatrixTypeFormatterBuilder(Separator.from("\t"));
                break;
            }
            default:
                throw new ProgrammingBugException("Incomplete switch case.");
        }

        interpreter.valueOf(LABEL_PREFIX)
                .ifPresent(header -> builder.enableLabel(header));
        interpreter.valueOf(SEPARATOR_OUTPUT)
                .ifPresent(separator -> builder.setSeparator(separator));
        return builder.build();
    }
}
