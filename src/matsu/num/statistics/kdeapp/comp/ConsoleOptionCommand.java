/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.23
 */
package matsu.num.statistics.kdeapp.comp;

import static java.util.stream.Collectors.*;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * コンソールオプションコマンドを表現するクラス.
 * 
 * <p>
 * このクラスのサブタイプは, identity を equality と定める. <br>
 * 基本は {@code public static final} な定数として扱う.
 * </p>
 * 
 * <p>
 * このクラスは抽象メソッドを持っていないが,
 * 必ず継承して使用されなければならないので抽象クラスとする.
 * </p>
 * 
 * @author Matsuura Y.
 * @param <T> このコマンドによって得られる値の型, see {@link PropertyKey}
 */
public abstract sealed class ConsoleOptionCommand<T>
        permits ArgumentRequiringCommand, NoArgumentCommand {

    private final String enumString;
    private final PropertyKey<T> propertyKey;

    private final String commandRepresentation;
    private final Set<String> representations;

    /**
     * 唯一のコンストラクタ. <br>
     * パッケージ外での継承が許可されないので非公開である.
     * 
     * @param enumString インスタンスの文字列表現
     * @param propertyKey 対応するプロパティキー
     * @param commandRepresentation コマンドの正式な文字列表現
     * @param otherRepresentations 正式表現以外の文字列表現
     * @throws IllegalArgumentException 文字列表現に空白を含む場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    ConsoleOptionCommand(String enumString, PropertyKey<T> propertyKey,
            String commandRepresentation, String... otherRepresentations) {

        this.enumString = enumString;
        if (this.enumString.isBlank()) {
            throw new IllegalArgumentException("enumString is blank");
        }
        this.propertyKey = Objects.requireNonNull(propertyKey);

        this.commandRepresentation = Objects.requireNonNull(commandRepresentation);
        this.representations = new HashSet<>();
        this.representations.add(commandRepresentation);
        this.representations.addAll(List.of(otherRepresentations));

        if (this.representations.stream().anyMatch(
                r -> r.chars().anyMatch(Character::isWhitespace))) {
            throw new IllegalArgumentException(this.toString() + " includes white space");
        }
    }

    /**
     * コマンドの正式な文字列表現を返す.
     * 
     * @return 正式な文字列表現
     */
    public final String commandString() {
        return commandRepresentation;
    }

    /**
     * コマンド文字列表現のリストを得る. <br>
     * 順番は不明である. <br>
     * コピーではないかもしれないので, 戻り値に対して変更を加えるようなメソッドを実行してはならない.
     * 
     * @return 文字列表現リスト
     */
    final List<String> representations() {
        return List.copyOf(representations);
    }

    /**
     * このコマンドに対応付けられるプロパティキーを返す.
     * 
     * @return プロパティキー
     */
    final PropertyKey<T> propertyKey() {
        return propertyKey;
    }

    /**
     * コンパレータによって並び変えられたコマンド文字列表現のリストを得る. <br>
     * コピーではないかもしれないので, 戻り値に対して変更を加えるようなメソッドを実行してはならない.
     * 
     * @param comparator コンパレータ
     * @return 文字列表現リスト
     * @throws NullPointerException 引数がnullの場合
     */
    public final List<String> representations(Comparator<String> comparator) {
        return representations.stream()
                .sorted(comparator)
                .toList();
    }

    /**
     * 与えられたインスタンスと等価かどうかを判定する.
     */
    @Override
    public final boolean equals(Object obj) {
        // 同一性により判定する.
        return super.equals(obj);
    }

    /**
     * このインスタンスのハッシュコードを返す.
     */
    @Override
    public final int hashCode() {
        // identityHashCode
        return super.hashCode();
    }

    /**
     * このインスタンスの文字列表現を返す.
     */
    @Override
    public final String toString() {
        return this.enumString;
    }

    /**
     * コマンドの集合を, コマンド文字列からコマンドインスタンスへのマッパに変換する.
     * 
     * <p>
     * コマンドの文字列表現が重複した場合, どちらが採用されるかは不明である.
     * </p>
     * 
     * @param commands コマンドの集合
     * @return コマンド文字列からコマンドインスタンスへのマッパ
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    static <T extends ConsoleOptionCommand<?>>
            Map<String, T> toCommandMapper(Collection<? extends T> commands) {

        // フラット化
        List<Pair<T>> pairs = commands.stream()
                .flatMap(
                        o -> o.representations().stream()
                                .map(str -> new Pair<T>(o, str)))
                .toList();

        // representationの重複はBinaryOperatorで片方を無視する
        return pairs.stream()
                .collect(toMap(p -> p.representation, p -> p.command, (s1, s2) -> s2));
    }

    // コマンドインスタンスとコマンド文字列のペアを表現するクラス
    private static final class Pair<T extends ConsoleOptionCommand<?>> {
        final T command;
        final String representation;

        Pair(T command, String representation) {
            super();
            this.command = command;
            this.representation = representation;
        }
    }

    /**
     * コマンド集合において, 文字列表現に重複がないことを確認する.
     * 
     * @param commands コマンドの集合
     * @throws IllegalArgumentException プロパティ名に重複がある場合
     * @throws NullPointerException 引数にnullを含む場合
     */
    public static void requireNoRepresentationDuplicates(
            Set<? extends ConsoleOptionCommand<?>> commands) {

        Set<String> nameSet = new HashSet<>();
        for (ConsoleOptionCommand<?> c : commands) {
            for (String r : c.representations) {
                if (!nameSet.add(r)) {
                    throw new IllegalArgumentException("duplicate property name: " + r);
                }
            }
        }
    }
}
