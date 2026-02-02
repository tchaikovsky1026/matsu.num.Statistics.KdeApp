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

/**
 * オプションパラメータからインスタンスを構築することを表現するインターフェース.
 * 
 * @author Matsuura Y.
 */
interface ComponentConstructor<T> {

    /**
     * コンソールパラメータからインスタンスを構築する.
     * 
     * <p>
     * インスタンスを構築できない場合, {@link InvalidParameterException} をスローする.
     * </p>
     * 
     * @implSpec
     *               スローできる例外について, インターフェース説明に従うこと
     * 
     * @param interpreter パラメータの解釈
     * @return インスタンス
     * @throws InvalidParameterException パラメータが不正の場合
     * @throws NullPointerException 引数がnullの場合
     */
    public abstract T construct(ConsoleParameterInterpreter interpreter);
}
