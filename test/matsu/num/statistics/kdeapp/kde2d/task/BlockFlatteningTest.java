/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.kde2d.task;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * {@link BlockFlattening} のテスト.
 */
@RunWith(Enclosed.class)
final class BlockFlatteningTest {

    @RunWith(Theories.class)
    public static class フラット化のテスト {

        @DataPoints
        public static int[] gaps = { 0, 1, 2 };

        @Theory
        public void test_ランダムに構造化文字列を作成してテストする(int gap) {

            BlockFlattening blockFlattening = new BlockFlattening(gap);

            // 各サイズは2とする
            int size = 2;
            int[] structureSize = new int[size];
            for (int i = 0; i < size; i++) {
                structureSize[i] = 2;
            }

            List<List<String>> src = createStructuredString(structureSize);
            List<String> result = iterableToList(blockFlattening.apply(src));
            List<String> expected = flattenWithBlank(src, gap);

            assertThat(result, is(expected));

        }

        private List<List<String>> createStructuredString(int[] structureSize) {
            List<List<String>> list = new ArrayList<>();

            for (int eachSize : structureSize) {
                List<String> inner = new ArrayList<>();
                for (int i = 0; i < eachSize; i++) {
                    inner.add(String.valueOf(i));
                }
                list.add(inner);
            }
            return list;
        }

        private List<String> flattenWithBlank(List<List<String>> src, int gap) {
            List<String> out = new ArrayList<>();

            for (Iterator<List<String>> ite = src.iterator(); ite.hasNext();) {
                out.addAll(ite.next());
                if (ite.hasNext()) {
                    for (int i = 0; i < gap; i++) {
                        out.add("");
                    }
                }
            }
            return out;
        }

        private List<String> iterableToList(Iterable<String> iterable) {
            return StreamSupport.stream(iterable.spliterator(), false)
                    .toList();
        }
    }
}
