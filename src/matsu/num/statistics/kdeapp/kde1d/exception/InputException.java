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
 * 読み込みに係る例外を表現する.
 * 
 * @author Matsuura Y.
 */
public class InputException extends ApplicationException {

    private static final long serialVersionUID = 1L;
    /**
     * 引数なしコンストラクタ.
     */
    public InputException() {
        super();
    }

    /**
     * メッセージを与えるコンストラクタ.
     * 
     * @param message message
     */
    public InputException(String message) {
        super(message);
    }

    /**
     * 原因を与えるコンストラクタ.
     * 
     * @param cause cause
     */
    public InputException(Throwable cause) {
        super(cause);
    }

    /**
     * メッセージと原因を与えるコンストラクタ.
     * 
     * @param message message
     * @param cause cause
     */
    public InputException(String message, Throwable cause) {
        super(message, cause);
    }
}
