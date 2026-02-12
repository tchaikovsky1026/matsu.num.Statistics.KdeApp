/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.kde1d.command;

import static matsu.num.statistics.kdeapp.kde1d.command.NoArgumentCommand.*;
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

        @Test
        public void test_ECHO_OFFを含むことを確かめる() {
            assertThat(values(), containsInRelativeOrder(ECHO_OFF));
        }
    }

    public static class オプションコマンドの文字列解釈のテスト {

        @Test(expected = NullPointerException.class)
        public void test_nullは不可() {
            interpret(null);
        }

        @Test
        public void test_echoOff() {
            assertThat(
                    interpret("--echo-off").get(),
                    is(ECHO_OFF));
        }

        @Test
        public void test_未定義の場合() {
            assertThat(
                    interpret("--unknown"),
                    is(Optional.empty()));
        }
    }
}
