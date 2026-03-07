/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.base;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.StreamSupport;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link IterableFlattening} のテスト.
 */
@RunWith(Enclosed.class)
final class IterableFlatteningTest {

    public static class フラット化のテスト {

        @Test
        public void test_ランダムに構造化文字列を作成してテストする() {
            int iteration = 1000;

            for (int c = 0; c < iteration; c++) {
                // 各サイズは0から4の間とする
                int size = ThreadLocalRandom.current().nextInt(5);
                int[] structureSize = new int[size];
                for (int i = 0; i < size; i++) {
                    structureSize[i] = ThreadLocalRandom.current().nextInt(5);
                }

                List<List<String>> src = createStructuredString(structureSize);
                List<String> result = iterableToList(IterableFlattening.flatten(src));
                List<String> expected = flatten(src);

                assertThat(result, is(expected));
            }

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

        private List<String> flatten(List<List<String>> src) {
            List<String> out = new ArrayList<>();
            for (List<String> e : src) {
                out.addAll(e);
            }
            return out;
        }

        private List<String> iterableToList(Iterable<String> iterable) {
            return StreamSupport.stream(iterable.spliterator(), false)
                    .toList();
        }
    }
}
