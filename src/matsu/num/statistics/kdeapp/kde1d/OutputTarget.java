/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.12
 */
package matsu.num.statistics.kdeapp.kde1d;

import static java.nio.charset.StandardCharsets.*;
import static java.nio.file.StandardOpenOption.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import matsu.num.statistics.kdeapp.kde1d.exception.OutputException;

/**
 * 結果出力のリソースを扱う.
 * 
 * @author Matsuura Y.
 */
abstract class OutputTarget {

    /**
     * 強制上書きモードによる出力を返す.
     * 
     * @throws NullPointerException 引数がnullを含む場合
     */
    static OutputTarget forceOutput(String filePath) {
        return new ForceOutput(filePath);
    }

    /**
     * 上書き禁止モードによる出力を返す.
     * 
     * @throws NullPointerException 引数がnullを含む場合
     */
    static OutputTarget regularOutput(String filePath) {
        return new RegularOutput(filePath);
    }

    /**
     * null-出力を返す.
     */
    static OutputTarget nullOutput() {
        return NullOutput.INSTANCE;
    }

    /**
     * 非公開のコンストラクタ. <br>
     * ネストしたクラスからの継承のみ許可.
     */
    private OutputTarget() {

    }

    /**
     * 書き込みを実行する.
     * 
     * @param result 結果
     * @param writingFormatter フォーマッタ
     * @throws OutputException 例外が発生した場合
     * @throws NullPointerException 引数がnull (スローされない場合がある)
     */
    abstract void write(WritableKde1dResult result, WritingFormatter writingFormatter);

    /**
     * null-出力.
     */
    private static final class NullOutput extends OutputTarget {

        /**
         * シングルトンインスタンス.
         */
        static final NullOutput INSTANCE = new NullOutput();

        private NullOutput() {
        }

        @Override
        void write(WritableKde1dResult result, WritingFormatter writingFormatter) {
            // 何もしない.
        }
    }

    /**
     * 強制上書きモードによる出力.
     */
    private static final class ForceOutput extends OutputTarget {

        private final String filePath;

        /**
         * @throws NullPointerException 引数がnullを含む場合
         */
        ForceOutput(String filePath) {
            this.filePath = Objects.requireNonNull(filePath);
        }

        /**
         * @throws OutputException {@inheritDoc}
         * @throws NullPointerException {@inheritDoc}
         */
        @Override
        void write(WritableKde1dResult result, WritingFormatter writingFormatter) {
            try {
                Path path = Paths.get(filePath);

                // 出力ディレクトリの構築
                Path parent = path.getParent();
                if (Objects.nonNull(path)) {
                    Files.createDirectories(parent);
                }

                // 結果の出力
                try (PrintWriter output = new PrintWriter(
                        Files.newBufferedWriter(path, UTF_8))) {
                    if (result.write(output, writingFormatter)) {
                        throw new IOException("write to " + path.toString());
                    }
                }
            } catch (InvalidPathException | IOException e) {
                throw new OutputException(
                        e.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }
    }

    /**
     * 強制上書きモードによる出力.
     */
    private static final class RegularOutput extends OutputTarget {

        private final String filePath;

        /**
         * @throws NullPointerException 引数がnullを含む場合
         */
        RegularOutput(String filePath) {
            this.filePath = Objects.requireNonNull(filePath);
        }

        /**
         * @throws OutputException {@inheritDoc}
         * @throws NullPointerException {@inheritDoc}
         */
        @Override
        void write(WritableKde1dResult result, WritingFormatter writingFormatter) {
            try {
                Path path = Paths.get(filePath);

                // 出力ディレクトリの構築
                Path parent = path.getParent();
                if (Objects.nonNull(path)) {
                    Files.createDirectories(parent);
                }

                // 結果の出力
                try (PrintWriter output = new PrintWriter(
                        Files.newBufferedWriter(path, UTF_8, CREATE_NEW))) {
                    if (result.write(output, writingFormatter)) {
                        throw new IOException("write to " + path.toString());
                    }
                }
            } catch (InvalidPathException | IOException e) {
                throw new OutputException(
                        e.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }
    }
}
