/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.kde1d;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link GaussianStandardKde1dCalculator} のテスト.
 */
@RunWith(Enclosed.class)
final class GaussianStandardKde1dCalculatorTest {

    public static class 引数の検証に関する {

        @Test(expected = IllegalArgumentException.class)
        public void test_空ソースは例外() {
            new GaussianStandardKde1dCalculator().calc(new double[0]);
        }

        @Test(expected = IllegalArgumentException.class)
        public void test_NaNを含むと例外() {
            new GaussianStandardKde1dCalculator().calc(new double[] { 1d, Double.NaN });
        }
    }
}
