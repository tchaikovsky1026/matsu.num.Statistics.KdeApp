/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.16
 */
package matsu.num.statistics.kdeapp.kde2d;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import matsu.num.statistics.kdeapp.comp.CommandTestingTool;

/**
 * {@link Commands} のテスト.
 */
@RunWith(Enclosed.class)
final class CommandsTest {

    public static Class<?> TEST_CLASS = Commands.class;

    public static class コマンドの文字列表現の列挙 {
        @Test
        public void test_文字表示() {
            System.out.println(TEST_CLASS.getName() + ": ");
            CommandTestingTool.stdout(Commands.getCommands());
            System.out.println();
        }
    }
}
