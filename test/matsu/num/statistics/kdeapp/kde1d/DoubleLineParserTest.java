/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.kde1d;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.List;
import java.util.OptionalDouble;

import org.junit.Test;
import org.junit.Test.None;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link DoubleLineParser} のテスト.
 */
@RunWith(Enclosed.class)
final class DoubleLineParserTest {

    public static class 生成のテスト {

        @Test(expected = None.class)
        public void test_空コレクションを渡すことは可能() {
            new DoubleLineParser(List.of());
        }

        @Test(expected = NullPointerException.class)
        public void test_nullを渡す() {
            new DoubleLineParser(null);
        }

        @Test(expected = NullPointerException.class)
        public void test_nullを含む() {
            new DoubleLineParser(List.of("#", null));
        }

        @Test(expected = IllegalArgumentException.class)
        public void test_空文字を含む() {
            new DoubleLineParser(List.of(" "));
        }
    }

    public static class 値抽出のテスト {

        // 2種のエスケープを持つパーサー
        private final DoubleLineParser parser = new DoubleLineParser(List.of("//", "#"));

        @Test
        public void test_スラッシュエスケープ() {
            assertThat(parser.parse("// dummy"), is(OptionalDouble.empty()));
        }

        @Test
        public void test_シャープエスケープ() {
            assertThat(parser.parse("## dummy"), is(OptionalDouble.empty()));
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
