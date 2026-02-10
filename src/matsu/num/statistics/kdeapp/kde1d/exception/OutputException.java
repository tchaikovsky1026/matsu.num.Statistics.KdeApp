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
 * 出力に係る例外を表現する.
 * 
 * @author Matsuura Y.
 */
public class OutputException extends ApplicationException {

    private static final long serialVersionUID = 1L;
    /**
     * 引数なしコンストラクタ.
     */
    public OutputException() {
        super();
    }

    /**
     * メッセージを与えるコンストラクタ.
     * 
     * @param message message
     */
    public OutputException(String message) {
        super(message);
    }

    /**
     * 原因を与えるコンストラクタ.
     * 
     * @param cause cause
     */
    public OutputException(Throwable cause) {
        super(cause);
    }

    /**
     * メッセージと原因を与えるコンストラクタ.
     * 
     * @param message message
     * @param cause cause
     */
    public OutputException(String message, Throwable cause) {
        super(message, cause);
    }
}
