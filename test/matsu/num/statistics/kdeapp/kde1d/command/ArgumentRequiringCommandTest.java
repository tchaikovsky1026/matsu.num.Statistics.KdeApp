/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.kde1d.command;

import static matsu.num.statistics.kdeapp.kde1d.command.ArgumentRequiringCommand.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import matsu.num.statistics.kdeapp.kde1d.exception.InvalidParameterException;

/**
 * {@link ArgumentRequiringCommand} のテスト.
 */
@RunWith(Enclosed.class)
final class ArgumentRequiringCommandTest {

    public static class オプションコマンドの集合生成に関するテスト {

        @Test
        public void test_INPUT_FILE_PATHを含むことを確かめる() {
            assertThat(values(), containsInRelativeOrder(INPUT_FILE_PATH));
        }
    }

    public static class オプションコマンドの文字列解釈のテスト {

        @Test(expected = NullPointerException.class)
        public void test_nullは不可() {
            interpret(null);
        }

        @Test
        public void test_セパレータコマンド() {
            assertThat(
                    interpret("--separator").get(),
                    is(SEPARATOR));
            assertThat(
                    interpret("-sep").get(),
                    is(SEPARATOR));
        }

        @Test
        public void test_未定義の場合() {
            assertThat(
                    interpret("--unknown"),
                    is(Optional.empty()));
        }
    }

    public static class オプションコマンドのコンバータのバリデーションテスト {

        @Test(expected = InvalidParameterException.class)
        public void test_セパレータは空文字は不可() {
            SEPARATOR.convertArg("");
        }
    }
}
