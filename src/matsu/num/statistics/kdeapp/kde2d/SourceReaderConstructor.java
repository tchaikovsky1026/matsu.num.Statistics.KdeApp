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

import java.nio.file.Path;
import java.util.NoSuchElementException;

import matsu.num.statistics.kdeapp.comp.ResolverContainer;
import matsu.num.statistics.kdeapp.exception.ProgrammingBugException;
import matsu.num.statistics.kdeapp.format.CommentPrefix;
import matsu.num.statistics.kdeapp.format.Separator;
import matsu.num.statistics.kdeapp.kde2d.task.Kde2dSourceReader;

/**
 * {@link Kde2dSourceReader} の構築器.
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
final class SourceReaderConstructor {

    /**
     * 唯一のコンストラクタ.
     */
    SourceReaderConstructor() {
    }

    /**
     * @throws NullPointerException {@inheritDoc }
     */
    Kde2dSourceReader apply(ResolverContainer property) {
        try {
            Path path = property.get(INPUT_FILE_PATH);
            CommentPrefix commentPrefix = property.get(INPUT_COMMENT_PREFIX);
            Separator separator = property.get(INPUT_SEPARATOR);
            return new Kde2dSourceReader(path, separator, commentPrefix);
        } catch (NoSuchElementException e) {
            throw new ProgrammingBugException(e.getMessage());
        }
    }
}
