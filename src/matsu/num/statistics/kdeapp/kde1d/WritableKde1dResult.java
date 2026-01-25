/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.1.25
 */
package matsu.num.statistics.kdeapp.kde1d;

import java.io.PrintWriter;
import java.util.Objects;

import matsu.num.statistics.kerneldensity.output.FormattableKdeResult1D;

/**
 * 1次元のカーネル密度推定結果を扱うクラス.
 * 
 * @author Matsuura Y.
 */
final class WritableKde1dResult {

    private final FormattableKdeResult1D kde1dResult;

    /**
     * パッケージ内にのみ公開されたコンストラクタ.
     * 
     * <p>
     * {@link FormattableKdeResult1D} を生成する計算器から呼ばれることを想定している. <br>
     * それ以外の呼ばれ方は不適当である.
     * </p>
     */
    WritableKde1dResult(FormattableKdeResult1D kde1dResult) {
        super();
        assert Objects.nonNull(kde1dResult) : " arg is null";
        this.kde1dResult = kde1dResult;
    }

    /**
     * 結果を出力する.
     * 
     * <p>
     * 結果出力フォーマットは, 2 columns であり,
     * 与えたフォーマッターにより成形される. <br>
     * メソッド終了時に, PrintWriter はフラッシュされる.
     * </p>
     * 
     * @param pw 出力となる PrintWriter
     * @param formatter フォーマッター
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    void write(PrintWriter pw, WritingFormatter formatter) {
        for (String s : formatter.format(kde1dResult)) {
            pw.println(s);
        }
        pw.flush();
    }
}
