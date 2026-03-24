/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.23
 */
package matsu.num.statistics.kdeapp.command;

import matsu.num.statistics.kdeapp.base.DummySupplierForTesting;
import matsu.num.statistics.kdeapp.config.PropertyKey;

/**
 * テスト用のダミーコマンド.
 * 
 * @author Matsuura Y.
 */
public final class DummyCommandListForTesting {

    public static final PropertyKey<String> DUMMY_1 = PropertyKey.of("dummy-1", String.class);
    public static final PropertyKey<String> DUMMY_2 = PropertyKey.of("dummy-2", String.class);
    public static final PropertyKey<String> DUMMY_3 = PropertyKey.of("dummy-3", String.class);

    public static final NoArgumentCommand<?> DUMMY_NO_ARG_1 =
            NoArgumentCommand.of(
                    "DUMMY_NO_ARG_1", DUMMY_1, DummySupplierForTesting.instance(""),
                    "--dummy-no-arg-1", "-d-na-1");

    public static final NoArgumentCommand<?> DUMMY_NO_ARG_2 =
            NoArgumentCommand.of(
                    "DUMMY_NO_ARG_2", DUMMY_2, DummySupplierForTesting.instance(""),
                    "--dummy-no-arg-2", "-d-na-2");

    public static final NoArgumentCommand<?> DUMMY_NO_ARG_3 =
            NoArgumentCommand.of(
                    "DUMMY_NO_ARG_3", DUMMY_3, DummySupplierForTesting.instance(""),
                    "--dummy-no-arg-3", "-d-na-3");

    public static final ArgumentRequiringCommand<?> DUMMY_ARG_1 =
            ArgumentRequiringCommand.identifying(
                    "DUMMY_ARG_1", DUMMY_1, "--dummy-arg-1", "-d-a-1");

    public static final ArgumentRequiringCommand<?> DUMMY_ARG_2 =
            ArgumentRequiringCommand.identifying(
                    "DUMMY_ARG_2", DUMMY_2, "--dummy-arg-2", "-d-a-2");

    public static final ArgumentRequiringCommand<?> DUMMY_ARG_3 =
            ArgumentRequiringCommand.identifying(
                    "DUMMY_ARG_3", DUMMY_3, "--dummy-arg-3", "-d-a-3");

    private DummyCommandListForTesting() {
        // インスタンス化不可
    }
}
