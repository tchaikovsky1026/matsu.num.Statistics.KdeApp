/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.format;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import matsu.num.statistics.kdeapp.format.Separator.EscapeSequence;

/**
 * {@link Separator} のテスト.
 */
@RunWith(Enclosed.class)
final class SeparatorTest {

    public static final Class<?> TEST_CLASS = Separator.class;

    public static class 等価性と比較のテスト {

        @Test
        public void test_同一charなら等価() {
            assertThat(Separator.from("a"), is(Separator.from("a")));
        }

        @Test
        public void test_異なるcharなら等価でない() {
            assertThat(Separator.from("a"), not(Separator.from("b")));
        }

        @Test
        public void test_等価なら同等() {
            assertThat(Separator.from("a"), is(lessThanOrEqualTo(Separator.from("a"))));
            assertThat(Separator.from("a"), is(greaterThanOrEqualTo(Separator.from("a"))));
        }

        @Test
        public void test_charの比較と等しい() {
            assertThat(Separator.from("a"), is(lessThan(Separator.from("b"))));
        }
    }

    public static class エスケープ以外の解釈のテスト {

        @Test
        public void test_通常の1文字() {
            assertThat(Separator.from("a").charValue(), is('a'));
        }

        @Test
        public void test_空白1文字() {
            assertThat(Separator.from(" ").charValue(), is(' '));
        }

        @Test
        public void test_バックスラッシュ1文字() {
            assertThat(Separator.from("\\").charValue(), is('\\'));
        }

        @Test(expected = IllegalArgumentException.class)
        public void test_全角1文字は不可() {
            Separator.from("あ");
        }

        @Test(expected = IllegalArgumentException.class)
        public void test_空文字は不正() {
            Separator.from("");
        }

        @Test(expected = IllegalArgumentException.class)
        public void test_2文字以上は不正() {
            Separator.from("//");
        }
    }

    public static class エスケープの解釈のテスト {

        @Test
        public void test_tab() {
            assertThat(Separator.from("\\t"), is(Separator.from("\t")));
        }

        @Test
        public void test_バックスラッシュ2文字() {
            assertThat(Separator.from("\\\\"), is(Separator.from("\\")));
        }

        @Test(expected = IllegalArgumentException.class)
        public void test_不正なエスケープシーケンス() {
            Separator.from("\\x");
        }
    }

    public static class エスケープシーケンスの列挙 {

        @Test
        public void test_列挙定数を表示する() {
            System.out.println(TEST_CLASS.getName());
            for (EscapeSequence e : EscapeSequence.values()) {
                System.out.println("%s: \"%s\"".formatted(e, e.representation()));
            }
            System.out.println();
        }
    }
}
