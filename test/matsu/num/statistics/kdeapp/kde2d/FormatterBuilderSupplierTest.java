/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.kde2d;

import org.junit.Test;
import org.junit.Test.None;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link FormatterBuilderSupplier} のテスト.
 */
@RunWith(Enclosed.class)
final class FormatterBuilderSupplierTest {

    public static class 文字列からの変換に関するテスト {

        @Test(expected = None.class)
        public void test_正当な場合に取得できることを確認() {
            for (FormatterBuilderSupplier format : FormatterBuilderSupplier.values()) {
                FormatterBuilderSupplier.from(format.representation());
            }
        }

        @Test(expected = IllegalArgumentException.class)
        public void test_不当な場合は例外() {
            FormatterBuilderSupplier.from("dummy");
        }

        @Test(expected = NullPointerException.class)
        public void test_nullは例外() {
            FormatterBuilderSupplier.from(null);
        }
    }
}
