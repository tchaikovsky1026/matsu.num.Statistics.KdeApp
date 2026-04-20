/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.4.14
 */
package matsu.num.statistics.kdeapp.base;

import java.util.function.Supplier;

/**
 * {@link Supplier} を模した, 検査例外をスローできるサプライヤ.
 * 
 * <p>
 * get メソッドの実行時に
 * {@link Exception} をスローできるようにインターフェースを定義した. <br>
 * ラムダ (あるいはメソッド参照) により実装されるべきである.
 * </p>
 * 
 * @author Matsuura Y.
 * @param <T> get メソッドの戻り値型
 * @param <X> get メソッドでスローされる可能性がある例外の型
 */
@FunctionalInterface
public interface ThrowableSupplier<T, X extends Exception> {

    /**
     * インスタンスを返す.
     * 
     * <p>
     * このメソッドの実行では, 検査例外としては {@code X} (のサブタイプ) をスローする可能性がある. <br>
     * 一方で, 実行時例外については契約を定めない. <br>
     * 実行時例外のスローについては, サブタイプの定義あるいは具象化されたときに, 正確に規定すべきである.
     * </p>
     * 
     * @return T型のインスタンス
     * @throws X get に失敗した場合
     */
    public abstract T get() throws X;
}
