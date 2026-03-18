/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.17
 */
package matsu.num.statistics.kdeapp.help;

/**
 * ヘルプ表示用のコマンド説明フォーマッタ.
 * 
 * @author Matsuura Y.
 */
public interface DescriptionFormatter {

    /**
     * 与えたコマンドの集合から, ヘルプ表示用の文字列を生成する. <br>
     * 1行ごとの文字列の Iterable として返す.
     * 
     * @param commands コマンド
     * @return 表示用文字列
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public abstract Iterable<String> format(Iterable<? extends CommandDescription> commands);

}
