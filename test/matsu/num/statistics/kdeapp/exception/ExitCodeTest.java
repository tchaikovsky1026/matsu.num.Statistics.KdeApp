/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.exception;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * {@link ExitCode} のテスト.
 */
@RunWith(Enclosed.class)
final class ExitCodeTest {

    @RunWith(Theories.class)
    public static class 用意された具象例外に関するテスト {

        @DataPoints
        public static List<ApplicationException> exceptions;

        @BeforeClass
        public static void before_検証する例外クラスのリストアップ() {
            exceptions = new ArrayList<>();
            exceptions.add(new IllegalParameterException());
            exceptions.add(new InputException());
            exceptions.add(new CalculationException());
            exceptions.add(new OutputException());
        }

        @Theory
        public void test_終了コードを検証する(ApplicationException e) {
            assertThat(e.getExitCode(), is(not(0)));
            assertThat(e.getExitCode(), is(not(1)));
            
            // 具象例外のため, 終了コードは2ではない
            assertThat(e.getExitCode(), is(not(2)));
        }
    }
}
