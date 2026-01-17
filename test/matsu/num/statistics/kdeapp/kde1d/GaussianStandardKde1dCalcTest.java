/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.kde1d;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.Test.None;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link GaussianStandardKde1dCalc} のテスト.
 */
@RunWith(Enclosed.class)
final class GaussianStandardKde1dCalcTest {

    public static class 引数の検証に関する {

        @Test(expected = IllegalArgumentException.class)
        public void test_空ソースは例外() {
            new GaussianStandardKde1dCalc().runAndWrite(new double[0], new PrintWriter(System.out));
        }

        @Test(expected = IllegalArgumentException.class)
        public void test_NaNを含むと例外() {
            new GaussianStandardKde1dCalc().runAndWrite(
                    new double[] { 1d, Double.NaN },
                    new PrintWriter(System.out));
        }
    }

    public static class 結果出力のフォーマットに関する {

        private String[] lineStrings;

        @Before
        public void before_結果となる文字列を準備() {
            StringWriter sw = new StringWriter();
            new GaussianStandardKde1dCalc()
                    .runAndWrite(new double[] { 0d, 1d }, new PrintWriter(sw));

            lineStrings = sw.toString().split("\\R");
        }

        @Test
        public void test_結果は1件以上() {

            long length = Arrays.stream(lineStrings)
                    .filter(s -> !s.isBlank())
                    .count();

            assertThat((int) length, is(greaterThan(0)));
        }

        @Test(expected = None.class)
        public void test_結果はタブ区切り2カラム() {
            for (String s : lineStrings) {
                String[] values = s.split("\t");
                assertThat(values.length, is(2));

                // 例外がスローされないことを確かめる
                Double.parseDouble(values[0]);
                Double.parseDouble(values[1]);
            }
        }
    }
}
