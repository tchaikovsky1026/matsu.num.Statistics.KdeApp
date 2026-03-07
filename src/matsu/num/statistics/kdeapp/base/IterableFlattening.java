/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.7
 */
package matsu.num.statistics.kdeapp.base;

import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

/**
 * ネストされた {@link Iterable} をフラット化する機能.
 * 
 * @author Matsuura Y.
 */
public final class IterableFlattening {

    private IterableFlattening() {
        // インスタンス化不可
        throw new AssertionError();
    }

    /**
     * 与えられたネスト {@link Iterable} をフラット化する.
     * 
     * <p>
     * 得られたイテレータはスレッドセーフでない. <br>
     * remove メソッドはサポートされていない.
     * </p>
     * 
     * <p>
     * 外側イテレータの要素に null が含まれる場合,
     * イテレータ時に例外がスローされる. <br>
     * イテレータ中に,
     * ソースとなるネスト {@link Iterable} の内部状態が書き換えられることは想定されていない. <br>
     * 動作は保証されない.
     * </p>
     * 
     * @param <T> 要素の型
     * @param src ソースとなるネストされた {@link Iterable}
     * @return フラット化の結果
     * @throws NullPointerException 引数が null の場合
     */
    public static <T> Iterable<T> flatten(Iterable<? extends Iterable<? extends T>> src) {
        return new FlattenedIterable<T>(src);
    }

    /**
     * フラット化された Iterator の実装. <br>
     * スレッドセーフでない.
     */
    private static final class FlattenedIterable<T> implements Iterable<T> {

        private final Iterable<? extends Iterable<? extends T>> src;

        /**
         * @throws NullPointerException 引数が null の場合
         */
        FlattenedIterable(Iterable<? extends Iterable<? extends T>> src) {
            super();
            this.src = Objects.requireNonNull(src);
        }

        @Override
        public Iterator<T> iterator() {
            return new FlattenedIterator();
        }

        private final class FlattenedIterator implements Iterator<T> {

            private final Iterator<? extends Iterable<? extends T>> outer = src.iterator();
            private Iterator<? extends T> inner = Collections.emptyIterator();

            @Override
            public boolean hasNext() {
                normalize();
                return inner.hasNext();
            }

            @Override
            public T next() {
                normalize();
                return inner.next();
            }

            /**
             * inner.hasNext == true となるまでイテレータを進める.
             * ただし, outer, inner ともに hasNext == false となった場合は終了.
             */
            private void normalize() {
                while (!inner.hasNext()) {
                    if (!outer.hasNext()) {
                        break;
                    }
                    inner = outer.next().iterator();
                }
            }
        }
    }
}
