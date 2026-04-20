/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.comp;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.function.Function;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link LookupResult} のテスト.
 */
@RunWith(Enclosed.class)
final class LookupResultTest {

    public static class コンテナ生成に関するテスト {

        @Test(expected = NullPointerException.class)
        public void test_nullをofに渡してはいけない() {
            // NullPointerExをスローするはず
            LookupResult.of(new Object(), null);
        }

        @Test
        public void test_nullをofNullableに渡すと空() {
            assertThat(LookupResult.ofNullable(new Object(), null).isEmpty(), is(true));
        }

        @Test
        public void test_emptyによる生成() {
            assertThat(LookupResult.empty(new Object()).isEmpty(), is(true));
        }

        @Test(expected = NullPointerException.class)
        public void test_keyがnullは例外() {
            // NullPointerExをスローするはず
            LookupResult.ofNullable(null, new Object());
        }
    }

    public static class getに関するテスト {

        private final Object key = "KEY";
        private final String value = "VALUE";

        private final LookupResult<String> con = LookupResult.of(key, value);
        private final LookupResult<String> conEmpty = LookupResult.empty(key);

        @Test
        public void test_値を持つ場合の取得() {
            assertThat(getHelper(con), is(value));
        }

        @Test(expected = IllegalStateException.class)
        public void test_値を持たない場合の取得失敗() {
            getHelper(conEmpty);
        }

        @Test(expected = NullPointerException.class)
        public void test_値を持たず例外のnull生成の場合の例外() {
            conEmpty.getOrThrow(s -> null);
        }
    }

    public static class mapに関するテスト {

        private final Object key = "KEY";
        private final String value = "VALUE";

        private final LookupResult<String> con = LookupResult.of(key, value);
        private final LookupResult<String> conEmpty = LookupResult.empty(key);

        @Test
        public void test_値を持つ場合のマッピング() {
            assertThat(getHelper(mapHelper(con, s -> s.length())), is(value.length()));
        }

        @Test
        public void test_空の場合のマッピング() {
            assertThat(mapHelper(conEmpty, s -> s.length()).isEmpty(), is(true));
        }

        @Test(expected = NullPointerException.class)
        public void test_値を持ち変換がnull生成の場合() {
            mapHelper(con, s -> null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void test_値を持ち変換に失敗した場合() {
            mapHelper(con, s -> {
                throw new RuntimeException("inner");
            });
        }
    }

    /**
     * {@link LookupResult#getOrThrow(Function)}
     * に転送するヘルパ. <br>
     * 例外生成は IllegalStateException.
     * 
     * @throws IllegalStateException 空の場合
     */
    private static <V> V getHelper(LookupResult<V> result) {
        return result.getOrThrow(IllegalStateException::new);
    }

    /**
     * {@link LookupResult#mapOrThrow(Function, Function)}
     * に転送するヘルパ. <br>
     * 例外生成は IllegalArgumentException.
     * 
     * @throws IllegalArgumentException 変換に失敗した場合
     */
    private static <V, V2> LookupResult<V2> mapHelper(
            LookupResult<V> result,
            Function<? super V, ? extends V2> mapper) {
        return result.mapOrThrow(mapper, IllegalArgumentException::new);
    }
}
