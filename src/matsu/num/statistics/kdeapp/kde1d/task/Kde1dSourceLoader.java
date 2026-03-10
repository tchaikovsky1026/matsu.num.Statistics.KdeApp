/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.10
 */
package matsu.num.statistics.kdeapp.kde1d.task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import matsu.num.statistics.kdeapp.exception.InputException;
import matsu.num.statistics.kdeapp.format.CommentPrefix;
import matsu.num.statistics.kdeapp.format.LineParser;
import matsu.num.statistics.kdeapp.logging.AppLogger;

/**
 * 1次元のカーネル密度推定に使うデータソースのローダー.
 * 
 * @author Matsuura Y.
 */
public final class Kde1dSourceLoader {

    private static final AppLogger LOGGER =
            AppLogger.getLogger(Kde1dSourceLoader.class);

    private final DoubleDataLoader loader;
    private final Path path;

    /**
     * エスケープする文字列を指定し, ローダーを起動.
     * 
     * @param path ロードするファイルのパス
     * @param commentPrefix コメント開始文字列
     * @throws IllegalArgumentException コメント開始文字が空文字の場合
     * @throws NullPointerException 引数にnullを含む場合
     */
    public Kde1dSourceLoader(Path path, CommentPrefix commentPrefix) {
        this.loader = new DoubleDataLoader(new LineParser(commentPrefix));
        this.path = Objects.requireNonNull(path);
    }

    /**
     * ファイルをロードし, データソースを取得する.
     * 
     * @return データソース
     * @throws InputException ファイルアクセスで例外が発生した場合, ファイルのフォーマットが不正の場合
     */
    public double[] load() {
        try {
            double[] out = loader.load(() -> Files.lines(path));
            LOGGER.info("load: \"" + path.toAbsolutePath().normalize() + "\"");
            return out;
        } catch (IOException e) {
            throw new InputException(
                    e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
}
