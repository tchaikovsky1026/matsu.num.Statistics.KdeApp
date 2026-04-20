/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.comp;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.Properties;
import java.util.Set;
import java.util.function.BinaryOperator;

import org.junit.Before;
import org.junit.Test;
import org.junit.Test.None;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import matsu.num.statistics.kdeapp.comp.PropertyContainer.Builder;
import matsu.num.statistics.kdeapp.comp.PropertyContainer.StdApiReader;

/**
 * {@link PropertyContainer} のテスト.
 */
@RunWith(Enclosed.class)
final class PropertyContainerTest {

    public static final Class<?> TEST_CLASS = PropertyContainer.class;

    public static class ビルダへの登録のテスト {

        private final PropertyKey[] keys = {
                newProp("param1"),
                newProp("param2"),
                newProp("param3")
        };

        private Builder builder;

        @Before
        public void before_ビルダを生成する() {
            builder = new Builder(Set.of(keys));
        }

        @Test(expected = None.class)
        public void test_param1は存在() {
            builder.put("param1", "v");
        }

        @Test(expected = IllegalArgumentException.class)
        public void test_param4は存在しない() {
            builder.put("param4", "v");
        }

        @Test(expected = NullPointerException.class)
        public void test_keyがnullは不可() {
            builder.put(null, "v");
        }

        @Test(expected = NullPointerException.class)
        public void test_valueがnullは不可() {
            builder.put("param1", null);
        }
    }

    public static class 標準APIの利用に関する {

        private final PropertyKey[] keys = {
                newProp("param1"),
                newProp("param2"),
                newProp("param3")
        };

        private StdApiReader reader;

        @Before
        public void before_リーダーを生成する() {
            reader = new StdApiReader(Set.of(keys));
        }

        @Test(expected = None.class)
        public void test_プロパティAPIから生成_成功パターン() {
            Properties properties = new Properties();
            properties.setProperty("param1", "v1");
            properties.setProperty("param3", "v3");

            // コンバートは成功する
            reader.convert(properties);
        }

        @Test(expected = IllegalArgumentException.class)
        public void test_プロパティAPIから生成_失敗パターン() {
            Properties properties = new Properties();
            properties.setProperty("param1", "v1");
            // 不明なkey
            properties.setProperty("param4", "v4");

            // コンバートは失敗する
            reader.convert(properties);
        }
    }

    public static class Resolver生成に関するテスト {

        private final PropertyKey pKey1 = newProp("key1");
        private final PropertyKey pKey2 = newProp("key2");
        private final PropertyKey pKey3 = newProp("key3");
        private final ResolverKey<Object> rKey = newRes();

        private final BinaryOperator<String> sf =
                (s1, s2) -> "[" + s1 + ", " + s2 + "]";
        private final ResolverDesign<Object> design =
                ResolverDesign.of(
                        rKey, Set.of(pKey1, pKey2),
                        map -> {
                            String p1 = map.find(pKey1).get();
                            String p2 = map.find(pKey2).get();
                            return sf.apply(p1, p2);
                        });

        private PropertyContainer.Builder builder;

        @Before
        public void before_プロパティコンテナビルダの用意() {
            builder = new Builder(Set.of(pKey1, pKey2, pKey3));
        }

        @Test
        public void test_発火しないパターン() {
            builder.put(pKey3.name(), "p3");
            var container = builder.build();
            container.toResolvers(Set.of(design));
        }

        @Test(expected = IllegalArgumentException.class)
        public void test_発火するが失敗パターン() {
            builder.put(pKey1.name(), "p1");
            builder.put(pKey3.name(), "p3");
            var container = builder.build();
            container.toResolvers(Set.of(design));
        }

        @Test
        public void test_発火して成功パターン() {
            String s1 = "p1";
            String s2 = "p2";
            builder.put(pKey1.name(), s1);
            builder.put(pKey2.name(), s2);
            var container = builder.build();

            String result = container.toResolvers(Set.of(design))
                    .require(rKey)
                    .toString();
            String expected = sf.apply(s1, s2);
            assertThat(result, is(expected));
        }
    }

    static PropertyKey newProp(String name) {
        return PropertyKey.of(name);
    }

    static ResolverKey<Object> newRes() {
        return ResolverKey.of("resolver", Object.class);
    }
}
