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

import matsu.num.statistics.kdeapp.comp.ResolverContainer;
import matsu.num.statistics.kdeapp.kde2d.task.ResultFileWriter;
import matsu.num.statistics.kdeapp.kde2d.task.ResultWriter;

/**
 * {@link ResultFileWriter} の構築器.
 * 
 * @author Matsuura Y.
 */
final class FileWriterConstructor {

    /**
     * 唯一のコンストラクタ.
     */
    FileWriterConstructor() {
    }

    /**
     * @throws NullPointerException {@inheritDoc }
     */
    ResultWriter apply(ResolverContainer property) {
        OutputFileConfig outputFileConfig = property.get(Resolvers.OUTPUT_FILE);
        return outputFileConfig.get();
    }
}
