/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.4.12
 */
package matsu.num.statistics.kdeapp.base;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * クラスに定義された定数を集めるユーティリティ.
 * 
 * @author Matsuura Y.
 */
public final class ConstantsCollector {

    private ConstantsCollector() {
        // インスタンス化不可
        throw new AssertionError();
    }

    /**
     * 与えたクラス (clazz) に定義されている type 型のstaticフィールドの値を得る. <br>
     * ただし, clazz とフィールドは {@code public} でなければならない.
     * 
     * 
     * @apiNote
     *              type がジェネリック型の場合, 型パラメータ {@code T} には raw 型でバインドされる. <br>
     *              そのため, 戻り値の {@code Set<T>} は型安全でない. <br>
     *              そのような type を使用する場合,
     *              呼び出し元で要素型をワイルドカードで置き換えるように詰め替えを行うべきである. <br>
     *              例えば, 次のように行う.
     * 
     *              <pre>
     * // T を Optional でバインドする
     * {@code Set<Optional> rawSet = collect(clazz, Optional.class);}
     * 
     * {@code Set<Optional<?>> set = 
     *         rawSet.stream()
     *             .map(e -> (Optional<?>) e)
     *             .collect(Collectors.toSet());}
     *              </pre>
     * 
     * @param <T> 集める型
     * @param clazz clazz
     * @param type type
     * @return clazz クラスに定義された public static な type 型フィールドの値
     * @throws NullPointerException 引数にnullを含む場合
     */
    public static <T> Set<T> collect(Class<?> clazz, Class<? extends T> type) {
        Set<T> constantFieldSet = new HashSet<>();

        // staticかつ互換性のあるフィールドのみが対象
        for (Field f : clazz.getFields()) {
            if ((f.getModifiers() & Modifier.STATIC) == 0) {
                continue;
            }
            try {
                T p = type.cast(f.get(null));
                constantFieldSet.add(p);
            } catch (IllegalAccessException | ClassCastException ignore) {
                //無関係なフィールドなら無視する
            }
        }
        return constantFieldSet;
    }
}
