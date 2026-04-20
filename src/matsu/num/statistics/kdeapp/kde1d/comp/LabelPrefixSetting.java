/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.29
 */
package matsu.num.statistics.kdeapp.kde1d.comp;

import java.util.Objects;
import java.util.function.Consumer;

import matsu.num.statistics.kdeapp.kde1d.task.XyTypeFormatterBuilder;

/**
 * ラベル出力に関する設定を扱う.
 * 
 * @author Matsuura Y.
 */
public final class LabelPrefixSetting implements Consumer<XyTypeFormatterBuilder> {

    private final Consumer<XyTypeFormatterBuilder> operation;

    private LabelPrefixSetting(Consumer<XyTypeFormatterBuilder> applyBuilder) {
        this.operation = Objects.requireNonNull(applyBuilder);
    }

    /**
     * ビルダに対して LabelPrefix 関係の作用を行う.
     * 
     * @param builder ビルダ
     * @throws NullPointerException 引数がnullの場合
     */
    @Override
    public void accept(XyTypeFormatterBuilder builder) {
        operation.accept(builder);
    }

    /**
     * ラベル出力しないことを表す設定.
     * 
     * @return 設定
     */
    public static LabelPrefixSetting disable() {
        return new LabelPrefixSetting(b -> b.disableLabel());
    }

    /**
     * ラベル出力することを表す設定.
     * 
     * @param labelPrefix labelPrefix
     * @return 設定
     * @throws NullPointerException 引数がnullの場合
     */
    public static LabelPrefixSetting enable(String labelPrefix) {
        Objects.requireNonNull(labelPrefix);
        return new LabelPrefixSetting(b -> b.enableLabel(labelPrefix));
    }
}
