/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.2
 */
package matsu.num.statistics.kdeapp.kde1d;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * 1次元のカーネル密度推定に使うデータソースのローダー.
 * 
 * @author Matsuura Y.
 */
final class Kde1dSourceLoader {

    private final DoubleDataLoader loader;
    private final Path path;

    /**
     * エスケープする文字列を指定し, ローダーを起動.
     * 
     * @param path ロードするファイルのパス
     * @param escapes エスケープする文字列のセット
     * @throws IllegalArgumentException エスケープ文字列に空文字が含まれる場合
     * @throws NullPointerException 引数にnullを含む場合
     */
    Kde1dSourceLoader(Path path, String... escapes) {
        DoubleLineParser lineParser = new DoubleLineParser(List.of(escapes));
        this.loader = new DoubleDataLoader(lineParser);
        this.path = Objects.requireNonNull(path);
    }

    /**
     * ファイルをロードし, データソースを取得する.
     * 
     * @return データソース
     * @throws IOException ファイルアクセスで例外が発生した場合, ファイルのフォーマットが不正の場合
     */
    double[] load() throws IOException {
        return loader.load(() -> Files.lines(path));
    }
}
