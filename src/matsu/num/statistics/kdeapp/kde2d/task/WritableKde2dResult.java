/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.12
 */
package matsu.num.statistics.kdeapp.kde2d.task;

import java.io.PrintWriter;
import java.util.Objects;

import matsu.num.statistics.kerneldensity.output.FormattableKdeResult2D;

/**
 * 2次元のカーネル密度推定結果を扱うクラス.
 * 
 * @author Matsuura Y.
 */
public final class WritableKde2dResult {

    private final FormattableKdeResult2D kde1dResult;

    /**
     * パッケージ内にのみ公開されたコンストラクタ.
     * 
     * <p>
     * {@link FormattableKdeResult2D} を生成する計算器から呼ばれることを想定している. <br>
     * それ以外の呼ばれ方は不適当である.
     * </p>
     */
    WritableKde2dResult(FormattableKdeResult2D kde1dResult) {
        super();
        assert Objects.nonNull(kde1dResult) : " arg is null";
        this.kde1dResult = kde1dResult;
    }

    /**
     * 結果を出力する.
     * 
     * <p>
     * 結果出力フォーマットは, 3 columns であり,
     * 与えたフォーマッターにより成形される. <br>
     * メソッド終了時に, PrintWriter はフラッシュされる. <br>
     * 戻り値により例外が発生したかどうかを報告する
     * (例外が発生した場合は {@code true})
     * </p>
     * 
     * @param pw 出力となる PrintWriter
     * @param formatter フォーマッター
     * @return 書き込み処理で例外が発生した場合はtrue
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    boolean write(PrintWriter pw, WritingFormatter formatter) {
        for (String s : formatter.format(kde1dResult)) {
            pw.println(s);
        }
        return pw.checkError();
    }
}
