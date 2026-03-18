/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.kde2d.help;

import org.junit.Test;
import org.junit.Test.None;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link Kde1dHelpDisplayRunner } のテスト.
 */
@RunWith(Enclosed.class)
final class Kde2dHelpDisplayRunnerTest {

    public static final Class<?> TEST_CLASS = Kde2dHelpDisplayRunner.class;

    public static class ランナーの実行 {

        @Test(expected = None.class)
        public void test_ヘルプの表示() {
            System.out.println(TEST_CLASS.getName());
            System.out.println("=================");
            new Kde2dHelpDisplayRunner().run();
            System.out.println("=================");
            System.out.println();
        }
    }
}
