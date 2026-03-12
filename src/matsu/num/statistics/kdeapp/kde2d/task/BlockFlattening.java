/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.12
 */
package matsu.num.statistics.kdeapp.kde2d.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import matsu.num.statistics.kdeapp.base.IterableFlattening;

/**
 * ブロック化された文字列Iterable
 * ({@code Iterable<Iterable<String>>})
 * をフラット化して,
 * {@code Iterable<String>} に変換するヘルパ.
 * 
 * <p>
 * 構造の間に空行を挟む.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class BlockFlattening {

    private final Iterable<String> blanckLineIterable;

    /**
     * 唯一のコンストラクタ. <br>
     * ブロックの間に挟む空行の数を与える.
     * 
     * @param blankGap ブロック間の空行の数
     * @throws IllegalArgumentException gap が負の場合
     */
    BlockFlattening(int blankGap) {
        super();

        this.blanckLineIterable = new BlankLineIterable(blankGap);
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
    Iterable<String> apply(Iterable<? extends Iterable<? extends String>> src) {
        // 与えられたネストIterableを展開し, 間にブランクを挟む
        List<Iterable<? extends String>> blankInsertedList = new ArrayList<>();
        for (Iterator<? extends Iterable<? extends String>> oi = src.iterator();
                oi.hasNext();) {
            blankInsertedList.add(oi.next());
            if (oi.hasNext()) {
                blankInsertedList.add(blanckLineIterable);
            }
        }

        return IterableFlattening.flatten(blankInsertedList);
    }

    /**
     * ブランク行を表すIterable. <br>
     * このクラスのインスタンスは使いまわしが可能.
     */
    private static final class BlankLineIterable implements Iterable<String> {

        private final int blankGap;

        /**
         * 唯一のコンストラクタ. <br>
         * ブロックの間に挟む空行の数を与える.
         * 
         * @param blankGap ブロック間の空行の数
         * @throws IllegalArgumentException gap が負の場合
         */
        BlankLineIterable(int blankGap) {
            super();

            if (blankGap < 0) {
                throw new IllegalArgumentException("blank gap < 0");
            }
            this.blankGap = blankGap;
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
                return cursor < blankGap;
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
