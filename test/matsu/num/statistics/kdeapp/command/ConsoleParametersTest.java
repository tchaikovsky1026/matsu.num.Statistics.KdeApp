/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.command;

import static matsu.num.statistics.kdeapp.command.DummyCommandListForTesting.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.statistics.kdeapp.base.DummySupplierForTesting;
import matsu.num.statistics.kdeapp.exception.IllegalParameterException;

/**
 * {@link ConsoleParameters} のテスト.
 */
@RunWith(Enclosed.class)
final class ConsoleParametersTest {

    public static class 解釈器の生成に関するテスト {

        @Test(expected = IllegalArgumentException.class)
        public void test_コマンドに同一の文字列表現がある場合は例外() {

            // ArgCommandとコマンド文字列が重複するNoArgCommand
            NoArgumentCommand<?> command = NoArgumentCommand.of(
                    "DUMMY_4", DUMMY_PROPERTY, DummySupplierForTesting.instance(),
                    DUMMY_ARG_1.commandString());

            ConsoleParameters.Interpreter.of(
                    Set.of(command),
                    Set.of(DUMMY_ARG_1),
                    CommandAssignmentRule.nullRule());
        }
    }

    @RunWith(Theories.class)
    public static class 解釈の正常系に関するテスト {

        @DataPoints
        public static List<String[]> argsList;

        private ConsoleParameters.Interpreter interpreter;

        @BeforeClass
        public static void before_引数リストの作成() {
            argsList = new ArrayList<>();

            argsList.add(new String[] {});
            argsList.add(
                    new String[] {
                            DUMMY_ARG_1.commandString(), "test.txt" });
            argsList.add(
                    new String[] {
                            DUMMY_ARG_1.commandString(), "test.txt",
                            DUMMY_NO_ARG_1.commandString() });
            argsList.add(
                    new String[] {
                            DUMMY_NO_ARG_1.commandString(),
                            DUMMY_ARG_1.commandString(), "test.txt" });
            argsList.add(
                    new String[] {
                            DUMMY_ARG_1.commandString(), "test.txt",
                            DUMMY_ARG_2.commandString(), "\t" });
        }

        @Before
        public void before_解釈器の用意() {
            interpreter = ConsoleParameters.Interpreter.of(
                    Set.of(DUMMY_NO_ARG_1, DUMMY_NO_ARG_2, DUMMY_NO_ARG_3),
                    Set.of(DUMMY_ARG_1, DUMMY_ARG_2, DUMMY_ARG_3),
                    CommandAssignmentRule.nullRule());
        }

        @Theory
        public void test_正常系の網羅テスト(String[] args) {
            // 例外がスローされなければOK
            interpreter.interpret(args);
        }
    }

    public static class 解釈の異常系に関するテスト {

        private ConsoleParameters.Interpreter interpreter;

        @Before
        public void before_解釈器の用意() {
            interpreter = ConsoleParameters.Interpreter.of(
                    Set.of(DUMMY_NO_ARG_1, DUMMY_NO_ARG_2, DUMMY_NO_ARG_3),
                    Set.of(DUMMY_ARG_1, DUMMY_ARG_2, DUMMY_ARG_3),
                    CommandAssignmentRule.nullRule());
        }

        @Test(expected = IllegalParameterException.class)
        public void test_パラメータに重複がある場合は例外_引数有り() {
            String[] args = {
                    DUMMY_ARG_1.commandString(), "test.txt",
                    DUMMY_ARG_1.commandString(), "test.txt" };
            interpreter.interpret(args);
        }

        @Test(expected = IllegalParameterException.class)
        public void test_パラメータに重複がある場合は例外_引数無し() {
            String[] args = { DUMMY_NO_ARG_1.commandString(), DUMMY_NO_ARG_1.commandString() };
            interpreter.interpret(args);
        }
    }

    public static class 解釈結果の取得に関するテスト {

        /*
         * dummy-arg-1 と dummy-noarg-1 を設定
         */

        private final String file = "test.txt";

        private ConsoleParameters interpretedParameters;

        @Before
        public void before_解釈の構築() {
            ConsoleParameters.Interpreter interpreter = ConsoleParameters.Interpreter.of(
                    Set.of(DUMMY_NO_ARG_1, DUMMY_NO_ARG_2, DUMMY_NO_ARG_3),
                    Set.of(DUMMY_ARG_1, DUMMY_ARG_2, DUMMY_ARG_3),
                    CommandAssignmentRule.nullRule());

            // インプットファイル と dummy-no-arg を設定
            String[] args = {
                    DUMMY_ARG_1.commandString(), file, DUMMY_NO_ARG_1.commandString()
            };
            interpretedParameters = interpreter.interpret(args);
        }

        @Test
        public void test_da1は設定ずみ() {
            assertThat(interpretedParameters.valueOf(DUMMY_ARG_1).get(), is(file));
        }

        @Test
        public void test_da2は設定されていない() {
            assertThat(interpretedParameters.valueOf(DUMMY_ARG_2), is(Optional.empty()));
        }

        @Test
        public void test_dna1は設定済み() {
            assertThat(interpretedParameters.contains(DUMMY_NO_ARG_1), is(true));
        }

        @Test
        public void test_dna2は設定されていない() {
            assertThat(interpretedParameters.contains(DUMMY_NO_ARG_2), is(false));
        }
    }
}
