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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import matsu.num.statistics.kdeapp.kde2d.exception.InputException;

/**
 * 2次元のカーネル密度推定に使うデータソースのローダー.
 * 
 * @author Matsuura Y.
 */
final class Kde2dSourceLoader {

    private final DoubleDataLoader loader;
    private final String pathString;

    /**
     * エスケープする文字列を指定し, ローダーを起動.
     * 
     * @param path ロードするファイルのパス
     * @param separator 区切り文字
     * @param escapes エスケープする文字列のセット
     * @throws IllegalArgumentException エスケープ文字列に空文字が含まれる場合
     * @throws NullPointerException 引数にnullを含む場合
     */
    Kde2dSourceLoader(String pathString, char separator, String... escapes) {
        DoubleColumnDoubleLineParser lineParser =
                new DoubleColumnDoubleLineParser(List.of(escapes), separator);
        this.loader = new DoubleDataLoader(lineParser);
        this.pathString = Objects.requireNonNull(pathString);
    }

    /**
     * ファイルをロードし, データソースを取得する.
     * 
     * @return データソース
     * @throws InputException ファイルアクセスで例外が発生した場合, ファイルのフォーマットが不正の場合
     */
    double[][] load() {
        try {
            Path path = Path.of(pathString);
            return loader.load(() -> Files.lines(path));
        } catch (InvalidPathException | IOException e) {
            throw new InputException(
                    e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
}
