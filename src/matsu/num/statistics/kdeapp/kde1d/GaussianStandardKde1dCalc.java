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
import java.util.Arrays;

import matsu.num.statistics.kerneldensity.GaussianKd1D;
import matsu.num.statistics.kerneldensity.KernelDensity1D;
import matsu.num.statistics.kerneldensity.Range;
import matsu.num.statistics.kerneldensity.output.FormattableKdeResult1D;
import matsu.num.statistics.kerneldensity.output.Kde1dCharSVTextFormatter;

/**
 * スタンダードなガウシアンKdeにて, 1次元のカーネル密度推定を実行する.
 * 
 * @author Matsuura Y.
 */
final class GaussianStandardKde1dCalc {

    private final GaussianKd1D.Factory kde1dFactory = GaussianKd1D.Factory.withDefaultRule();

    GaussianStandardKde1dCalc() {
    }

    /**
     * ソースを入力し, 推定を実行し, 結果を出力する.
     * 
     * <p>
     * 入力ソースは配列である. <br>
     * メソッド終了まで書き換えられることは想定されていない.
     * </p>
     * 
     * <p>
     * 計算における範囲と空間分解能は, ソースから自動的に判定される. <br>
     * 結果出力フォーマットは, ヘッダ無しのタブ区切り 2 columns である. <br>
     * メソッド終了時に, PrintWriter はフラッシュされる.
     * </p>
     * 
     * @param source 入力ソース
     * @param pw 出力となる PrintWriter
     * @throws IllegalArgumentException ソースが空の場合, NaNを含む場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    void runAndWrite(double[] source, PrintWriter pw) {

        // 暫定的に, 描画区間を自動で定めるとする
        if (source.length == 0) {
            throw new IllegalArgumentException("source is empty");
        }
        double min = Arrays.stream(source).min().getAsDouble();
        double max = Arrays.stream(source).max().getAsDouble();
        if (Double.isFinite(min) && Double.isFinite(max)) {
            double newMin = min - (max - min) * 0.5;
            double newMax = max + (max - min) * 0.5;

            min = newMin;
            max = newMax;
        }

        KernelDensity1D kde = kde1dFactory.createOf(source);
        FormattableKdeResult1D kdeResult =
                FormattableKdeResult1D.evaluate(kde, Range.of(min, max));

        for (String s : kdeResult.formatted(Kde1dCharSVTextFormatter.labelless('\t'))) {
            pw.println(s);
        }
        pw.flush();
    }
}
