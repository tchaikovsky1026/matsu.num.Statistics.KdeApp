/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.17
 */
package matsu.num.statistics.kdeapp.kde2d.exception;

/**
 * kde2dのアプリケーションに係る例外を扱うパッケージ.
 * 
 * @author Matsuura Y.
 */
public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 引数なしコンストラクタ.
     */
    public ApplicationException() {
        super();
    }

    /**
     * メッセージを与えるコンストラクタ.
     * 
     * @param message message
     */
    public ApplicationException(String message) {
        super(message);
    }

    /**
     * 原因を与えるコンストラクタ.
     * 
     * @param cause cause
     */
    public ApplicationException(Throwable cause) {
        super(cause);
    }

    /**
     * メッセージと原因を与えるコンストラクタ.
     * 
     * @param message message
     * @param cause cause
     */
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
