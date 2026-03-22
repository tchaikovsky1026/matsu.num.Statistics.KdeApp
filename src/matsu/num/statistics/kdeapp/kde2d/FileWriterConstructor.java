/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.22
 */
package matsu.num.statistics.kdeapp.kde2d;

import static matsu.num.statistics.kdeapp.kde2d.Commands.*;

import matsu.num.statistics.kdeapp.command.ComponentConstructor;
import matsu.num.statistics.kdeapp.command.ConsoleParameters;
import matsu.num.statistics.kdeapp.kde2d.task.ResultFileWriter;
import matsu.num.statistics.kdeapp.kde2d.task.ResultWriter;

/**
 * {@link ResultFileWriter} の構築器.
 * 
 * @author Matsuura Y.
 */
final class FileWriterConstructor implements ComponentConstructor<ResultWriter> {

    /**
     * 唯一のコンストラクタ.
     */
    FileWriterConstructor() {
    }

    /**
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public ResultWriter apply(ConsoleParameters interpreter) {

        WriterContainer container = new WriterContainer();

        /*
         * この2個の出力はコンソールパラメータの解釈時に排他検証されているので,
         * ここは else 処理しなくてよい.
         */
        // OUTPUT_NONEはチェックしていない
        interpreter.valueOf(OUTPUT_FORCE_FILE_PATH)
                .map(ResultFileWriter::forceWriter)
                .ifPresent(container::add);
        interpreter.valueOf(OUTPUT_FILE_PATH)
                .map(ResultFileWriter::regularWriter)
                .ifPresent(container::add);

        return container.get();
    }

    /**
     * {@link ResultWriter} の可変コンテナ.
     */
    private static final class WriterContainer {

        private ResultWriter element;

        /**
         * 唯一のコンストラクタ.
         * 内部のライター要素はnullWriterで初期化される.
         */
        WriterContainer() {
            element = ResultWriter.nullWriter();
        }

        /**
         * @throws NullPointerException 引数がnullの場合
         */
        void add(ResultWriter writer) {
            element = element.andThen(writer);
        }

        ResultWriter get() {
            return element;
        }
    }
}
