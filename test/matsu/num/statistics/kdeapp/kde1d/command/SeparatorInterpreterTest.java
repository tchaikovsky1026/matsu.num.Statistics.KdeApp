/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.kde1d.command;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import matsu.num.statistics.kdeapp.kde1d.command.SeparatorInterpreter.EscapeSequence;

/**
 * {@link SeparatorInterpreter} のテスト.
 */
@RunWith(Enclosed.class)
final class SeparatorInterpreterTest {

    public static final Class<?> TEST_CLASS = SeparatorInterpreter.class;

    public static class エスケープ以外の解釈のテスト {

        @Test
        public void test_通常の1文字() {
            assertThat(SeparatorInterpreter.from("a"), is('a'));
        }

        @Test
        public void test_全角1文字は不可() {
            assertThat(SeparatorInterpreter.from("あ"), is((Character) null));
        }

        @Test
        public void test_空白1文字() {
            assertThat(SeparatorInterpreter.from(" "), is(' '));
        }

        @Test
        public void test_バックスラッシュ1文字() {
            assertThat(SeparatorInterpreter.from("\\"), is('\\'));
        }

        @Test
        public void test_空文字は不正() {
            assertThat(SeparatorInterpreter.from(""), is((Character) null));
        }

        @Test
        public void test_2文字以上は不正() {
            assertThat(SeparatorInterpreter.from("//"), is((Character) null));
        }
    }

    public static class エスケープの解釈のテスト {

        @Test
        public void test_tab() {
            assertThat(SeparatorInterpreter.from("\\t"), is('\t'));
        }

        @Test
        public void test_バックスラッシュ2文字() {
            assertThat(SeparatorInterpreter.from("\\\\"), is('\\'));
        }
    }

    public static class エスケープシーケンスの列挙 {

        @Test
        public void test_列挙定数を表示する() {
            System.out.println(TEST_CLASS.getName());
            for (EscapeSequence e : EscapeSequence.values()) {
                System.out.println("%s: \"%s\"".formatted(e, e.stringValue()));
            }

            System.out.println();
        }
    }
}
