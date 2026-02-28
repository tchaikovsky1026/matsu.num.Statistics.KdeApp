/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.27
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
 * フィルタと区切り文字による分割, データ抽出を行うクラス.
 * 
 * <p>
 * 入力データとして与えられる各行の文字列を想定している.
 * </p>
 * 
 * @author Matsuura Y.
 */
public final class DelimitedLineParser {

    private final CommentLineFilter lineFilter;

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
        this.lineFilter = new CommentLineFilter(commentPrefix);

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
     * 与えられた文字列に対してフィルタとデータ抽出を実行する:
     * 
     * <p>
     * 前処理として, 末尾 white space が削除され, コメント判定される. <br>
     * 前処理の結果が空文字の場合, コメント開始文字列から始まる場合は空が返る. <br>
     * その後, セパレータで分割され, 各要素について前後の white space を削除する.
     * </p>
     * 
     * <p>
     * データ抽出の結果で, 要素の数が {@link #columns()} に一致しない場合,
     * 空データがある場合は例外をスローする.
     * </p>
     * 
     * @param line 解析する文字列
     * @return コメント行, 空行なら空
     * @throws IllegalArgumentException 正当なフォーマットでない場合
     * @throws NullPointerException 引数がnullの場合
     */
    public Optional<String[]> apply(String line) {

        Optional<String[]> out = lineFilter.apply(line)
                .map(s -> s.split(separatorPattern))
                .map(s -> toData(s));

        // カラム数, 空データの確認
        out.ifPresent(arr -> {
            if (arr.length != columns ||
                    Arrays.stream(arr).anyMatch(String::isEmpty)) {
                throw new IllegalArgumentException("invalid line: \"" + line + "\"");
            }
        });

        return out;
    }

    /**
     * ソースの文字列配列をデータ化する. <br>
     * データ化は, 以下を行う.
     * 
     * <ul>
     * <li>文字列の両側 white space を削除.</li>
     * <li>末尾にある連続した空文字をすべて排除.</li>
     * <li>末尾でない位置に空文が残っている場合は残す.</li>
     * </ul>
     */
    private String[] toData(String[] src) {

        int validSize = src.length;

        // strip後に空にならないデータを探すことで, 有効なサイズを知る
        while (validSize >= 1) {
            if (!src[validSize - 1].strip().isEmpty()) {
                break;
            }
            validSize--;
        }

        return Arrays.stream(src)
                .limit(validSize)
                .map(String::strip)
                .toArray(String[]::new);
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
     * @param <T> マッパによる変換後の型
     * @param line 文字列
     * @param mapper マッパ
     * @param typeToken {@code T} 型の型トークン
     * @return 抽出された結果
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
