/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.2
 */
package matsu.num.statistics.kdeapp.kde1d;

/**
 * パラメータ不正であることを表現する例外クラス.
 * 
 * @author Matsuura Y.
 */
public final class InvalidParameterException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    InvalidParameterException() {
        super();
    }

    InvalidParameterException(String s) {
        super(s);
    }

    InvalidParameterException(Throwable cause) {
        super(cause);
    }

    InvalidParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}
