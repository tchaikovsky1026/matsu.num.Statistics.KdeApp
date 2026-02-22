/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.format;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link CommentLineFilter} のテスト.
 */
@RunWith(Enclosed.class)
final class CommentLineFilterTest {

    public static class 値抽出のテスト {

        private final CommentLineFilter lineFilter = new CommentLineFilter(CommentPrefix.of("//"));

        @Test
        public void test_スラッシュエスケープ() {
            assertThat(lineFilter.apply("// dummy"), is(Optional.empty()));
        }

        @Test
        public void test_前に空白がある場合はエスケープされない() {
            assertThat(lineFilter.apply("  // dummy"), not(Optional.empty()));
        }

        @Test
        public void test_値の取得() {
            assertThat(lineFilter.apply(" abcd "), is(Optional.of(" abcd")));
        }
    }
}
