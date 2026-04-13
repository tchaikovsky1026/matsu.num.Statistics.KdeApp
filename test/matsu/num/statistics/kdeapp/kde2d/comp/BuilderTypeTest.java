/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.kde2d.comp;

import org.junit.Test;
import org.junit.Test.None;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link BuilderType} のテスト.
 */
@RunWith(Enclosed.class)
final class BuilderTypeTest {

    public static class 文字列からの変換に関するテスト {

        @Test(expected = None.class)
        public void test_正当な場合に取得できることを確認() {
            for (BuilderType format : BuilderType.values()) {
                BuilderType.from(format.representation());
            }
        }

        @Test(expected = IllegalArgumentException.class)
        public void test_不当な場合は例外() {
            BuilderType.from("dummy");
        }

        @Test(expected = NullPointerException.class)
        public void test_nullは例外() {
            BuilderType.from(null);
        }
    }
}
