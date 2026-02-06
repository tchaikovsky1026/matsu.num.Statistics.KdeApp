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

    public InvalidParameterException() {
        super();
    }

    public InvalidParameterException(String s) {
        super(s);
    }

    public InvalidParameterException(Throwable cause) {
        super(cause);
    }

    public InvalidParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}
