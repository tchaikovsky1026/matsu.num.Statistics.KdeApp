/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.17
 */
package matsu.num.statistics.kdeapp.kde2d;

import java.util.Arrays;

import matsu.num.statistics.kdeapp.kde2d.exception.CalculationException;
import matsu.num.statistics.kerneldensity.GaussianKd2D;
import matsu.num.statistics.kerneldensity.Kde2DSourceDto;
import matsu.num.statistics.kerneldensity.KernelDensity2D;
import matsu.num.statistics.kerneldensity.Range;
import matsu.num.statistics.kerneldensity.output.FormattableKdeResult2D;

/**
 * スタンダードなガウシアンKdeにて, 2次元のカーネル密度推定を実行する.
 * 
 * @author Matsuura Y.
 */
final class GaussianStandardKde2dCalculator {

    private final GaussianKd2D.Factory kde2dFactory = GaussianKd2D.Factory.withDefaultRule();

    GaussianStandardKde2dCalculator() {
    }

    /**
     * ソースを入力し, 推定を実行する. <br>
     * 結果は, {@link WritableKde2dResult} の形で得られる.
     * 
     * <p>
     * 入力ソースは配列である. <br>
     * メソッド終了まで書き換えられることは想定されていない.
     * </p>
     * 
     * <p>
     * 計算における範囲と空間分解能は, ソースから自動的に判定される.
     * </p>
     * 
     * @param source 入力ソース
     * @throws CalculationException ソースが空の場合, infやNaNを含む場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    WritableKde2dResult calc(double[][] source) {
        if (source.length != 2) {
            throw new AssertionError("source.length != 2");
        }
        if (source[0].length != source[1].length) {
            throw new AssertionError("source[0].length != source[1].length");
        }

        if (source[0].length == 0) {
            throw new CalculationException("source is empty");
        }
        // 暫定的に, 描画区間を自動で定めるとする
        if (Arrays.stream(source[0]).anyMatch(d -> !Double.isFinite(d))
                || Arrays.stream(source[1]).anyMatch(d -> !Double.isFinite(d))) {
            throw new CalculationException("source includes inf or NaN");
        }

        Kde2DSourceDto sourceDto = new Kde2DSourceDto(source[0].length);
        System.arraycopy(source[0], 0, sourceDto.x, 0, sourceDto.size);
        System.arraycopy(source[1], 0, sourceDto.y, 0, sourceDto.size);

        double minX = Arrays.stream(sourceDto.x).min().getAsDouble();
        double maxX = Arrays.stream(sourceDto.x).max().getAsDouble();
        double minY = Arrays.stream(sourceDto.y).min().getAsDouble();
        double maxY = Arrays.stream(sourceDto.y).max().getAsDouble();

        if (Double.isFinite(minX) && Double.isFinite(maxX)) {
            double newMin = minX - (maxX - minX) * 0.5;
            double newMax = maxX + (maxX - minX) * 0.5;

            minX = newMin;
            maxX = newMax;
        }
        if (Double.isFinite(minY) && Double.isFinite(maxY)) {
            double newMin = minY - (maxY - minY) * 0.5;
            double newMax = maxY + (maxY - minY) * 0.5;

            minY = newMin;
            maxY = newMax;
        }

        KernelDensity2D kde = kde2dFactory.createOf(sourceDto);
        FormattableKdeResult2D kdeResult =
                FormattableKdeResult2D.evaluate(kde, Range.of(minX, maxX), Range.of(minY, maxY));

        return new WritableKde2dResult(kdeResult);
    }
}
