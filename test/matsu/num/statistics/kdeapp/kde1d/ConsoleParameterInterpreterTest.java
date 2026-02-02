/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.kde1d;

import static matsu.num.statistics.kdeapp.kde1d.ConsoleOptionCommand.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * {@link ConsoleParameterInterpreter} のテスト.
 */
@RunWith(Enclosed.class)
final class ConsoleParameterInterpreterTest {

    @RunWith(Theories.class)
    public static class 解釈生成の正常系に関するテスト {

        @DataPoints
        public static List<String[]> argsList;

        @BeforeClass
        public static void before_引数リストの作成() {
            argsList = new ArrayList<>();

            argsList.add(new String[] {});
            argsList.add(new String[] { "-f", "test.txt" });
            argsList.add(new String[] { "-f", "test.txt", "--dummy-no-arg" });
            argsList.add(new String[] { "--dummy-no-arg", "-f", "test.txt" });
            argsList.add(new String[] { "-f", "test.txt", "-sep", "\t" });
        }

        @Theory
        public void test_正常系の網羅テスト(String[] args) {
            // 例外がスローされなければOK
            ConsoleParameterInterpreter.from(args);
        }
    }

    public static class 解釈結果の取得に関するテスト {

        /*
         * インプットファイル と dummy-no-arg を設定
         */

        private final String file = "test.txt";

        private ConsoleParameterInterpreter interpretation;

        @Before
        public void before_解釈の構築() {
            // インプットファイル と dummy-no-arg を設定
            String[] args = {
                    "-f", file, "--dummy-no-arg"
            };
            interpretation = ConsoleParameterInterpreter.from(args);
        }

        @Test
        public void test_inputFileは設定ずみ() {
            assertThat(interpretation.valueOf(INPUT_FILE_PATH).get().get(), is(file));
        }

        @Test
        public void test_commentCharは設定されていない() {
            assertThat(interpretation.valueOf(COMMENT_CHAR), is(Optional.empty()));
        }

        @SuppressWarnings("deprecation")
        @Test
        public void test_dummyNoArgは設定済み() {
            assertThat(interpretation.valueOf(DUMMY_NO_ARG).get(), is(Optional.empty()));
        }
    }
}
