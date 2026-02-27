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
 * アプリケーションに係る例外を扱うパッケージ.
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

    /**
     * ログ出力向けの文字列を取得する.
     * 
     * <p>
     * 形式は, <br>
     * 単純クラス名 + ": " + メッセージ <br>
     * である.
     * </p>
     * 
     * @return 文字列
     */
    public final String toStringForLogging() {
        return getClass().getSimpleName() + ": " + getMessage();
    }

    /**
     * この例外によりアプリケーションが終了するときの終了コードを返す. <br>
     * 2 以上の整数である.
     * 
     * @implSpec
     *               {@link ApplicationException} クラスでは2を返す. <br>
     *               サブクラスでオーバーライドしても良い (2 以上の整数を返すようにすること).
     * 
     * @return 終了コード
     */
    public int getExitCode() {
        return ExitCode.getExitCode(getClass());
    }
}
