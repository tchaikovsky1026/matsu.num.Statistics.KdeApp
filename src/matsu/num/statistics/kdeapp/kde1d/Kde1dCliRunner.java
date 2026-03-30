/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.30
 */
package matsu.num.statistics.kdeapp.kde1d;

import java.io.PrintStream;

import matsu.num.statistics.kdeapp.comp.ResolverContainer;
import matsu.num.statistics.kdeapp.exception.ApplicationException;
import matsu.num.statistics.kdeapp.kde1d.task.GaussianStandardKde1dCalculator;
import matsu.num.statistics.kdeapp.kde1d.task.Kde1dSourceReader;
import matsu.num.statistics.kdeapp.kde1d.task.ResultWriter;
import matsu.num.statistics.kdeapp.kde1d.task.WritableKde1dResult;
import matsu.num.statistics.kdeapp.kde1d.task.WritingFormatter;

/**
 * 最も単純な1次元カーネル密度推定を実行するクラス. <br>
 * 実行される処理を扱う.
 * 
 * @author Matsuura Y.
 */
final class Kde1dCliRunner {

    /**
     * 唯一のコンストラクタ.
     */
    Kde1dCliRunner() {
        super();
    }

    /**
     * ソースとなるファイルパスをコマンドライン引数として受け取り, 標準出力で推定結果を出力する単純実行.
     * 
     * <p>
     * 入力ファイルのフォーマットは, {@link SourceReaderConstructor} に従う. <br>
     * 出力フォーマットは, {@link WritingFormatterConstructor} に従う.
     * </p>
     * 
     * <p>
     * 発生した例外は, {@link ApplicationException} でラップされてスローされる.
     * </p>
     * 
     * @param args コマンドライン引数
     * @return 終了コード
     * @throws ApplicationException アプリケーション例外がスローされた場合
     * @throws Exception 予期しない例外がスローされた場合
     */
    int run(String[] args) throws Exception {
        return run(args, System.out, System.err);
    }

    /**
     * クラス内部での利用とテスト用に用意された run メソッド. <br>
     * 契約は {@link #run(String[])} と同一.
     * 
     * @param out System.out
     * @param err System.err
     * @throws ApplicationException アプリケーション例外がスローされた場合
     * @throws Exception 予期しない例外がスローされた場合
     */
    int run(String[] args, PrintStream out, PrintStream err) throws Exception {

        out.println("kde1d...");

        ResolverContainer container = Commands.getInterpreter().interpret(args)
                .withDefaults(Resolvers.DEFAULT_RESOLVERS);
        Resolvers.validateCompleteness(container);

        Kde1dSourceReader loader =
                new SourceReaderConstructor().apply(container);
        WritingFormatter writingFormatter =
                new WritingFormatterConstructor().apply(container);
        ResultWriter fileWriter =
                new FileWriterConstructor().apply(container);
        ResultWriter printer =
                new PrinterConstructor(out, err).apply(container);

        double[] source = loader.read();
        WritableKde1dResult result = new GaussianStandardKde1dCalculator().calc(source);
        printer.andThen(fileWriter).write(result, writingFormatter);

        out.println("Bye.");
        return 0;
    }
}
