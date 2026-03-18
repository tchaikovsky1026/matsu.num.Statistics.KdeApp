/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.help;

import static matsu.num.statistics.kdeapp.command.DummyCommandListForTesting.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.Test.None;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link CategorizationFormatter} のテスト.
 */
@RunWith(Enclosed.class)
final class CategorizationFormatterTest {

    public static final Class<?> TEST_CLASS = CategorizationFormatter.class;

    private static final CommandCategory CAT_DUMMY_X = CommandCategory.from("DUMMY_X");
    private static final CommandCategory CAT_DUMMY_Y = CommandCategory.from("DUMMY_Y");

    @Ignore
    public static class フォーマット結果を生成できるかのテストと表示 {

        private List<CommandDescription> commands;

        @Before
        public void before_コマンドリストを作成する() {
            commands = new ArrayList<>();
            commands.add(CommandDescription.of(DUMMY_NO_ARG_1, "   no arg 1 ", CAT_DUMMY_X));
            commands.add(CommandDescription.of(DUMMY_ARG_3, "ARG", "arg 3 ", CAT_DUMMY_X));
            commands.add(CommandDescription.of(DUMMY_NO_ARG_3, "   no arg 3 ", CAT_DUMMY_Y));
            commands.add(CommandDescription.of(DUMMY_ARG_1, "ARG", "arg 1 ", CAT_DUMMY_Y));
        }

        @Test(expected = None.class)
        public void test_フォーマット結果の表示() {
            List<String> formatted = new CategorizationFormatter().format(commands);

            System.out.println(TEST_CLASS.getName());
            System.out.println("=================");
            for (String s : formatted) {
                System.out.println(s);
            }
            System.out.println("=================");
            System.out.println();
        }
    }
}
