/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.2
 */
package matsu.num.statistics.kdeapp.kde1d;

import java.io.PrintStream;

import matsu.num.statistics.kdeapp.kde1d.command.ConsoleParameterInterpreter;
import matsu.num.statistics.kdeapp.kde1d.exception.ApplicationException;

/**
 * 最も単純な1次元カーネル密度推定を実行するクラス.
 * 
 * <p>
 * コンソールパラメータは, version 0.2.0 以降のスタイルとする.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class Kde1dCliWithStyle020 {

    /**
     * 唯一のコンストラクタ.
     */
    Kde1dCliWithStyle020() {
        super();
    }

    /**
     * ソースとなるファイルパスをコマンドライン引数として受け取り, 標準出力で推定結果を出力する単純実行.
     * 
     * <p>
     * 入力ファイルのフォーマットは, {@link Kde1dSourceLoaderConstructor} に従う. <br>
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
     */
    int run(String[] args) {
        return run(args, System.out, System.err);
    }

    /**
     * クラス内部での利用とテスト用に用意された run メソッド. <br>
     * 契約は {@link #run(String[])} と同一.
     * 
     * @param out System.out
     * @param err System.err
     * @throws ApplicationException アプリケーション例外がスローされた場合
     */
    int run(String[] args, PrintStream out, PrintStream err) {

        out.println("kde1d...");

        ConsoleParameterInterpreter interpretation = ConsoleParameterInterpreter.from(args);

        Kde1dSourceLoader loader =
                new Kde1dSourceLoaderConstructor().construct(interpretation);
        WritingFormatter writingFormatter =
                new WritingFormatterConstructor().construct(interpretation);
        ResultOutput output =
                new ResultOutputConstructor().construct(interpretation);
        ResultDisplay stdout =
                new ResultDisplayConstructor(out, err).construct(interpretation);

        double[] source = loader.load();
        WritableKde1dResult result = new GaussianStandardKde1dCalculator().calc(source);
        stdout.write(result, writingFormatter);
        output.write(result, writingFormatter);

        out.println("Bye.");
        return 0;
    }
}
