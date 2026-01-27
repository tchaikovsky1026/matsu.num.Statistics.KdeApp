/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.1.28
 */
package matsu.num.statistics.kdeapp.kde1d;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * 1次元のカーネル密度推定に使うデータソースのローダー.
 * 
 * @author Matsuura Y.
 */
final class Kde1dSourceLoader {

    private final DoubleDataLoader loader;

    /**
     * エスケープする文字列を指定し, ローダーを起動.
     * 
     * @param escapes エスケープする文字列のセット
     * @throws IllegalArgumentException エスケープ文字列に空文字が含まれる場合
     * @throws NullPointerException 引数にnullを含む場合
     */
    Kde1dSourceLoader(String... escapes) {
        DoubleLineParser lineParser = new DoubleLineParser(List.of(escapes));
        this.loader = new DoubleDataLoader(lineParser);
    }

    /**
     * ファイルをロードし, データソースを取得する.
     * 
     * @param path ファイルのパス
     * @return データソース
     * @throws IOException ファイルアクセスで例外が発生した場合, ファイルのフォーマットが不正の場合
     * @throws NullPointerException 引数がnull
     */
    double[] loadFrom(Path path) throws IOException {
        return loader.load(() -> Files.lines(path));
    }
}
