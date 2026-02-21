/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.21
 */
package matsu.num.statistics.kdeapp.format;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * 1行文字列に対し, フィルタを行うクラス.
 * 
 * <p>
 * 入力データとして与えられる各行の文字列を想定している.
 * </p>
 * 
 * @author Matsuura Y.
 */
public final class LineFilter {

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
    public LineFilter(CommentPrefix commentPrefix) {
        super();
        this.commentPrefix = Objects.requireNonNull(commentPrefix);
    }

    /**
     * 与えられた文字列に対してフィルタを実行する.
     * 
     * <p>
     * 前処理として, 前後のブランクが削除される. <br>
     * 前処理後の文字列が {@link Optional} でラップされて返るが,
     * 空文字の場合, コメント開始文字列から始まる場合は空が返る.
     * </p>
     * 
     * @param line 文字列
     * @return フィルタされた結果
     * @throws NullPointerException 引数がnullの場合
     */
    public Optional<String> apply(String line) {
        line = line.strip();
        if (line.isEmpty() || line.startsWith(commentPrefix.asString())) {
            return Optional.empty();
        }

        return Optional.of(line);
    }

    /**
     * 与えられた文字列に対してフィルタを実行し,
     * mapper によって値を変換して返す.
     * 
     * <p>
     * {@link #apply(String)} の要件に従う. <br>
     * また, 空文字でない場合で, mapper によって例外が発生した場合はそのままスローする.
     * </p>
     * 
     * @param line 文字列
     * @param mapper マッパ
     * @return フィルタされた結果
     * @throws RuntimeException マッパが例外をスローした場合
     * @throws NullPointerException 文字列がnullの場合, フィルタの結果が空でなくマッパがnullの場合
     */
    public <T> Optional<T> apply(String line, Function<? super String, ? extends T> mapper) {
        return apply(line).map(mapper);
    }
}
