/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.base;

import static matsu.num.statistics.kdeapp.base.StandardPropertyLoader.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.nio.file.Path;
import java.util.Properties;

import org.junit.Test;
import org.junit.Test.None;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link StandardPropertyLoader} のテスト.
 */
@RunWith(Enclosed.class)
final class StandardPropertyLoaderTest {

    public static class ファイルロード型プロパティのテスト {

        @Test(expected = IllegalStateException.class)
        public void test_プロパティが存在しない場合は例外() {
            fromFile(Path.of("test/resources/dummy.properties"))
                    .compute();
        }

        @Test(expected = None.class)
        public void test_プロパティが存在する場合での値検証() {
            Properties p = fromFile(Path.of("test/resources/test-properties.properties"))
                    .compute();

            assertThat(p.getProperty("param1"), is("s1"));
            assertThat(p.getProperty("param0"), is(""));
        }
    }
}
