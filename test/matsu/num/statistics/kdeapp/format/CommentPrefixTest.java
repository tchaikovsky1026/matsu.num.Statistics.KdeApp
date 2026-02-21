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
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * {@link CommentPrefix} のテスト.
 */
@RunWith(Enclosed.class)
final class CommentPrefixTest {

    public static final Class<?> TEST_CLASS = CommentPrefix.class;

    public static class 等価性と比較のテスト {

        @Test
        public void test_同一文字列なら等価() {
            assertThat(CommentPrefix.of("a"), is(CommentPrefix.of("a")));
        }

        @Test
        public void test_異なる文字列なら等価でない() {
            assertThat(CommentPrefix.of("a"), not(CommentPrefix.of("b")));
        }

        @Test
        public void test_等価なら同等() {
            assertThat(CommentPrefix.of("a"), is(lessThanOrEqualTo(CommentPrefix.of("a"))));
            assertThat(CommentPrefix.of("a"), is(greaterThanOrEqualTo(CommentPrefix.of("a"))));
        }

        @Test
        public void test_文字列の比較と等しい() {
            assertThat(CommentPrefix.of("a"), is(lessThan(CommentPrefix.of("b"))));
        }
    }

    @RunWith(Theories.class)
    public static class 文字列判定のテスト {

        private final CommentPrefix commentPrefix = CommentPrefix.of("//");

        @DataPoints
        public static Fixture[] data = {
                Fixture.of("aa", false),
                Fixture.of("/", false),
                Fixture.of("//a", true),
                Fixture.of("", false),
                Fixture.of(" //", false)
        };

        @Theory
        public void test_文字列判定(Fixture f) {
            assertThat(commentPrefix.matches(f.string), is(f.match));
        }

        public static class Fixture {
            public final String string;
            public final boolean match;

            public Fixture(String string, boolean match) {
                super();
                this.string = string;
                this.match = match;
            }

            @Override
            public String toString() {
                return "Fixture(\"" + string + "\")";
            }

            public static Fixture of(String string, boolean match) {
                return new Fixture(string, match);
            }
        }
    }

    public static class 生成のテスト {

        @Test(expected = IllegalArgumentException.class)
        public void test_前に空白を含んではいけない() {
            CommentPrefix.of(" a");
        }

        @Test(expected = IllegalArgumentException.class)
        public void test_後に空白を含んではいけない() {
            CommentPrefix.of("a ");
        }

        @Test(expected = IllegalArgumentException.class)
        public void test_空文字は不正() {
            CommentPrefix.of("");
        }
    }

    public static class toString表示 {

        @Test
        public void test_toString() {
            System.out.println(TEST_CLASS.getName());
            System.out.println(CommentPrefix.of("//"));
            System.out.println();
        }
    }
}
