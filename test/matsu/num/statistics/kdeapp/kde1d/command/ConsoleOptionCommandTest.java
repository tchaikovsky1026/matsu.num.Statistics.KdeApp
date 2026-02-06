/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.statistics.kdeapp.kde1d.command;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link ConsoleOptionCommand} のテスト.
 */
@RunWith(Enclosed.class)
final class ConsoleOptionCommandTest {

    public static class サブクラスの重複の検証 {

        @Test
        public void test_サブクラスのコマンド文字列表現が重複しないことを確認() {
            List<String> argCommandExpressions = ArgumentRequiringCommand.allExpressions();
            List<String> noArgCommandExpressions = NoArgumentCommand.allExpressions();

            Set<String> merged = new HashSet<>(argCommandExpressions);
            merged.addAll(noArgCommandExpressions);

            int overlapCount = argCommandExpressions.size() +
                    noArgCommandExpressions.size() - merged.size();

            assertThat("Subclass-command overlapped count", overlapCount, is(0));
        }
    }
}
