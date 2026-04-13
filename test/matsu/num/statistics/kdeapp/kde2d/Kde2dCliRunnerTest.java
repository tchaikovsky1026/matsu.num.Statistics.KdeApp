/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.kde2d;

import static matsu.num.statistics.kdeapp.kde2d.Commands.*;
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
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * {@link Kde2dCliRunner} のテスト.
 */
@RunWith(Enclosed.class)
final class Kde2dCliRunnerTest {

    public static final Class<?> TEST_CLASS = Kde2dCliRunner.class;

    @RunWith(Theories.class)
    public static class 処理の実行のテスト {

        private static final Path inputFile = Path.of("test/resources/kde2d test.txt");
        private static final Path inputFile_tab_separated = Path.of("test/resources/kde2d test separated_tab.txt");
        private static final Path outputDir = Path.of("test/output");
        private static final Path outputFile = outputDir.resolve("kde2d result.txt");

        private static final String inputSeparator = ",";
        private static final String commentChar = "#";

        @DataPoints
        public static String[][] args = {
                {
                        INPUT_FILE_PATH.commandString(), inputFile.toString(),
                        INPUT_SEPARATOR.commandString(), inputSeparator,
                        INPUT_COMMENT_PREFIX.commandString(), commentChar,
                        OUTPUT.commandString(), outputFile.toString(),
                        OUTPUT_FORMAT_TYPE.commandString(), "xyz",
                        OUTPUT_SEPARATOR.commandString(), ",",
                        OUTPUT_LABEL_PREFIX.commandString(), "//",
                        ECHO_OFF.commandString()
                },
                {
                        INPUT_FILE_PATH.commandString(), inputFile.toString(),
                        INPUT_SEPARATOR.commandString(), inputSeparator,
                        INPUT_COMMENT_PREFIX.commandString(), commentChar,
                        OUTPUT_NONE.commandString(),
                        OUTPUT_FORMAT_TYPE.commandString(), "matrix",
                        OUTPUT_SEPARATOR.commandString(), ",",
                        OUTPUT_NO_LABEL.commandString(),
                        ECHO_ON.commandString()
                },
                {
                        INPUT_FILE_PATH.commandString(), inputFile_tab_separated.toString()
                }
        };

        @Before
        public void before_ハッピーパスの準備() throws IOException {
            // アウトプットファイルが含まれるディレクトリの削除
            deleteDir(outputDir);
        }

        @Theory
        public void test_ハッピーパス(String[] arg) throws Exception {
            if (!Files.exists(inputFile)) {
                throw new AssertionError("does not exists: " + inputFile.toAbsolutePath());
            }

            PrintStream out = new PrintStream(OutputStream.nullOutputStream());
            PrintStream err = new PrintStream(OutputStream.nullOutputStream());

            assertThat(
                    new Kde2dCliRunner().run(
                            arg, out, err),
                    is(0));
        }
    }

    public static class エラーメッセージの表示 {

        @Test
        public void test_エラーメッセージ表示() {
            System.out.println(TEST_CLASS.getName() + ":");
            try {
                // ファイルがないパターン
                new Kde2dCliRunner().run(new String[] { INPUT_FILE_PATH.commandString(), "dummy.txt" });
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
