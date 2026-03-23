/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.23
 */
package matsu.num.statistics.kdeapp.base;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * (一次的なモック)
 * 
 * @author Matsuura Y.
 */
public final class DummySupplier {
    private DummySupplier() {
        // インスタンス化不可
        throw new AssertionError();
    }

    /**
     * ダミーサプライヤを返す.
     * 
     * @param <T> 値型
     * @param obj getされるインスタンス
     * @return サプライヤ
     * @throws NullPointerException 引数がnull
     */
    public static <T> Supplier<T> instance(T obj) {
        Objects.requireNonNull(obj);
        return () -> obj;
    }
}
