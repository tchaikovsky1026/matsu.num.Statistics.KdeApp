/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.1.21
 */
package matsu.num.statistics.kdeapp.kde1d;

import java.io.PrintWriter;
import java.util.Objects;

import matsu.num.statistics.kerneldensity.output.FormattableKdeResult1D;
import matsu.num.statistics.kerneldensity.output.Kde1dCharSVTextFormatter;

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
     * 結果出力フォーマットは, ラベル無しのタブ区切り 2 columns である. <br>
     * メソッド終了時に, PrintWriter はフラッシュされる.
     * </p>
     * 
     * @param pw 出力となる PrintWriter
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    void write(PrintWriter pw) {
        for (String s : kde1dResult.formatted(Kde1dCharSVTextFormatter.labelless('\t'))) {
            pw.println(s);
        }
        pw.flush();
    }
}
