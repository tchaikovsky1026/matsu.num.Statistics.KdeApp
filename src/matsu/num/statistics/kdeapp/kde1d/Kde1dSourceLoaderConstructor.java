/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.20
 */
package matsu.num.statistics.kdeapp.kde1d;

import static matsu.num.statistics.kdeapp.kde1d.Commands.*;

import java.nio.file.Path;

import matsu.num.statistics.kdeapp.command.ConsoleParameters;

/**
 * {@link Kde1dSourceLoader} の構築器.
 * 
 * <p>
 * 入力ファイルのフォーマットは, 次である.
 * </p>
 * 
 * <ul>
 * <li>エスケープ文字はデフォルトが "#" (オプションコマンドで変更される)</li>
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
    public Kde1dSourceLoader construct(ConsoleParameters interpreter) {

        // INPUT_FILE_PATH は必須パラメータなので必ず取得できる.
        Path path = interpreter.valueOf(INPUT_FILE_PATH)
                .orElseThrow(() -> new AssertionError("unreachable"));

        String escape = interpreter.valueOf(COMMENT_CHAR)
                .orElse("#");
        return new Kde1dSourceLoader(path, escape);
    }
}
