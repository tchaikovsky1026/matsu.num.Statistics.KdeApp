/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.17
 */
package matsu.num.statistics.kdeapp.kde2d;

import static matsu.num.statistics.kdeapp.kde2d.command.ArgumentRequiringCommand.*;

import matsu.num.statistics.kdeapp.kde2d.command.ConsoleParameterInterpreter;

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
    public Kde2dSourceLoader construct(ConsoleParameterInterpreter interpreter) {

        String pathString = interpreter.valueOf(INPUT_FILE_PATH)
                .orElseThrow(() -> new AssertionError("unreachable"));

        String escape = interpreter.valueOf(COMMENT_CHAR)
                .orElse("#");
        char separator = interpreter.valueOf(SEPARATOR)
                .orElse('\t');
        return new Kde2dSourceLoader(pathString, separator, escape);
    }
}
