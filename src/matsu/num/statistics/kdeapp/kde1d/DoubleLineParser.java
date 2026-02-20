/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.20
 */
package matsu.num.statistics.kdeapp.kde1d;

import java.util.OptionalDouble;

/**
 * 1行の文字列を1個の {@code double} 値に変換するパーサー.
 * 
 * @author Matsuura Y.
 */
final class DoubleLineParser {

    private final String commentPrefix;

    /**
     * インスタンスを生成する.
     * 
     * <p>
     * 引数でコメント開始文字列を指定する. <br>
     * ブランクであってはならない. <br>
     * 前後のブランクは削除される.
     * </p>
     * 
     * @param commentPrefix コメント開始文字列のセット
     * @throws IllegalArgumentException 空文字の場合
     * @throws NullPointerException 引数がnullの場合
     */
    DoubleLineParser(String commentPrefix) {
        super();

        this.commentPrefix = commentPrefix.strip();
        if (this.commentPrefix.isEmpty()) {
            throw new IllegalArgumentException("blank");
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

        if (s.startsWith(commentPrefix)) {
            return OptionalDouble.empty();
        }
        return OptionalDouble.of(Double.parseDouble(s));
    }
}
