/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.kde2d;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import matsu.num.statistics.kdeapp.format.Separator;

/**
 * {@link DoubleColumnDoubleLineParser} のテスト.
 */
@RunWith(Enclosed.class)
final class DoubleColumnDoubleLineParserTest {

    public static class 生成のテスト {
        private final Separator separator = Separator.from(",");

        @Test(expected = NullPointerException.class)
        public void test_nullを渡す() {
            new DoubleColumnDoubleLineParser(null, separator);
        }

        @Test(expected = IllegalArgumentException.class)
        public void test_空文字を含む() {
            new DoubleColumnDoubleLineParser(" ", separator);
        }
    }

    public static class 値抽出のテスト {

        private final DoubleColumnDoubleLineParser parser =
                new DoubleColumnDoubleLineParser(
                        "//",
                        Separator.from(","));

        @Test
        public void test_スラッシュエスケープ() {
            assertThat(parser.parse("// dummy"), is(Optional.empty()));
        }

        @Test(expected = NullPointerException.class)
        public void test_値の取得に失敗_null() {
            parser.parse(null);
        }

        @Test(expected = NumberFormatException.class)
        public void test_値の取得に失敗_数値でない() {
            parser.parse(" dummy ");
        }

        @Test(expected = NumberFormatException.class)
        public void test_値の取得に失敗_カラム数() {
            parser.parse("1.0");
        }

        @Test
        public void test_値の取得() {
            assertThat(
                    parser.parse(" 1.0d  , 2.0d").get(), is(
                            new double[] { 1d, 2d }));
        }
    }
}
