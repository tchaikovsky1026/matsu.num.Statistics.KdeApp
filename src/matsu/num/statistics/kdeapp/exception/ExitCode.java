/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.27
 */
package matsu.num.statistics.kdeapp.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 例外に対応する終了コードの対応を扱う.
 * 
 * @author Matsuura Y.
 */
enum ExitCode {
    ILLEGAL_PARAMETER(IllegalParameterException.class, 3),
    INPUT(InputException.class, 4),
    CALCULATION(CalculationException.class, 5),
    OUTPUT(OutputException.class, 6);

    static final int EXIT_CODE_OF_ANONYMOUS_EXCEPTION = 2;

    private final Class<? extends ApplicationException> exceptionType;
    private final int exitCode;

    private ExitCode(
            Class<? extends ApplicationException> exceptionType,
            int exitCode) {
        this.exitCode = exitCode;
        this.exceptionType = exceptionType;
    }

    /**
     * この列挙型クラスが管理している定数インスタンスをデータベースとして,
     * 例外クラスから終了コードへ変換する.
     * 
     * <p>
     * 管理下でない場合, {@link #EXIT_CODE_OF_ANONYMOUS_EXCEPTION} の値を返す.
     * </p>
     * 
     * @param exceptionType 例外クラス
     * @return 終了コード
     * @throws NullPointerException 引数がnull
     */
    static int getExitCode(Class<? extends ApplicationException> exceptionType) {
        ExitCode ec = ClassEnumMapperHolder.MAPPER.get(
                Objects.requireNonNull(exceptionType));
        return ec == null
                ? EXIT_CODE_OF_ANONYMOUS_EXCEPTION
                : ec.exitCode;
    }

    private static final class ClassEnumMapperHolder {
        static final Map<Class<? extends ApplicationException>, ExitCode> MAPPER;

        static {
            MAPPER = new HashMap<>();
            for (ExitCode ec : ExitCode.values()) {
                if (Objects.nonNull(
                        MAPPER.put(Objects.requireNonNull(ec.exceptionType), ec))) {
                    throw new AssertionError("duplicate exception type");
                }
            }
        }
    }
}
