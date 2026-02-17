/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.kde1d.command;

import static matsu.num.statistics.kdeapp.kde1d.command.ArgumentRequiringCommand.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Collection;
import java.util.Optional;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * {@link ArgumentRequiringCommand} のテスト.
 */
@RunWith(Enclosed.class)
final class ArgumentRequiringCommandTest {

    public static final Class<?> TEST_CLASS = ArgumentRequiringCommand.class;

    public static class オプションコマンドの集合生成に関するテスト {

        @Test
        public void test_サイズが1以上を確かめ文字列列挙() {
            assertThat(values().size(), is(greaterThan(0)));

            System.out.println(TEST_CLASS.getName());
            values().stream()
                    .forEach(System.out::println);
            System.out.println();
        }
    }

    @RunWith(Theories.class)
    public static class オプションコマンドの文字列解釈の正常系テスト {

        @DataPoints
        public static Collection<ArgumentRequiringCommand<?>> commands = values();

        @Theory
        public void test_各コマンドについて解釈を実行(ArgumentRequiringCommand<?> command) {
            assertThat(
                    interpret(command.commandString()).get(),
                    is(command));
            for (String commandStr : command.expressions()) {
                assertThat(
                        interpret(commandStr).get(),
                        is(command));
            }
        }
    }

    public static class オプションコマンドの文字列解釈の異常系テスト {

        @Test(expected = NullPointerException.class)
        public void test_nullの場合() {
            interpret(null);
        }

        @Test
        public void test_未定義の場合() {
            assertThat(
                    interpret("--unknown"),
                    is(Optional.empty()));
        }
    }
}
