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
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.junit.Test.None;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link GaussianStandardKde1dCalculator} のテスト.
 */
@RunWith(Enclosed.class)
final class WritableKde1dResultTest {

    public static class 結果出力のフォーマットに関する_ラベル無し {

        private final char separator = ',';

        private WritableKde1dResult result;
        private WritingFormatter formatter;

        @Before
        public void before_結果を準備() {
            result = new GaussianStandardKde1dCalculator()
                    .calc(new double[] { 0d, 1d });
        }

        @Before
        public void before_フォーマッターの準備() {
            // カンマ区切り, スラッシュがラベル
            formatter = new WritingFormatter.Builder()
                    .setSeparator(separator)
                    .disableLabel()
                    .build();
        }

        @Test
        public void test_結果は1件以上() {
            String[] lineStrings = resultToStrings(result, formatter);

            int length = (int) Arrays.stream(lineStrings)
                    .filter(s -> !s.isBlank())
                    .count();

            assertThat(length, is(greaterThan(0)));
        }

        @Test(expected = None.class)
        public void test_結果の検証() {
            String[] lineStrings = resultToStrings(result, formatter);

            // すべての行がデータ
            for (String s : lineStrings) {
                String[] values = s.split(String.valueOf(separator));
                assertThat(values.length, is(2));

                // 例外がスローされないことを確かめる
                Double.parseDouble(values[0]);
                Double.parseDouble(values[1]);
            }
        }
    }

    public static class 結果出力のフォーマットに関する_ラベル有り {

        private final char separator = ',';
        private final char labelHeader = '/';

        private WritableKde1dResult result;
        private WritingFormatter formatter;

        @Before
        public void before_結果を準備() {
            result = new GaussianStandardKde1dCalculator()
                    .calc(new double[] { 0d, 1d });
        }

        @Before
        public void before_フォーマッターの準備() {
            // カンマ区切り, スラッシュがラベル
            formatter = new WritingFormatter.Builder()
                    .setSeparator(separator)
                    .enableLabel(labelHeader)
                    .build();
        }

        @Test
        public void test_結果は1件以上() {
            String[] lineStrings = resultToStrings(result, formatter);

            int length = (int) Arrays.stream(lineStrings)
                    .filter(s -> !s.isBlank())
                    .count();

            // ラベル行を抜いたlength
            length--;

            assertThat(length, is(greaterThan(0)));
        }

        @Test(expected = None.class)
        public void test_結果の検証() {
            String[] lineStrings = resultToStrings(result, formatter);

            Iterator<String> ite = Arrays.stream(lineStrings).iterator();

            // 最初の行はヘッダ
            assertThat(ite.next().charAt(0), is(labelHeader));

            // 2行目以降はデータ
            for (; ite.hasNext();) {
                String s = ite.next();
                String[] values = s.split(String.valueOf(separator));
                assertThat(values.length, is(2));

                // 例外がスローされないことを確かめる
                Double.parseDouble(values[0]);
                Double.parseDouble(values[1]);
            }
        }
    }

    private static String[] resultToStrings(
            WritableKde1dResult result, WritingFormatter parameter) {
        StringWriter sw = new StringWriter();
        result.write(new PrintWriter(sw), parameter);
        return sw.toString().split("\\R");
    }
}
