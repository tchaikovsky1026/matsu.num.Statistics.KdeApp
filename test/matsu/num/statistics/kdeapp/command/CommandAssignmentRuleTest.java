/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.command;

import static matsu.num.statistics.kdeapp.command.CommandAssignmentRule.*;
import static matsu.num.statistics.kdeapp.command.DummyCommandListForTesting.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.Test.None;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import matsu.num.statistics.kdeapp.exception.IllegalParameterException;

/**
 * {@link CommandAssignmentRule} のテスト.
 */
@RunWith(Enclosed.class)
final class CommandAssignmentRuleTest {

    public static final class NullRuleTest {

        @Test(expected = None.class)
        public void test_実行できるかを確かめる() {
            nullRule().validate(
                    Set.of(DUMMY_NO_ARG_1));
        }
    }

    public static final class ProhibitedCommandRuleTest {

        private CommandAssignmentRule rule;

        @Before
        public void before_ルールを構築() {
            // LABEL_HEADERを禁止とする
            rule = prohibitedCommandRule(DUMMY_NO_ARG_1);
        }

        @Test(expected = None.class)
        public void test_含まれない場合は問題なし() {
            rule.validate(Set.of(DUMMY_NO_ARG_2));
            rule.validate(Set.of());
        }

        @Test(expected = IllegalParameterException.class)
        public void test_含む場合は例外() {
            rule.validate(Set.of(DUMMY_NO_ARG_1, DUMMY_NO_ARG_2));
        }
    }

    public static class SingleOptionalRuleTest {

        private CommandAssignmentRule rule;

        @Before
        public void before_ルールを構築() {
            rule = singleOptionalRule(
                    DUMMY_NO_ARG_1, DUMMY_NO_ARG_2);
        }

        @Test(expected = None.class)
        public void test_指定されない場合は問題なし() {
            rule.validate(Set.of(DUMMY_NO_ARG_3));
            rule.validate(Set.of());
        }

        @Test(expected = None.class)
        public void test_片方の指定は問題なし() {
            rule.validate(Set.of(DUMMY_NO_ARG_1));
            rule.validate(Set.of(DUMMY_NO_ARG_2, DUMMY_NO_ARG_3));
        }

        @Test(expected = IllegalParameterException.class)
        public void test_両方の指定は例外() {
            rule.validate(Set.of(DUMMY_NO_ARG_1, DUMMY_NO_ARG_2));
        }
    }

    public static class SingleRequiredRuleTest {

        private CommandAssignmentRule rule;

        @Before
        public void before_ルールを構築() {
            rule = singleRequiredRule(
                    DUMMY_NO_ARG_1, DUMMY_NO_ARG_2);
        }

        @Test(expected = IllegalParameterException.class)
        public void test_指定されない場合は例外() {
            rule.validate(Set.of());
        }

        @Test(expected = None.class)
        public void test_指定されれば問題ない() {
            rule.validate(Set.of(DUMMY_NO_ARG_1));
            rule.validate(Set.of(DUMMY_NO_ARG_1, DUMMY_NO_ARG_3));
        }

        @Test(expected = IllegalParameterException.class)
        public void test_両方指定されれば例外() {
            rule.validate(Set.of(DUMMY_NO_ARG_1, DUMMY_NO_ARG_2));
        }
    }
}
