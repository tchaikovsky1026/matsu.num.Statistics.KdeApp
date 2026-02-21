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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * 固定カラム数のデータを区切り文字で結合した1行文字列に対し,
 * 区切り文字による分割とフィルタを行うクラス.
 * 
 * <p>
 * 入力データとして与えられる各行の文字列を想定している.
 * </p>
 * 
 * @author Matsuura Y.
 */
public final class DelimitedLineParser {

    private final LineFilter lineFilter;

    private final Separator separator;
    private final String separatorPattern;
    private final int columns;

    /**
     * 公開されたコンストラクタ.
     * 
     * <p>
     * カラム数, コメント開始文字列を与える.
     * </p>
     * 
     * @param columns カラム数
     * @param separator 区切り文字
     * @param commentPrefix コメント開始文字列
     * @throws IllegalArgumentException columnsが0以下の場合
     * @throws NullPointerException 引数がnullの場合
     */
    public DelimitedLineParser(int columns, Separator separator, CommentPrefix commentPrefix) {
        super();

        if (columns <= 0) {
            throw new IllegalArgumentException("invalid: columns <= 0");
        }

        this.columns = columns;
        this.separator = Objects.requireNonNull(separator);
        this.lineFilter = new LineFilter(commentPrefix);

        this.separatorPattern = Pattern.quote(this.separator.asString());
    }

    /**
     * このインスタンスが扱うカラム数の値を返す.
     * 
     * @return columns カラム数
     */
    public int columns() {
        return columns;
    }

    /**
     * 与えられた文字列に対してフィルタを実行する:
     * {@link String} の配列として抽出する.
     * 
     * <p>
     * 前処理として, 前後のブランクが削除され, コメント判定される. <br>
     * 前処理の結果が空文字の場合, コメント開始文字列から始まる場合は空が返る. <br>
     * その後, セパレータで分割され, 各要素について前後のブランクを削除する
     * (処理の結果, ブランクになった場合は無視される).
     * </p>
     * 
     * <p>
     * フィルタの結果で, 要素の数が {@link #columns()} に一致しない場合は例外をスローする.
     * </p>
     * 
     * @param line 解析する文字列
     * @return エスケープ行, 空行なら空
     * @throws IllegalArgumentException 正当なフォーマットでない場合
     * @throws NullPointerException null
     */
    public Optional<String[]> apply(String line) {

        Optional<String[]> out = lineFilter.apply(line)
                .map(s -> s.split(separatorPattern))
                .map(
                        arr -> Arrays.stream(arr)
                                .flatMap(s -> lineFilter.apply(s).stream())
                                .toArray(String[]::new));

        // カラム数の確認
        out.ifPresent(arr -> {
            if (arr.length != columns) {
                throw new IllegalArgumentException("invalid column size: " + line);
            }
        });

        return out;
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
     * @param <T> マッパによる変換後の型
     * @param line 文字列
     * @param mapper マッパ
     * @param typeToken {@code T} 型の型トークン
     * @return フィルタされた結果
     * @throws RuntimeException マッパが例外をスローした場合
     * @throws IllegalArgumentException 正当なフォーマットでない場合
     * @throws NullPointerException 文字列がnullの場合, フィルタの結果が空でなくマッパがnullの場合
     */
    public <T> Optional<T[]> apply(
            String line, Function<? super String, ? extends T> mapper, Class<T> typeToken) {

        return apply(line)
                .map(src -> convert(src, mapper, typeToken));
    }

    private <T> T[] convert(
            String[] src,
            Function<? super String, ? extends T> mapper,
            Class<T> typeToken) {

        // このキャストは必ず成功する
        @SuppressWarnings("unchecked")
        Class<T[]> arrayType = (Class<T[]>) typeToken.arrayType();

        // このキャストは必ず成功する
        return Arrays.stream(src)
                .map(mapper)
                .toArray(len -> arrayType.cast(Array.newInstance(typeToken, len)));
    }
}
