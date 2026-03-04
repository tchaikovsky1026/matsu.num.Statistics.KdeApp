/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.3
 */
package matsu.num.statistics.kdeapp.kde2d;

import java.io.PrintStream;

import matsu.num.statistics.kdeapp.command.ConsoleParameters;
import matsu.num.statistics.kdeapp.exception.ApplicationException;

/**
 * 最も単純な2次元カーネル密度推定を実行するクラス. <br>
 * 実行される処理を扱う.
 * 
 * <p>
 * コンソールパラメータは, version 0.5.0 以降のスタイルとする.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class Kde2dCliWithStyle050 {

    /**
     * 唯一のコンストラクタ.
     */
    Kde2dCliWithStyle050() {
        super();
    }

    /**
     * ソースとなるファイルパスをコマンドライン引数として受け取り, 標準出力で推定結果を出力する単純実行.
     * 
     * <p>
     * 入力ファイルのフォーマットは, {@link Kde2dSourceLoaderConstructor} に従う. <br>
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

        out.println("kde2d...");

        ConsoleParameters interpretation = Commands.getInterpreter().interpret(args);

        Kde2dSourceLoader loader =
                new Kde2dSourceLoaderConstructor().apply(interpretation);
        WritingFormatter writingFormatter =
                new WritingFormatterConstructor().apply(interpretation);
        ResultOutput output =
                new ResultOutputConstructor().apply(interpretation);
        ResultDisplay stdout =
                new ResultDisplayConstructor(out, err).apply(interpretation);

        double[][] source = loader.load();
        WritableKde2dResult result = new GaussianStandardKde2dCalculator().calc(source);
        stdout.write(result, writingFormatter);
        output.write(result, writingFormatter);

        out.println("Bye.");
        return 0;
    }
}
