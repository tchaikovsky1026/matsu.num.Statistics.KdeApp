/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.6
 */
package matsu.num.statistics.kdeapp.kde1d.exception;

/**
 * パラメータ不正であることを表現する例外クラス.
 * 
 * @author Matsuura Y.
 */
public final class InvalidParameterException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    /**
     * 引数なしコンストラクタ.
     */
    public InvalidParameterException() {
        super();
    }

    /**
     * メッセージを与えるコンストラクタ.
     * 
     * @param message message
     */
    public InvalidParameterException(String message) {
        super(message);
    }

    /**
     * 原因を与えるコンストラクタ.
     * 
     * @param cause cause
     */
    public InvalidParameterException(Throwable cause) {
        super(cause);
    }

    /**
     * メッセージと原因を与えるコンストラクタ.
     * 
     * @param message message
     * @param cause cause
     */
    public InvalidParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}
