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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import matsu.num.statistics.kdeapp.exception.InputException;
import matsu.num.statistics.kdeapp.format.CommentPrefix;
import matsu.num.statistics.kdeapp.format.DelimitedLineParser;
import matsu.num.statistics.kdeapp.format.Separator;

/**
 * 2次元のカーネル密度推定に使うデータソースのローダー.
 * 
 * @author Matsuura Y.
 */
final class Kde2dSourceLoader {

    private final DoubleDataLoader loader;
    private final Path path;

    /**
     * エスケープする文字列を指定し, ローダーを起動.
     * 
     * @param path ロードするファイルのパス
     * @param separator 区切り文字
     * @param commentPrefix コメント開始文字列
     * @throws IllegalArgumentException コメント開始文字が空文字の場合
     * @throws NullPointerException 引数にnullを含む場合
     */
    Kde2dSourceLoader(Path path, Separator separator, CommentPrefix commentPrefix) {
        this.loader = new DoubleDataLoader(
                new DelimitedLineParser(2, separator, commentPrefix));
        this.path = Objects.requireNonNull(path);
    }

    /**
     * ファイルをロードし, データソースを取得する.
     * 
     * @return データソース
     * @throws InputException ファイルアクセスで例外が発生した場合, ファイルのフォーマットが不正の場合
     */
    double[][] load() {
        try {
            return loader.load(() -> Files.lines(path));
        } catch (IOException e) {
            throw new InputException(
                    e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
}
