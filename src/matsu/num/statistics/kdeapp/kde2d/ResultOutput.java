/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.19
 */
package matsu.num.statistics.kdeapp.kde2d;

import static java.nio.file.StandardOpenOption.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Objects;

import matsu.num.statistics.kdeapp.exception.OutputException;

/**
 * 結果の外部出力を扱う.
 * 
 * @author Matsuura Y.
 */
abstract class ResultOutput {

    /**
     * null-出力を表すシングルトンインスタンス.
     */
    private static final ResultOutput nullOutput = new ResultOutput() {

        @Override
        void write(WritableKde2dResult result, WritingFormatter writingFormatter) {
            // 何もしない.
        }
    };

    /**
     * 強制上書きモードによる出力を返す.
     * 
     * @throws NullPointerException 引数がnullを含む場合
     */
    static ResultOutput forceOutput(Path path) {
        return new FileOutput(path, FileOutput.OverwriteOption.FORCE);
    }

    /**
     * 上書き禁止モードによる出力を返す.
     * 
     * @throws NullPointerException 引数がnullを含む場合
     */
    static ResultOutput regularOutput(Path path) {
        return new FileOutput(path, FileOutput.OverwriteOption.REGULAR);
    }

    /**
     * null-出力を返す.
     */
    static ResultOutput nullOutput() {
        return nullOutput;
    }

    /**
     * 非公開のコンストラクタ. <br>
     * ネストしたクラスからの継承のみ許可.
     */
    private ResultOutput() {

    }

    /**
     * 書き込みを実行する.
     * 
     * @param result 結果
     * @param writingFormatter フォーマッタ
     * @throws OutputException 例外が発生した場合
     * @throws NullPointerException 引数がnull (スローされない場合がある)
     */
    abstract void write(WritableKde2dResult result, WritingFormatter writingFormatter);

    /**
     * ファイルへの出力.
     */
    private static final class FileOutput extends ResultOutput {

        private final OverwriteOption outputOption;
        private final Path path;

        /**
         * @param forceOverwrite 強制上書きするかどうかに関するオプション
         * @throws NullPointerException 引数がnullを含む場合
         */
        FileOutput(Path path, OverwriteOption outputOption) {
            this.path = Objects.requireNonNull(path);
            this.outputOption = Objects.requireNonNull(outputOption);
        }

        /**
         * @throws OutputException {@inheritDoc}
         * @throws NullPointerException {@inheritDoc}
         */
        @Override
        void write(WritableKde2dResult result, WritingFormatter writingFormatter) {
            try {
                // 出力ディレクトリの構築
                Path parent = path.getParent();
                if (Objects.nonNull(parent)) {
                    Files.createDirectories(parent);
                }

                // 結果の出力
                try (PrintWriter output = new PrintWriter(
                        Files.newBufferedWriter(path, outputOption.openOption))) {
                    if (result.write(output, writingFormatter)) {
                        throw new IOException("write to " + path.toString());
                    }
                }
            } catch (InvalidPathException | IOException e) {
                throw new OutputException(
                        e.getClass().getSimpleName() + ": " + e.getMessage());
            }
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
}
