/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.5
 */
package matsu.num.statistics.kdeapp.kde2d.format;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * ブロック化された文字列Iterable
 * ({@code Iterable<Iterable<String>>})
 * をフラット化して,
 * {@code Iterable<String>} に変換するヘルパ.
 * 
 * @author Matsuura Y.
 */
public final class BlockFlattening {

    private final int gap;
    private final Iterable<String> blanckLineIterable;

    /**
     * 唯一のコンストラクタ. <br>
     * ブロックの間に挟む空行の数を与える.
     * 
     * @param gap ブロック間の空行の数
     * @throws IllegalArgumentException gap が負の場合
     */
    public BlockFlattening(int gap) {
        super();

        if (gap < 0) {
            throw new IllegalArgumentException("gap < 0");
        }
        this.gap = gap;
        this.blanckLineIterable = new BlankLineIterable();
    }

    /**
     * 与えられた構造化された文字列 Iterable をフラット化する. <br>
     * フラット化の際に, 間に空行を挟む.
     * 
     * <p>
     * 与えられる Iterable は不変でなければならない. <br>
     * 可変である場合, 動作は保証されない. <br>
     * スレッドセーフでない.
     * </p>
     * 
     * @param src ソース
     * @return フラット化した Iterable
     * @throws NullPointerException 引数が null の場合
     */
    public Iterable<String> apply(Iterable<? extends Iterable<? extends String>> src) {
        return new FlattenedIterable(src);
    }

    /**
     * フラット化された Iterator の実装. <br>
     * スレッドセーフでない.
     */
    private final class FlattenedIterable implements Iterable<String> {

        private final List<Iterable<? extends String>> iterableList;

        FlattenedIterable(Iterable<? extends Iterable<? extends String>> src) {
            super();

            List<Iterable<? extends String>> list = new ArrayList<>();
            for (Iterator<? extends Iterable<? extends String>> oi = src.iterator();
                    oi.hasNext();) {
                list.add(oi.next());
                if (oi.hasNext()) {
                    list.add(blanckLineIterable);
                }
            }
            this.iterableList = list;
        }

        @Override
        public Iterator<String> iterator() {
            return new FlattenedIterator();
        }

        private final class FlattenedIterator implements Iterator<String> {

            private final Iterator<Iterable<? extends String>> outer = iterableList.iterator();
            private Iterator<? extends String> inner = Collections.emptyIterator();

            @Override
            public boolean hasNext() {
                normalize();
                return inner.hasNext();
            }

            @Override
            public String next() {
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

    /**
     * ブランク行を表すIterable. <br>
     * このクラスのインスタンスは使いまわしが可能.
     */
    private final class BlankLineIterable implements Iterable<String> {

        BlankLineIterable() {
            super();
        }

        @Override
        public Iterator<String> iterator() {
            return new BLIterator();
        }

        /**
         * イテレータ.
         */
        private final class BLIterator implements Iterator<String> {

            private int cursor = 0;

            BLIterator() {
                super();
            }

            @Override
            public boolean hasNext() {
                return cursor < gap;
            }

            @Override
            public String next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                cursor++;
                return "";
            }
        }
    }
}
