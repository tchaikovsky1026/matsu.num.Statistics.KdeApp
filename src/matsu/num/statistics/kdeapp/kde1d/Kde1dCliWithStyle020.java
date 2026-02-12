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

import java.io.IOException;
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
     * 入力はファイルパスのみである. <br>
     * 引数のlengthが1でない場合はerr出力.
     * </p>
     * 
     * <p>
     * 入力ファイルのフォーマットは, 次である.
     * </p>
     * 
     * <ul>
     * <li>エスケープ文字が "#"</li>
     * <li>ソースの値は 1 column で縦に並べる</li>
     * <li>ソースの値には inf, NaN を含まず, {@link Double#parseDouble(String)} で解釈可能</li>
     * </ul>
     * 
     * <p>
     * 出力フォーマットは, ラベル無しのタブ区切り 2 columns である.
     * </p>
     * 
     * <p>
     * 入力ファイルが見つからない, ファイルフォーマットが不正の場合は例外をスロー.
     * </p>
     * 
     * @param args コマンドライン引数
     * @return 終了コード
     * @throws IOException 入力ファイルが見つからない, フォーマット不正の場合
     */
    int run(String[] args) throws IOException {
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
                Kde1dSourceLoaderConstructor.INSTANCE.construct(interpretation);
        WritingFormatter writingFormatter =
                WritingFormatterConstructor.INSTANCE.construct(interpretation);
        OutputTarget targets =
                OutputTargetsConstructor.INSTANCE.construct(interpretation);
        ResultDisplay stdout = ResultDisplay.stdout(out, err);

        double[] source = loader.load();
        WritableKde1dResult result = new GaussianStandardKde1dCalculator().calc(source);
        stdout.write(result, writingFormatter);
        targets.write(result, writingFormatter);

        out.println("Bye.");
        return 0;
    }
}
