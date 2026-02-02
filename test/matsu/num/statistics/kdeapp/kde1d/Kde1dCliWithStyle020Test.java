/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.kde1d;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

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

        @Test
        public void test_ハッピーパス() throws Exception {
            Path p = Path.of("test/resources/kde1d test.txt");
            if (!Files.exists(p)) {
                throw new AssertionError("does not exists: " + p.toAbsolutePath());
            }

            PrintStream out = new PrintStream(OutputStream.nullOutputStream());
            PrintStream err = new PrintStream(OutputStream.nullOutputStream());

            assertThat(
                    new Kde1dCliWithStyle020().run(new String[] { "-f", p.toString() }, out, err),
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
}
