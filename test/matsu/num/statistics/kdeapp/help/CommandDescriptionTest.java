/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.help;

import static java.util.Comparator.*;
import static matsu.num.statistics.kdeapp.command.DummyCommandListForTesting.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.Test.None;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.statistics.kdeapp.command.ArgumentRequiringCommand;
import matsu.num.statistics.kdeapp.command.NoArgumentCommand;

/**
 * {@link CommandDescription} のテスト.
 */
@RunWith(Enclosed.class)
final class CommandDescriptionTest {

    public static class 生成に関する {

        private final CommandCategory category = CommandCategory.from("Dummy");

        @Test(expected = None.class)
        public void test_説明にnullを渡しても良い() {
            CommandDescription.of(DUMMY_NO_ARG_1, null, category);
            CommandDescription.of(DUMMY_ARG_1, "ARG", null, category);
        }

        @Test(expected = IllegalArgumentException.class)
        public void test_argNameはブランクではいけない() {
            CommandDescription.of(DUMMY_ARG_1, "  ", null, category);
        }

        @Test(expected = IllegalArgumentException.class)
        public void test_argNameは空白を含んではいけない() {
            CommandDescription.of(DUMMY_ARG_1, "a b", null, category);
        }
    }

    @RunWith(Theories.class)
    public static class 文字列列挙に関する {

        private final CommandCategory category = CommandCategory.from("Dummy");

        @DataPoints
        public static NoArgumentCommand<?>[] noArgCommands = {
                DUMMY_NO_ARG_1,
                DUMMY_NO_ARG_2,
                DUMMY_NO_ARG_3
        };

        @DataPoints
        public static ArgumentRequiringCommand<?>[] argCommands = {
                DUMMY_ARG_1,
                DUMMY_ARG_2,
                DUMMY_ARG_3
        };

        @Theory
        public void test_usageSyntaxが文字列の多い順_noArg(
                NoArgumentCommand<?> command) {
            List<String> list = CommandDescription.of(command, "", category).getUsageSyntaxes();
            assertThat(isDescending(list, comparingInt(String::length)), is(true));
        }

        @Theory
        public void test_usageSyntaxが文字列の多い順_arg(
                ArgumentRequiringCommand<?> command) {
            List<String> list = CommandDescription.of(command, "ARG", "", category).getUsageSyntaxes();
            assertThat(isDescending(list, comparingInt(String::length)), is(true));
        }
    }

    /**
     * 与えられたIterableが降順であることを確認する.
     * 
     * @param <T> 型
     * @param iterable Iterable
     * @param comp Comparator
     * @return 降順ならtrue
     * @throws NullPointerException null
     */
    static <T> boolean isDescending(
            Iterable<? extends T> iterable,
            Comparator<? super T> comp) {

        Iterator<? extends T> it = iterable.iterator();

        if (!it.hasNext()) {
            return true; // 空はOK
        }

        T prev = it.next();

        while (it.hasNext()) {
            T curr = it.next();
            if (comp.compare(prev, curr) < 0) {
                return false;
            }
            prev = curr;
        }

        return true;
    }
}
