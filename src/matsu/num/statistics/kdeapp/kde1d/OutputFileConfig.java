/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.24
 */
package matsu.num.statistics.kdeapp.kde1d;

import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Supplier;

import matsu.num.statistics.kdeapp.kde1d.task.ResultFileWriter;
import matsu.num.statistics.kdeapp.kde1d.task.ResultWriter;

/**
 * 
 * 
 * @author Matsuura Y.
 */
final class OutputFileConfig {

    private final Supplier<ResultWriter> supplier;

    private OutputFileConfig(Supplier<ResultWriter> supplier) {
        super();
        this.supplier = Objects.requireNonNull(supplier);
    }

    final ResultWriter get() {
        return supplier.get();
    }

    static OutputFileConfig none() {
        return new OutputFileConfig(() -> ResultWriter.nullWriter());
    }

    static OutputFileConfig output(Path path) {
        Objects.requireNonNull(path);
        return new OutputFileConfig(() -> ResultFileWriter.regularWriter(path));
    }

    static OutputFileConfig outputForce(Path path) {
        Objects.requireNonNull(path);
        return new OutputFileConfig(() -> ResultFileWriter.forceWriter(path));
    }
}
