/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.18
 */
package matsu.num.statistics.kdeapp.kde2d;

import matsu.num.statistics.kdeapp.command.ConsoleParameters;
import matsu.num.statistics.kdeapp.exception.IllegalParameterException;

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
     * (インスタンスを構築できない場合, {@link IllegalParameterException} をスローする.) <br>
     * TODO: ここで {@link IllegalParameterException} をスローするのは不適切である. <br>
     * このメソッドは例外をスローしないのが正しい.
     * </p>
     * 
     * @implSpec
     *               スローできる例外について, インターフェース説明に従うこと
     * 
     * @param interpreter パラメータの解釈
     * @return インスタンス
     * @throws NullPointerException 引数がnullの場合
     */
    public abstract T construct(ConsoleParameters interpreter);
}
