/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.format;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link DelimitedLineParser} のテスト.
 */
@RunWith(Enclosed.class)
final class DelimitedLineParserTest {

    public static class 値の抽出に関する {

        private DelimitedLineParser parser;

        @Before
        public void before_パーサーの用意() {
            parser = new DelimitedLineParser(2, Separator.from(","), CommentPrefix.of("#"));
        }

        @Test
        public void test_Doubleへの変換() {
            String line = "1.0,2d";
            Double[] result =
                    parser.apply(line, Double::parseDouble, Double.class)
                            .get();

            Double[] expected = { 1d, 2d };

            assertThat(result, arrayContaining(expected));
        }

        @Test
        public void test_Doubleへの変換_後ろに空があり() {
            String line = "1.0,3d,";
            Double[] result =
                    parser.apply(line, Double::parseDouble, Double.class)
                            .get();

            Double[] expected = { 1d, 3d };

            assertThat(result, arrayContaining(expected));
        }
    }
}
