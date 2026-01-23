/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.1.13
 */
package matsu.num.statistics.kdeapp.kde1d;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.OptionalDouble;
import java.util.Set;

/**
 * 1行の文字列を1個の {@code double} 値に変換するパーサー.
 * 
 * @author Matsuura Y.
 */
final class DoubleLineParser {

    private final Set<String> escapes;

    /**
     * インスタンスを生成する.
     * 
     * <p>
     * 引数でエスケープ行の開始文字列を指定する. <br>
     * コレクションが空ならばエスケープしない. <br>
     * </p>
     * 
     * <p>
     * エスケープ文字列は, ブランクであってはならない. <br>
     * 前後のブランクは削除される.
     * </p>
     * 
     * @param escapes エスケープ文字列のセット (空の場合はエスケープしない)
     * @throws IllegalArgumentException エスケープ文字列に空文字が含まれる場合
     * @throws NullPointerException 引数がnullの場合, コレクションがnullを含む場合
     */
    DoubleLineParser(Collection<String> escapes) {
        super();

        this.escapes = new HashSet<>();
        for (String s : new ArrayList<>(escapes)) {
            String trim = s.strip();
            if (trim.isEmpty()) {
                throw new IllegalArgumentException("including blank");
            }
            this.escapes.add(trim);
        }
    }

    /**
     * 文字列を解析し, 1個の {@code double} 値を抽出する.
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
    public OptionalDouble parse(String line) {
        String s = line.strip();
        if (s.isEmpty()) {
            return OptionalDouble.empty();
        }

        if (escapes.stream().anyMatch(s::startsWith)) {
            return OptionalDouble.empty();
        }
        return OptionalDouble.of(Double.parseDouble(s));
    }
}
