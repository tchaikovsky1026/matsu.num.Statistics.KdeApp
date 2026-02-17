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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 1行の文字列を2個の {@code double} 値に変換するパーサー.
 * 
 * @author Matsuura Y.
 */
final class DoubleColumnDoubleLineParser {

    private static final int COLUMNS = 2;

    private final Set<String> escapes;
    private final String separatorPattern;

    /**
     * インスタンスを生成する.
     * 
     * <p>
     * 引数でエスケープ行の開始文字列と, 区切り文字を指定する. <br>
     * コレクションが空ならばエスケープしない. <br>
     * </p>
     * 
     * <p>
     * エスケープ文字列は, ブランクであってはならない. <br>
     * 前後のブランクは削除される.
     * </p>
     * 
     * @param escapes エスケープ文字列のセット (空の場合はエスケープしない)
     * @param separator 区切り文字
     * @throws IllegalArgumentException エスケープ文字列に空文字が含まれる場合
     * @throws NullPointerException 引数がnullの場合, コレクションがnullを含む場合
     */
    DoubleColumnDoubleLineParser(Collection<String> escapes, char separator) {
        super();

        this.escapes = new HashSet<>();
        for (String s : new ArrayList<>(escapes)) {
            String trim = s.strip();
            if (trim.isEmpty()) {
                throw new IllegalArgumentException("including blank");
            }
            this.escapes.add(trim);
        }

        this.separatorPattern = Pattern.quote(String.valueOf(separator));
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

        if (escapes.stream().anyMatch(s::startsWith)) {
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
