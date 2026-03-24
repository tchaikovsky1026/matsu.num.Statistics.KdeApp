/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.24
 */
package matsu.num.statistics.kdeapp.kde2d;

import java.util.Objects;
import java.util.function.Consumer;

import matsu.num.statistics.kdeapp.kde2d.task.WritingFormatter;



/**
 * 
 * 
 * @author Matsuura Y.
 */
final class OutputLabelPrefixConfig {

    private final Consumer<WritingFormatter.Builder<?>> consumer;

    private OutputLabelPrefixConfig(Consumer<WritingFormatter.Builder<?>> consumer) {
        this.consumer = Objects.requireNonNull(consumer);
    }

    final void accept(WritingFormatter.Builder<?> formatterBuilder) {
        consumer.accept(Objects.requireNonNull(formatterBuilder));
    }

    static OutputLabelPrefixConfig nonLabel() {
        return new OutputLabelPrefixConfig(b -> b.disableLabel());
    }

    static OutputLabelPrefixConfig withLabel(String labelPrefix) {
        Objects.requireNonNull(labelPrefix);
        return new OutputLabelPrefixConfig(b -> b.enableLabel(labelPrefix));
    }
}
