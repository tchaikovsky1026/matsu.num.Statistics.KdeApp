/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.10
 */
package matsu.num.statistics.kdeapp.kde1d.exception;

/**
 * 計算に係る例外を表現する.
 * 
 * @author Matsuura Y.
 */
public class CalculationException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    /**
     * 引数なしコンストラクタ.
     */
    public CalculationException() {
        super();
    }

    /**
     * メッセージを与えるコンストラクタ.
     * 
     * @param message message
     */
    public CalculationException(String message) {
        super(message);
    }

    /**
     * 原因を与えるコンストラクタ.
     * 
     * @param cause cause
     */
    public CalculationException(Throwable cause) {
        super(cause);
    }

    /**
     * メッセージと原因を与えるコンストラクタ.
     * 
     * @param message message
     * @param cause cause
     */
    public CalculationException(String message, Throwable cause) {
        super(message, cause);
    }
}
