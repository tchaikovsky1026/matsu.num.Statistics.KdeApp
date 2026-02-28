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

import java.util.Optional;
import java.util.function.Function;

/**
 * １行1データであるような1行文字列に対し,
 * フィルタとデータ抽出を行うクラス.
 * 
 * <p>
 * 入力データとして与えられる各行の文字列を想定している.
 * </p>
 * 
 * @author Matsuura Y.
 */
public final class LineParser {

    private final CommentLineFilter lineFilter;

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
    public LineParser(CommentPrefix commentPrefix) {
        super();

        this.lineFilter = new CommentLineFilter(commentPrefix);
    }

    /**
     * 与えられた文字列に対してフィルタとデータ抽出を実行する:
     * 
     * <p>
     * 前処理として, 末尾 white space が削除され, コメント判定される. <br>
     * 前処理の結果が空文字の場合, コメント開始文字列から始まる場合は空が返る. <br>
     * その後, 前後の white space も削除され, 返される
     * </p>
     * 
     * @param line 解析する文字列
     * @return コメント行, 空行なら空
     * @throws NullPointerException 引数がnullの場合
     */
    public Optional<String> apply(String line) {
        return lineFilter.apply(line)
                .map(String::strip);
    }

    /**
     * 与えられた文字列に対してデータ抽出を実行し,
     * mapper によって値を変換して返す.
     * 
     * <p>
     * {@link #apply(String)} の要件に従う. <br>
     * また, 戻り値が空でない場合で, mapper によって例外が発生した場合はそのままスローする.
     * </p>
     * 
     * @param line 文字列
     * @param mapper マッパ
     * @return 抽出された結果
     * @throws RuntimeException マッパが例外をスローした場合
     * @throws NullPointerException 文字列がnullの場合, フィルタの結果が空でなくマッパがnullの場合
     * 
     */
    public <T> Optional<T> apply(String line, Function<? super String, ? extends T> mapper) {
        return apply(line).map(mapper);
    }
}
