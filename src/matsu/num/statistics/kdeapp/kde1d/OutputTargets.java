/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.8
 */
package matsu.num.statistics.kdeapp.kde1d;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import matsu.num.statistics.kdeapp.kde1d.exception.OutputException;

/**
 * 結果出力のリソースを扱う.
 * 
 * <p>
 * 標準出力は必ず含むとする.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class OutputTargets {

    private final PrintStream out;
    @SuppressWarnings("unused")
    private final PrintStream err;

    private final List<String> filePaths;

    /**
     * @throws NullPointerException 引数がnullを含む場合
     */
    OutputTargets(PrintStream out, PrintStream err, List<String> filePaths) {
        this.out = Objects.requireNonNull(out);
        this.err = Objects.requireNonNull(err);

        this.filePaths = List.copyOf(filePaths);

        if (this.filePaths.stream().anyMatch(Objects::isNull)) {
            throw new NullPointerException("including null");
        }
    }

    /**
     * 書き込みを実行する.
     * 
     * @param result 結果
     * @param writingFormatter フォーマッタ
     * @throws OutputException 例外が発生した場合(一度でも)
     * @throws NullPointerException 引数がnull
     */
    void write(WritableKde1dResult result, WritingFormatter writingFormatter) {

        result.write(new PrintWriter(out), writingFormatter);

        for (String pathStr : filePaths) {
            try {
                Path path = Paths.get(pathStr);

                // 出力ディレクトリの構築
                Path parent = path.getParent();
                if (Objects.nonNull(path)) {
                    Files.createDirectories(parent);
                }

                // 結果の出力
                try (PrintWriter output = new PrintWriter(
                        Files.newBufferedWriter(path, StandardCharsets.UTF_8))) {
                    result.write(output, writingFormatter);
                }
            } catch (InvalidPathException | IOException e) {
                throw new OutputException(
                        e.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }
    }
}
