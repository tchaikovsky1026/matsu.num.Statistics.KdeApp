/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.kde1d;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link ConsoleOptionCommand} のテスト.
 */
@RunWith(Enclosed.class)
final class ConsoleOptionCommandTest {

    public static class オプションコマンドの文字列解釈のテスト {

        @Test(expected = NullPointerException.class)
        public void test_nullは不可() {
            ConsoleOptionCommand.interpret(null);
        }

        @Test
        public void test_セパレータコマンド() {
            assertThat(
                    ConsoleOptionCommand.interpret("--separator").get(),
                    is(ConsoleOptionCommand.SEPARATOR));
            assertThat(
                    ConsoleOptionCommand.interpret("-sep").get(),
                    is(ConsoleOptionCommand.SEPARATOR));
        }

        @Test
        public void test_未定義の場合() {
            assertThat(
                    ConsoleOptionCommand.interpret("--unknown"),
                    is(Optional.empty()));
        }
    }
}
