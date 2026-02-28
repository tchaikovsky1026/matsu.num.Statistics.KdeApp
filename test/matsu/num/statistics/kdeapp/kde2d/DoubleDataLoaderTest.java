/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.kde2d;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import matsu.num.statistics.kdeapp.format.CommentPrefix;
import matsu.num.statistics.kdeapp.format.DelimitedLineParser;
import matsu.num.statistics.kdeapp.format.Separator;

/**
 * {@link DoubleDataLoader} のテスト.
 */
@RunWith(Enclosed.class)
final class DoubleDataLoaderTest {

    public static class 値の抽出に関する {

        private DoubleDataLoader loader;

        @Before
        public void before_ローダーの準備() {
            loader = new DoubleDataLoader(
                    new DelimitedLineParser(2, Separator.from(","), CommentPrefix.of("#")));
        }

        @Test
        public void test_正常系() throws IOException {
            double[][] data = loader.load(
                    () -> List.of(
                            // U+3000は全角スペース
                            " 1.0,2.0 ", " -1d,3d ", "# dummy", " \u3000", "100,5").stream());

            assertThat(data, is(new double[][] { { 1d, -1d, 100d }, { 2d, 3d, 5d } }));
        }

        @Test(expected = IOException.class)
        public void test_異常系() throws IOException {
            loader.load(
                    () -> List.of(
                            " 1.0,1d ", "dummy").stream());
        }
    }
}
