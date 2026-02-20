/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.20
 */
package matsu.num.statistics.kdeapp.kde2d;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

import matsu.num.statistics.kdeapp.format.Separator;

/**
 * 1行の文字列を2個の {@code double} 値に変換するパーサー.
 * 
 * @author Matsuura Y.
 */
final class DoubleColumnDoubleLineParser {

    private static final int COLUMNS = 2;

    private final String commentPrefix;
    private final String separatorPattern;

    /**
     * インスタンスを生成する.
     * 
     * <p>
     * 引数でコメント開始文字列を指定する. <br>
     * ブランクであってはならない. <br>
     * 前後のブランクは削除される.
     * </p>
     * 
     * @param commentPrefix コメント開始文字列
     * @param separator 区切り文字
     * @throws IllegalArgumentException エスケープ文字列に空文字が含まれる場合
     * @throws NullPointerException 引数がnullの場合
     */
    DoubleColumnDoubleLineParser(String commentPrefix, Separator separator) {
        super();

        this.commentPrefix = commentPrefix.strip();
        if (this.commentPrefix.isEmpty()) {
            throw new IllegalArgumentException("blank");
        }

        this.separatorPattern = Pattern.quote(separator.asString());
    }

    /**
     * 文字列を解析し, 2個の {@code double} 値を抽出する.
     * 
     * <p>
     * ブランクの場合, エスケープ文字列から始まる場合は空が返る. <br>
     * 前後のブランクは削除される.
     * </p>
     * 
     * @param line 解析する文字列
     * @return エスケープ行, 空行なら空
     * @throws NumberFormatException フォーマット不正で値を抽出できなかった場合
     * @throws NullPointerException null
     */
    public Optional<double[]> parse(String line) {
        String s = line.strip();
        if (s.isEmpty()) {
            return Optional.empty();
        }
        if (s.startsWith(commentPrefix)) {
            return Optional.empty();
        }

        String[] splitStrings = line.split(separatorPattern, -1);
        if (splitStrings.length != COLUMNS) {
            throw new NumberFormatException("invalid column size: " + line);
        }

        // パース時にNumberFormatExceptionが発生する可能性あり.
        return Optional.of(
                Arrays.stream(splitStrings)
                        .map(String::strip)
                        .mapToDouble(Double::parseDouble)
                        .toArray());
    }
}
