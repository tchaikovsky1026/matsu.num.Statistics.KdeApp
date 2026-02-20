/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.18
 */
package matsu.num.statistics.kdeapp.kde2d;

import static matsu.num.statistics.kdeapp.kde2d.Commands.*;

import matsu.num.statistics.kdeapp.command.ConsoleParameters;
import matsu.num.statistics.kdeapp.format.Separator;

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
    public WritingFormatter construct(ConsoleParameters interpreter) {

        WritingFormatter.Builder builder = new WritingFormatter.Builder(
                Separator.from("\t"));

        interpreter.valueOf(LABEL_HEADER)
                .ifPresent(header -> builder.enableLabel(header));

        interpreter.valueOf(SEPARATOR_OUTPUT)
                .ifPresent(separator -> builder.setSeparator(separator));

        return builder.build();
    }
}
