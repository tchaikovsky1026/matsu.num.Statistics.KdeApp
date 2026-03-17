/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.17
 */
package matsu.num.statistics.kdeapp.command;

/**
 * テスト用のダミーコマンド.
 * 
 * @author Matsuura Y.
 */
public final class DummyCommandListForTesting {

    public static final NoArgumentCommand DUMMY_NO_ARG_1 =
            NoArgumentCommand.of(
                    "DUMMY_NO_ARG_1", "--dummy-no-arg-1", "-d-na-1");

    public static final NoArgumentCommand DUMMY_NO_ARG_2 =
            NoArgumentCommand.of(
                    "DUMMY_NO_ARG_2", "--dummy-no-arg-2", "-d-na-2");

    public static final NoArgumentCommand DUMMY_NO_ARG_3 =
            NoArgumentCommand.of(
                    "DUMMY_NO_ARG_3", "--dummy-no-arg-3", "-d-na-3");

    public static final ArgumentRequiringCommand<?> DUMMY_ARG_1 =
            ArgumentRequiringCommand.identifying(
                    "DUMMY_ARG_1", "--dummy-arg-1", "-d-a-1");

    public static final ArgumentRequiringCommand<?> DUMMY_ARG_2 =
            ArgumentRequiringCommand.identifying(
                    "DUMMY_ARG_2", "--dummy-arg-2", "-d-a-2");

    public static final ArgumentRequiringCommand<?> DUMMY_ARG_3 =
            ArgumentRequiringCommand.identifying(
                    "DUMMY_ARG_3", "--dummy-arg-3", "-d-a-3");

    /**
     * コンバートは恒等変換だが, 文字列の長さが1でなければならない.
     */
    static final ArgumentRequiringCommand<String> SINGLE_CHAR =
            ArgumentRequiringCommand.of(
                    "SINGLE_CHAR", String.class,
                    s -> {
                        if (s.length() != 1) {
                            throw new IllegalArgumentException("not single char");
                        }
                        return s;
                    },
                    "--single-char", "-sc");

    private DummyCommandListForTesting() {
        // インスタンス化不可
    }
}
