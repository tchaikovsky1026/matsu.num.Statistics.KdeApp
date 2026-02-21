/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.19
 */
package matsu.num.statistics.kdeapp.kde2d;

import static matsu.num.statistics.kdeapp.kde2d.Commands.*;

import java.nio.file.Path;

import matsu.num.statistics.kdeapp.command.ConsoleParameters;
import matsu.num.statistics.kdeapp.format.CommentPrefix;
import matsu.num.statistics.kdeapp.format.Separator;

/**
 * {@link Kde2dSourceLoader} の構築器.
 * 
 * <p>
 * 入力ファイルのフォーマットは, 次である.
 * </p>
 * 
 * <ul>
 * <li>エスケープ文字はデフォルトが "#" (オプションコマンドで変更される)</li>
 * <li>ソースの値は 2 column で縦に並べ, 区切り文字はデフォルトが "\t" (オプションコマンドで変更される)</li>
 * <li>ソースの値には inf, NaN を含まず, {@link Double#parseDouble(String)} で解釈可能</li>
 * </ul>
 * 
 * @author Matsuura Y.
 */
final class Kde2dSourceLoaderConstructor implements ComponentConstructor<Kde2dSourceLoader> {

    /**
     * 唯一のコンストラクタ.
     */
    Kde2dSourceLoaderConstructor() {
    }

    /**
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public Kde2dSourceLoader construct(ConsoleParameters interpreter) {

        // INPUT_FILE_PATH は必須パラメータなので必ず取得できる.
        Path path = interpreter.valueOf(INPUT_FILE_PATH)
                .orElseThrow(() -> new AssertionError("unreachable"));

        CommentPrefix commentPrefix = interpreter.valueOf(COMMENT_PREFIX)
                .orElse(CommentPrefix.of("#"));
        Separator separator = interpreter.valueOf(SEPARATOR_INPUT)
                .orElse(Separator.from("\t"));
        return new Kde2dSourceLoader(path, separator, commentPrefix);
    }
}
