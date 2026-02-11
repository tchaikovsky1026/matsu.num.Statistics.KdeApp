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
 * {@link RuleElement} のテスト.
 */
@RunWith(Enclosed.class)
final class RuleElementTest {

    public static class ルール生成のテスト {

        @Test(expected = IllegalArgumentException.class)
        public void test_空ルールの生成は例外_singleOptional() {
            RuleElement.singleOptionalRule();
        }

        @Test(expected = IllegalArgumentException.class)
        public void test_空ルールの生成は例外_singleRequired() {
            RuleElement.singleRequiredRule();
        }
    }

    public static class SingleOptionalRuleのテスト {

        private CommandAssignmentRule rule;

        @Before
        public void before_ルールを構築() {
            // OUTPUTの2種とする
            rule = RuleElement.singleOptionalRule(
                    OUTPUT_FILE_PATH, OUTPUT_FORCE_FILE_PATH);
        }

        @Test(expected = None.class)
        public void test_指定されない場合は問題なし() {
            rule.validate(Set.of(INPUT_FILE_PATH));
            rule.validate(Set.of());
        }

        @Test(expected = None.class)
        public void test_片方の指定は問題なし() {
            rule.validate(Set.of(OUTPUT_FILE_PATH));
            rule.validate(Set.of(OUTPUT_FORCE_FILE_PATH));
        }

        @Test(expected = InvalidParameterException.class)
        public void test_両方の指定は例外() {
            rule.validate(Set.of(OUTPUT_FILE_PATH, OUTPUT_FORCE_FILE_PATH));
        }
    }

    public static class SingleRequiredRuleのテスト {

        private CommandAssignmentRule rule;

        @Before
        public void before_ルールを構築() {
            // OUTPUT2種とする
            rule = RuleElement.singleRequiredRule(
                    OUTPUT_FILE_PATH, OUTPUT_FORCE_FILE_PATH);
        }

        @Test(expected = InvalidParameterException.class)
        public void test_指定されない場合は例外() {
            rule.validate(Set.of());
        }

        @Test(expected = None.class)
        public void test_指定されれば問題ない() {
            rule.validate(Set.of(OUTPUT_FILE_PATH));
        }

        @Test(expected = InvalidParameterException.class)
        public void test_両方指定されれば例外() {
            rule.validate(Set.of(OUTPUT_FILE_PATH, OUTPUT_FORCE_FILE_PATH));
        }
    }
}
