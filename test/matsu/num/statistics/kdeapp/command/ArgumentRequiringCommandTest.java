/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.command;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import matsu.num.statistics.kdeapp.exception.IllegalParameterException;

/**
 * {@link ArgumentRequiringCommand} のテスト.
 */
@RunWith(Enclosed.class)
final class ArgumentRequiringCommandTest {

    public static class メソッドconvertArgに関する {

        @Test(expected = IllegalParameterException.class)
        public void test_異常系ではパラメータエラーをスロー() {
            DummyCommandListForTesting.SINGLE_CHAR.convertArg("aa");
        }
    }
}
