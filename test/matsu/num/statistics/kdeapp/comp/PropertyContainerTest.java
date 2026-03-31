/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.31
 */
package matsu.num.statistics.kdeapp.comp;

import java.util.Properties;
import java.util.Set;

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

    static PropertyKey newProp(String name) {
        return PropertyKey.of(name);
    }
}
