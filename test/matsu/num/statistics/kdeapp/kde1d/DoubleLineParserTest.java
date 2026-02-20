/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.kde1d;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.OptionalDouble;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link DoubleLineParser} のテスト.
 */
@RunWith(Enclosed.class)
final class DoubleLineParserTest {

    public static class 生成のテスト {

        @Test(expected = NullPointerException.class)
        public void test_nullを渡す() {
            new DoubleLineParser(null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void test_空文字を含む() {
            new DoubleLineParser(" ");
        }
    }

    public static class 値抽出のテスト {

        private final DoubleLineParser parser = new DoubleLineParser("//");

        @Test
        public void test_スラッシュエスケープ() {
            assertThat(parser.parse("// dummy"), is(OptionalDouble.empty()));
        }

        @Test(expected = NullPointerException.class)
        public void test_値の取得に失敗_null() {
            parser.parse(null);
        }

        @Test(expected = NumberFormatException.class)
        public void test_値の取得に失敗() {
            parser.parse(" dummy ");
        }

        @Test
        public void test_値の取得() {
            assertThat(parser.parse(" 1.0d "), is(OptionalDouble.of(1d)));
        }
    }
}
