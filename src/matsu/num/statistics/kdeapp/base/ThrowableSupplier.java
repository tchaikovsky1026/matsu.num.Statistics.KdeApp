/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.22
 */
package matsu.num.statistics.kdeapp.base;

import java.util.function.Supplier;

/**
 * {@link Supplier} を模したサプライヤ.
 * 
 * <p>
 * get 時に
 * {@link Exception} をスローできるようにインターフェースを定義した. <br>
 * ラムダ (あるいはメソッド参照) により実装されるべきである.
 * </p>
 * 
 * @author Matsuura Y.
 * @param <T> get の戻り値型
 * @param <X> get でスローされる可能性がある例外の型
 */
@FunctionalInterface
public interface ThrowableSupplier<T, X extends Exception> {

    /**
     * インスタンスを返す.
     * 
     * @return T型のインスタンス
     * @throws X get に失敗した場合
     */
    public abstract T get() throws X;
}
