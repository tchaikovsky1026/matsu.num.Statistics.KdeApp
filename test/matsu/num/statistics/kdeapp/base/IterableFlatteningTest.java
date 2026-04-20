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
                int size = ThreadLocalRandom.current().nextInt(100);
                int[] structureSize = new int[size];
                for (int i = 0; i < size; i++) {
                    // 各サイズは0から4の間とする
                    structureSize[i] = ThreadLocalRandom.current().nextInt(5);
                }

                List<List<String>> src = createStructuredString(structureSize);
                // テスト対象機能によりフラット化された Iterable を作成し, リストに詰め替える.
                List<String> result = iterableToList(IterableFlattening.flatten(src));
                // 素朴実装によりフラット化リストを作成する.
                List<String> expected = flatten(src);

                assertThat(result, is(expected));
            }
        }

        /**
         * ネストされた構造の文字列リストを生成する. <br>
         * 引数により, 内側のリストのサイズを指定する.
         * 
         * <p>
         * 例えば, {@code [3, 2, 4]} ならば, <br>
         * {@code [["aa", "bb", "cc"], ["dd", "ee"], ["ff", "gg", "hh", "ii"]]}
         * <br>
         * のようなネストリストが生成される. <br>
         * ただし, 文字列自体の仕様は規定しない.
         * </p>
         * 
         * @param structureSize 内側のリストサイズ
         * @return ネストリスト
         */
        private List<List<String>> createStructuredString(int[] structureSize) {
            List<List<String>> list = new ArrayList<>();
            int count = 0;

            for (int eachSize : structureSize) {
                List<String> inner = new ArrayList<>();
                for (int i = 0; i < eachSize; i++) {
                    inner.add(String.valueOf(count));
                    count++;
                }
                list.add(inner);
            }
            return list;
        }

        /** ネストリストをフラット化する素朴な実装. */
        private List<String> flatten(List<List<String>> src) {
            List<String> out = new ArrayList<>();
            for (List<String> e : src) {
                out.addAll(e);
            }
            return out;
        }

        /** Iterable を List に詰め替える. */
        private List<String> iterableToList(Iterable<String> iterable) {
            return StreamSupport.stream(iterable.spliterator(), false)
                    .toList();
        }
    }
}
