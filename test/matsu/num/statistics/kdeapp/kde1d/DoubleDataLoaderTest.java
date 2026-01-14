/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.kde1d;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link DoubleDataLoader} のテスト.
 */
@RunWith(Enclosed.class)
final class DoubleDataLoaderTest {

    public static class 生成に関する {

        @Test(expected = NullPointerException.class)
        public void test_パーサーはnullではいけない() {
            new DoubleDataLoader(null);
        }
    }

    public static class 値の抽出に関する {

        private DoubleDataLoader loader;

        @Before
        public void before_ローダーの準備() {
            loader = new DoubleDataLoader(new DoubleLineParser(List.of("#")));
        }

        @Test
        public void test_正常系() throws IOException {
            double[] data = loader.load(
                    () -> List.of(
                            // U+3000は全角スペース
                            " 1.0 ", " -1d ", "# dummy", " \u3000", "100").stream());

            assertThat(data, is(new double[] { 1d, -1d, 100d }));
        }

        @Test(expected = IOException.class)
        public void test_異常系() throws IOException {
            loader.load(
                    () -> List.of(
                            " 1.0 ", "dummy").stream());
        }
    }
}
