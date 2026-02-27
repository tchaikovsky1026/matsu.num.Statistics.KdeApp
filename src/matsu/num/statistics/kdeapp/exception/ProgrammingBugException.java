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

/**
 * プログラミング上のバグを表現するクラス.
 * 
 * @author Matsuura Y.
 */
public class ProgrammingBugException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 引数なしコンストラクタ.
     */
    public ProgrammingBugException() {
        super();
    }

    /**
     * メッセージを与えるコンストラクタ.
     * 
     * @param message message
     */
    public ProgrammingBugException(String message) {
        super(message);
    }

    /**
     * 原因を与えるコンストラクタ.
     * 
     * @param cause cause
     */
    public ProgrammingBugException(Throwable cause) {
        super(cause);
    }

    /**
     * メッセージと原因を与えるコンストラクタ.
     * 
     * @param message message
     * @param cause cause
     */
    public ProgrammingBugException(String message, Throwable cause) {
        super(message, cause);
    }
}
