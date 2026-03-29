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

import java.nio.file.Path;
import java.util.NoSuchElementException;

import matsu.num.statistics.kdeapp.comp.ResolverContainer;
import matsu.num.statistics.kdeapp.exception.ProgrammingBugException;
import matsu.num.statistics.kdeapp.format.CommentPrefix;
import matsu.num.statistics.kdeapp.kde1d.task.Kde1dSourceReader;

/**
 * {@link Kde1dSourceReader} の構築器.
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
final class SourceReaderConstructor {

    /**
     * 唯一のコンストラクタ.
     */
    SourceReaderConstructor() {
    }

    /**
     * @throws NullPointerException {@inheritDoc }
     */
    Kde1dSourceReader apply(ResolverContainer property) {
        try {
            Path path = property.get(INPUT_FILE_PATH);
            CommentPrefix commentPrefix = property.get(INPUT_COMMENT_PREFIX);
            return new Kde1dSourceReader(path, commentPrefix);
        } catch (NoSuchElementException e) {
            throw new ProgrammingBugException(e.getMessage());
        }
    }
}
