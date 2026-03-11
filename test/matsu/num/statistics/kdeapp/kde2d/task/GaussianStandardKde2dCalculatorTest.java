/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.kde2d.task;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import matsu.num.statistics.kdeapp.exception.CalculationException;

/**
 * {@link GaussianStandardKde2dCalculator} のテスト.
 */
@RunWith(Enclosed.class)
final class GaussianStandardKde2dCalculatorTest {

    public static class 引数の検証に関する {

        @Test(expected = CalculationException.class)
        public void test_空ソースは例外() {
            new GaussianStandardKde2dCalculator().calc(new double[2][0]);
        }

        @Test(expected = CalculationException.class)
        public void test_NaNを含むと例外() {
            new GaussianStandardKde2dCalculator().calc(new double[][] { { 1d, Double.NaN }, { 1d, 2d } });
        }
    }
}
