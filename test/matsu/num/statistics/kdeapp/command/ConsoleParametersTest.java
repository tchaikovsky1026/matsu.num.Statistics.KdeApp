/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.command;

import static matsu.num.statistics.kdeapp.command.DummyCommandListForTesting.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Test.None;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.statistics.kdeapp.base.DummySupplierForTesting;
import matsu.num.statistics.kdeapp.config.ConfigProperty;
import matsu.num.statistics.kdeapp.config.PropertyKey;
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
                    "DUMMY_4", PropertyKey.of(String.class), DummySupplierForTesting.instance(""),
                    DUMMY_ARG_1.commandString());

            ConsoleParameterInterpreter.of(
                    Set.of(command),
                    Set.of(DUMMY_ARG_1),
                    CommandAssignmentRule.nullRule());
        }
    }

    @RunWith(Theories.class)
    public static class 解釈の正常系に関するテスト {

        @DataPoints
        public static List<String[]> argsList;

        private ConsoleParameterInterpreter interpreter;

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
            interpreter = ConsoleParameterInterpreter.of(
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

        private ConsoleParameterInterpreter interpreter;

        @Before
        public void before_解釈器の用意() {
            interpreter = ConsoleParameterInterpreter.of(
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
         * dummy-arg-1 と dummy-noarg-2 を設定
         */

        private final String file = "test.txt";

        private ConfigProperty property;

        @Before
        public void before_解釈の構築() {
            ConsoleParameterInterpreter interpreter = ConsoleParameterInterpreter.of(
                    Set.of(DUMMY_NO_ARG_1, DUMMY_NO_ARG_2, DUMMY_NO_ARG_3),
                    Set.of(DUMMY_ARG_1, DUMMY_ARG_2, DUMMY_ARG_3),
                    CommandAssignmentRule.nullRule());

            // インプットファイル と dummy-no-arg を設定
            String[] args = {
                    DUMMY_ARG_1.commandString(), file, DUMMY_NO_ARG_2.commandString()
            };
            property = interpreter.interpret(args);
        }

        @Test(expected = None.class)
        public void test_d1は設定ずみ() {
            property.get(DUMMY_1);
        }

        @Test(expected = None.class)
        public void test_d2は設定ずみ() {
            property.get(DUMMY_2);
        }

        @Test(expected = NoSuchElementException.class)
        public void test_d3は設定されていない() {
            property.get(DUMMY_3);
        }
    }
}
