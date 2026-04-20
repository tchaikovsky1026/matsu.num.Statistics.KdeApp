/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.comp;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Properties;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.Test.None;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link StandardPropertyToResolvers} のテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class StandardPropertyToResolversTest {

    public static class プロパティロードのテスト {

        private final PropertyKey pKey1 = PropertyKey.of("param1");
        private final PropertyKey pKey2 = PropertyKey.of("param2");

        private final ResolverKey<String> rKey = ResolverKey.of("resolver", String.class);
        private final ResolverDesign<String> design = ResolverDesign.of(
                rKey, Set.of(pKey1, pKey2),
                p -> joinStr(p.find(pKey1).get(), p.find(pKey2).get()));

        private StandardPropertyToResolvers loader;

        @Before
        public void before_ローダーを構築する() {
            loader = new StandardPropertyToResolvers(Set.of(pKey1, pKey2), Set.of(design));
        }

        @Test
        public void test_成功パターン() {
            Properties p = new Properties();
            String s1 = "p1";
            String s2 = "p2";
            p.setProperty(pKey1.name(), s1);
            p.setProperty(pKey2.name(), s2);

            String result = loader.parse(p).require(rKey);
            assertThat(result, is(joinStr(s1, s2)));
        }

        @Test(expected = None.class)
        public void test_全く含まないパターン_例外無し() {
            Properties p = new Properties();

            loader.parse(p);
        }

        @Test(expected = IllegalArgumentException.class)
        public void test_不明なキーのパターン() {
            Properties p = new Properties();
            p.setProperty("unknownKey", "v");

            loader.parse(p);
        }

        @Test(expected = IllegalArgumentException.class)
        public void test_キーが足りないパターン() {
            Properties p = new Properties();
            p.setProperty(pKey2.name(), "p2");

            loader.parse(p);
        }

        private static String joinStr(String s1, String s2) {
            return s1 + "+" + s2;
        }
    }

    static PropertyKey newProp(String name) {
        return PropertyKey.of(name);
    }
}
