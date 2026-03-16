/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.11
 */
package matsu.num.statistics.kdeapp.kde1d.task;

import static java.nio.file.StandardOpenOption.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Objects;

import matsu.num.statistics.kdeapp.exception.OutputException;
import matsu.num.statistics.kdeapp.logging.AppLogger;

/**
 * 結果の外部出力を扱う.
 * 
 * @author Matsuura Y.
 */
public final class ResultFileWriter implements ResultWriter {

    private static final AppLogger LOGGER =
            AppLogger.getLogger(ResultFileWriter.class);

    private final OverwriteOption outputOption;
    private final Path path;

    /**
     * 非公開のコンストラクタ.
     * 
     * @param forceOverwrite 強制上書きするかどうかに関するオプション
     * @throws NullPointerException 引数がnullを含む場合
     */
    private ResultFileWriter(Path path, OverwriteOption outputOption) {
        this.path = Objects.requireNonNull(path);
        this.outputOption = Objects.requireNonNull(outputOption);
    }

    /**
     * @throws OutputException {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public void write(WritableKde1dResult result, WritingFormatter writingFormatter) {
        try {
            // 出力ディレクトリの構築
            Path parent = path.getParent();
            if (Objects.nonNull(parent)) {
                Files.createDirectories(parent);
            }

            // 結果の出力
            try (PrintWriter output = new PrintWriter(
                    Files.newBufferedWriter(path, outputOption.openOption, WRITE, TRUNCATE_EXISTING))) {
                if (result.write(output, writingFormatter)) {
                    throw new IOException("write to " + path.toString());
                }
            }

            LOGGER.info("output to file: \"" + path.toAbsolutePath().normalize() + "\"");
        } catch (InvalidPathException | IOException e) {
            throw new OutputException(
                    e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    /**
     * 強制上書きモードによる出力を返す.
     * 
     * @param path 出力パス
     * @return 出力
     * @throws NullPointerException 引数がnullを含む場合
     */
    public static ResultFileWriter forceWriter(Path path) {
        return new ResultFileWriter(path, OverwriteOption.FORCE);
    }

    /**
     * 上書き禁止モードによる出力を返す.
     * 
     * @param path 出力パス
     * @return 出力
     * @throws NullPointerException 引数がnullを含む場合
     */
    public static ResultFileWriter regularWriter(Path path) {
        return new ResultFileWriter(path, OverwriteOption.REGULAR);
    }

    /**
     * 出力の上書きに関するオプション.
     */
    private static enum OverwriteOption {

        /**
         * 上書き禁止モードによる出力.
         */
        REGULAR(CREATE_NEW),

        /**
         * 強制上書きモードによる出力.
         */
        FORCE(CREATE);

        private final OpenOption openOption;

        private OverwriteOption(OpenOption openOption) {
            this.openOption = openOption;
        }
    }
}
