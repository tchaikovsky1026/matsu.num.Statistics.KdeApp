/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.23
 */
package matsu.num.statistics.kdeapp.command;

/**
 * コンソールパラメータからインスタンスを構築することを表現するインターフェース.
 * 
 * <p>
 * コンソールパラメータの引数が読み込まれた時点で最低限のバリデーションされており,
 * それをもとに, {@link #apply(ConsoleParameters)} メソッドによりインスタンスが生成される. <br>
 * このインスタンス生成は必ず実行できる.
 * </p>
 * 
 * @author Matsuura Y.
 * @param <T> 構築されるインスタンスの型
 */
public interface ComponentConstructor<T> {

    /**
     * コンソールパラメータからインスタンスを構築する.
     * 
     * @param interpreter パラメータの解釈
     * @return インスタンス
     * @throws NullPointerException 引数がnullの場合
     */
    public abstract T apply(ConsoleParameters interpreter);
}
