/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.kde1d;

import static matsu.num.statistics.kdeapp.kde1d.NoArgumentCommand.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link NoArgumentCommand} のテスト.
 */
@RunWith(Enclosed.class)
final class NoArgumentCommandTest {

    public static class オプションコマンドの集合生成に関するテスト {

        @SuppressWarnings("deprecation")
        @Test
        public void test_DUMMY_NO_ARGを含むことを確かめる() {
            assertThat(values(), containsInRelativeOrder(DUMMY_NO_ARG));
        }
    }

    public static class オプションコマンドの文字列解釈のテスト {

        @Test(expected = NullPointerException.class)
        public void test_nullは不可() {
            interpret(null);
        }

        @SuppressWarnings("deprecation")
        @Test
        public void test_ダミーコマンド() {
            assertThat(
                    interpret("--dummy-no-arg").get(),
                    is(DUMMY_NO_ARG));
        }

        @Test
        public void test_未定義の場合() {
            assertThat(
                    interpret("--unknown"),
                    is(Optional.empty()));
        }
    }
}
