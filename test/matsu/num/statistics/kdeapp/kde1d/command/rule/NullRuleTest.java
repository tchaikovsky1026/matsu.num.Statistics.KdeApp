/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.kde1d.command.rule;

import java.util.Set;

import org.junit.Test;
import org.junit.Test.None;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import matsu.num.statistics.kdeapp.kde1d.command.ArgumentRequiringCommand;

/**
 * {@link NullRule} のテスト.
 */
@RunWith(Enclosed.class)
final class NullRuleTest {

    public static class 実行に関するテスト {

        @Test(expected = None.class)
        public void test_実行できるかを確かめる() {
            NullRule.INSTANCE.validate(Set.of(ArgumentRequiringCommand.INPUT_FILE_PATH));
        }
    }
}
