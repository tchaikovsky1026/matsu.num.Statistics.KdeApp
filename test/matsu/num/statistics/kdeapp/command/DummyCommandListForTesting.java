/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.18
 */
package matsu.num.statistics.kdeapp.command;

/**
 * テスト用のダミーコマンド.
 * 
 * @author Matsuura Y.
 */
final class DummyCommandListForTesting {

    static final NoArgumentCommand DUMMY_NO_ARG_1 =
            NoArgumentCommand.of(
                    "DUMMY_NO_ARG_1", "--dummy-no-arg-1", "-d-na-1");

    static final NoArgumentCommand DUMMY_NO_ARG_2 =
            NoArgumentCommand.of(
                    "DUMMY_NO_ARG_2", "--dummy-no-arg-2", "-d-na-2");

    static final NoArgumentCommand DUMMY_NO_ARG_3 =
            NoArgumentCommand.of(
                    "DUMMY_NO_ARG_3", "--dummy-no-arg-3", "-d-na-3");

    static final ArgumentRequiringCommand<?> DUMMY_ARG_1 =
            ArgumentRequiringCommand.identifying(
                    "DUMMY_ARG_1", "--dummy-arg-1", "-d-a-1");

    static final ArgumentRequiringCommand<?> DUMMY_ARG_2 =
            ArgumentRequiringCommand.identifying(
                    "DUMMY_ARG_2", "--dummy-arg-2", "-d-a-2");

    static final ArgumentRequiringCommand<?> DUMMY_ARG_3 =
            ArgumentRequiringCommand.identifying(
                    "DUMMY_ARG_3", "--dummy-arg-3", "-d-a-3");

    private DummyCommandListForTesting() {
        // インスタンス化不可
    }
}
