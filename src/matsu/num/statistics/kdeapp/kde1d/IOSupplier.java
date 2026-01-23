/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.1.13
 */
package matsu.num.statistics.kdeapp.kde1d;

import java.io.IOException;
import java.util.function.Supplier;

/**
 * {@link Supplier} を模したサプライヤ.
 * 
 * <p>
 * get 時に
 * {@link IOException} をスローできるようにインターフェースを定義した. <br>
 * ラムダ (あるいはメソッド参照) により実装されるべきである.
 * </p>
 * 
 * <p>
 * {@code kde1d} パッケージで閉じることが可能ならば,
 *  このインターフェースのアクセス修飾子は package-private のままが望ましい.
 * </p>
 * 
 * @author Matsuura Y.
 * @param <T> get の戻り値型
 */
@FunctionalInterface
interface IOSupplier<T> {

    /**
     * インスタンスを返す.
     * 
     * @return T型のインスタンス
     * @throws IOException IOに失敗した場合
     */
    public abstract T get() throws IOException;
}
