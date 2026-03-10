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

import java.nio.file.Path;

import matsu.num.statistics.kdeapp.command.ComponentConstructor;
import matsu.num.statistics.kdeapp.command.ConsoleParameters;
import matsu.num.statistics.kdeapp.exception.ProgrammingBugException;
import matsu.num.statistics.kdeapp.format.CommentPrefix;
import matsu.num.statistics.kdeapp.kde1d.task.Kde1dSourceLoader;

/**
 * {@link Kde1dSourceLoader} の構築器.
 * 
 * <p>
 * 入力ファイルのフォーマットは, 次である.
 * </p>
 * 
 * <ul>
 * <li>コメント開始文字列はデフォルトが "#" (オプションコマンドで変更される)</li>
 * <li>ソースの値は 1 column で縦に並べる</li>
 * <li>ソースの値には inf, NaN を含まず, {@link Double#parseDouble(String)} で解釈可能</li>
 * </ul>
 * 
 * @author Matsuura Y.
 */
final class Kde1dSourceLoaderConstructor implements ComponentConstructor<Kde1dSourceLoader> {

    /**
     * 唯一のコンストラクタ.
     */
    Kde1dSourceLoaderConstructor() {
    }

    /**
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public Kde1dSourceLoader apply(ConsoleParameters interpreter) {

        // INPUT_FILE_PATH は必須パラメータなので必ず取得できる.
        Path path = interpreter.valueOf(INPUT_FILE_PATH)
                .orElseThrow(() -> new ProgrammingBugException("unreachable"));

        CommentPrefix commentPrefix = interpreter.valueOf(COMMENT_PREFIX)
                .orElse(CommentPrefix.of("#"));
        return new Kde1dSourceLoader(path, commentPrefix);
    }
}
