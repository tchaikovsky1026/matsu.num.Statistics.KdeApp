/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.17
 */
package matsu.num.statistics.kdeapp.exception;

/**
 * パラメータ不正であることを表現する例外クラス.
 * 
 * @author Matsuura Y.
 */
public final class IllegalParameterException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    /**
     * 引数なしコンストラクタ.
     */
    public IllegalParameterException() {
        super();
    }

    /**
     * メッセージを与えるコンストラクタ.
     * 
     * @param message message
     */
    public IllegalParameterException(String message) {
        super(message);
    }

    /**
     * 原因を与えるコンストラクタ.
     * 
     * @param cause cause
     */
    public IllegalParameterException(Throwable cause) {
        super(cause);
    }

    /**
     * メッセージと原因を与えるコンストラクタ.
     * 
     * @param message message
     * @param cause cause
     */
    public IllegalParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}
