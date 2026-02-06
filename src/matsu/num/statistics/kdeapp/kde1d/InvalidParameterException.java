/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.6
 */
package matsu.num.statistics.kdeapp.kde1d;

/**
 * パラメータ不正であることを表現する例外クラス.
 * 
 * @author Matsuura Y.
 */
public final class InvalidParameterException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    /**
     * 引数なしコンストラクタ.
     */
    public InvalidParameterException() {
        super();
    }

    /**
     * 文字列を指定するコンストラクタ.
     * 
     * @param s 文字列
     */
    public InvalidParameterException(String s) {
        super(s);
    }

    /**
     * 原因を指定するコンストラクタ.
     * 
     * @param cause 原因
     */
    public InvalidParameterException(Throwable cause) {
        super(cause);
    }

    /**
     * メッセージと原因を指定するコンストラクタ.
     * 
     * @param message メッセージ
     * @param cause 原因
     */
    public InvalidParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}
