/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.22
 */
package matsu.num.statistics.kdeapp.format;

import java.util.Objects;
import java.util.Optional;

/**
 * 1行文字列に対し, フィルタを行うクラス.
 * 
 * <p>
 * 入力データとして与えられる各行の文字列を想定している.
 * </p>
 * 
 * @author Matsuura Y.
 */
public final class CommentLineFilter {

    private final CommentPrefix commentPrefix;

    /**
     * 公開されたコンストラクタ.
     * 
     * <p>
     * コメント開始文字列を与える.
     * </p>
     * 
     * @param commentPrefix コメント開始文字列
     * @throws NullPointerException 引数がnullの場合
     */
    public CommentLineFilter(CommentPrefix commentPrefix) {
        super();
        this.commentPrefix = Objects.requireNonNull(commentPrefix);
    }

    /**
     * 与えられた文字列に対してフィルタを実行する.
     * 
     * <p>
     * 前処理として, 後のブランクが削除される. <br>
     * 前処理後の文字列が {@link Optional} でラップされて返るが,
     * 空文字の場合, コメント開始文字列から始まる場合は空が返る.
     * </p>
     * 
     * @param line 文字列
     * @return フィルタされた結果
     * @throws NullPointerException 引数がnullの場合
     */
    public Optional<String> apply(String line) {
        line = line.stripTrailing();
        if (line.isEmpty() || line.startsWith(commentPrefix.asString())) {
            return Optional.empty();
        }

        return Optional.of(line);
    }
}
