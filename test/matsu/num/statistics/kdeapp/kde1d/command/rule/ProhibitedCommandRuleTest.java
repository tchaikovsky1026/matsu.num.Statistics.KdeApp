/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.kde1d.command.rule;

import static matsu.num.statistics.kdeapp.kde1d.command.ArgumentRequiringCommand.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.Test.None;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import matsu.num.statistics.kdeapp.kde1d.exception.InvalidParameterException;

/**
 * {@link ProhibitedCommandRule} のテスト.
 */
@RunWith(Enclosed.class)
final class ProhibitedCommandRuleTest {

    public static class ルールのテスト {

        private CommandAssignmentRule rule;

        @Before
        public void before_ルールを構築() {
            // LABEL_HEADERを禁止とする
            rule = ProhibitedCommandRule.of(LABEL_HEADER);
        }

        @Test(expected = None.class)
        public void test_含まれない場合は問題なし() {
            rule.validate(Set.of(INPUT_FILE_PATH));
            rule.validate(Set.of());
        }

        @Test(expected = InvalidParameterException.class)
        public void test_含む場合は例外() {
            rule.validate(Set.of(LABEL_HEADER, OUTPUT_FORCE_FILE_PATH));
        }
    }
}
