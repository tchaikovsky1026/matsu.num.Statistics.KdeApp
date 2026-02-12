/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.kde1d.command.rule;

import static matsu.num.statistics.kdeapp.kde1d.command.ArgumentRequiringCommand.*;
import static matsu.num.statistics.kdeapp.kde1d.command.rule.CommandAssignmentRule.*;

import org.junit.Test;
import org.junit.Test.None;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link CompositeRule} のテスト.
 */
@RunWith(Enclosed.class)
final class CompositeRuleTest {

    public static class 複合ルールの構築のテスト {

        @Test(expected = None.class)
        public void test_1ルール() {
            CompositeRule.composite(singleRequiredRule(INPUT_FILE_PATH));
        }

        @Test(expected = NullPointerException.class)
        public void test_1ルール_null() {
            CompositeRule.composite(null);
        }

        @Test(expected = None.class)
        public void test_3ルール() {
            CompositeRule.composite(
                    singleRequiredRule(INPUT_FILE_PATH),
                    singleRequiredRule(INPUT_FILE_PATH),
                    singleRequiredRule(INPUT_FILE_PATH));
        }

        @Test(expected = NullPointerException.class)
        public void test_3ルール_null() {
            CompositeRule.composite(
                    singleRequiredRule(INPUT_FILE_PATH),
                    singleRequiredRule(INPUT_FILE_PATH),
                    null);
        }

        @Test(expected = None.class)
        public void test_入れ子ルール() {
            CompositeRule.composite(
                    CompositeRule.composite(
                            singleRequiredRule(INPUT_FILE_PATH),
                            singleRequiredRule(INPUT_FILE_PATH)),
                    CompositeRule.composite(
                            singleRequiredRule(INPUT_FILE_PATH),
                            singleRequiredRule(INPUT_FILE_PATH)));
        }
    }
}
