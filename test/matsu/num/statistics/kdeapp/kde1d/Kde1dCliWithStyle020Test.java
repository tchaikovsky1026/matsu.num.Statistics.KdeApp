/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.kde1d;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link Kde1dCliWithStyle020} のテスト.
 */
@RunWith(Enclosed.class)
final class Kde1dCliWithStyle020Test {

    public static final Class<?> TEST_CLASS = Kde1dCliWithStyle020.class;

    public static class 処理の実行のテスト {

        private final Path inputFile = Path.of("test/resources/kde1d test.txt");
        private final Path outputDir = Path.of("test/output");
        private final Path outputFile = outputDir.resolve("kde1d result.txt");

        @Before
        public void before_ハッピーパスの準備() throws IOException{
            // アウトプットファイルが含まれるディレクトリの削除
            deleteDir(outputDir);
        }

        @Test
        public void test_ハッピーパス() throws Exception {
            if (!Files.exists(inputFile)) {
                throw new AssertionError("does not exists: " + inputFile.toAbsolutePath());
            }

            PrintStream out = new PrintStream(OutputStream.nullOutputStream());
            PrintStream err = new PrintStream(OutputStream.nullOutputStream());

            assertThat(
                    new Kde1dCliWithStyle020().run(
                            new String[] {
                                    "-f", inputFile.toString(), "-out-f", outputFile.toString()
                            }, out, err),
                    is(0));
        }
    }

    public static class エラーメッセージの表示 {

        @Test
        public void test_エラーメッセージ表示() {
            System.out.println(TEST_CLASS.getName() + ":");
            try {
                // ファイルがないパターン
                new Kde1dCliWithStyle020().run(new String[] { "-f", "dummy.txt" });
            } catch (Exception e) {
                System.out.println(errorMessage(e));
            }
            System.out.println();
        }

        private String errorMessage(Exception e) {
            return e.getClass().getName() + ": " + e.getMessage();
        }
    }

    /**
     * 与えたパスのディレクトリまたはファイルを削除する.
     */
    private static void deleteDir(Path path) throws IOException {
        if (!Files.exists(path)) {
            return;
        }

        try (Stream<Path> eachPath = Files.walk(path)) {
            eachPath
                    .sorted(Comparator.reverseOrder()) // ← 深い順に
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
        } catch (UncheckedIOException e2) {
            throw e2.getCause();
        }
    }

}
