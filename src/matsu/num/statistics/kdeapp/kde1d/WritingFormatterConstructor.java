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
