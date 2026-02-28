/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.27
 */
package matsu.num.statistics.kdeapp.command;

import static java.util.stream.Collectors.*;

import java.util.Collection;
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
 */
public abstract sealed class ConsoleOptionCommand
        permits ArgumentRequiringCommand, NoArgumentCommand {

    private final String enumString;

    private final String commandRepresentation;
    private final Set<String> representations;

    /**
     * 唯一のコンストラクタ. <br>
     * パッケージ外での継承が許可されないので非公開である.
     * 
     * @param enumString インスタンスの文字列表現
     * @param commandRepresentation コマンドの正式な文字列表現
     * @param otherRepresentations 正式表現以外の文字列表現
     * @throws IllegalArgumentException ブランクを含む場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    ConsoleOptionCommand(String enumString,
            String commandRepresentation, String... otherRepresentations) {

        this.enumString = enumString;
        if (this.enumString.isBlank()) {
            throw new IllegalArgumentException("enumString is blank");
        }

        this.commandRepresentation = Objects.requireNonNull(commandRepresentation);
        this.representations = new HashSet<>();
        this.representations.add(commandRepresentation);
        this.representations.addAll(List.of(otherRepresentations));

        if (this.representations.stream().anyMatch(String::isBlank)) {
            throw new IllegalArgumentException(this.toString() + ": blank representation");
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
     * おそらくイミュータブルである.
     * 
     * @return 文字列表現リスト
     */
    final List<String> representations() {
        return List.copyOf(representations);
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
     * コマンドの文字列表現が重複してはいけない.
     * </p>
     * 
     * @param commands コマンドの集合
     * @return コマンド文字列からコマンドインスタンスへのマッパ
     * @throws IllegalArgumentException コマンド文字列に重複がある場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    static <T extends ConsoleOptionCommand>
            Map<String, T> toCommandMapper(Collection<? extends T> commands) {

        // フラット化
        List<Pair<T>> pairs = commands.stream()
                .flatMap(
                        o -> o.representations().stream()
                                .map(str -> new Pair<T>(o, str)))
                .toList();

        // オプションの文字列表現定義に重複がないことを確認する.
        Map<String, Long> map = pairs.stream()
                .map(p -> p.representation)
                .collect(groupingBy(s -> s, counting()));
        for (Map.Entry<String, Long> e : map.entrySet()) {
            if (e.getValue().longValue() >= 2) {
                throw new IllegalArgumentException("duplicate representation: " + e.getKey());
            }
        }

        // representationに重複がないので, toMapは成功
        return pairs.stream()
                .collect(toMap(p -> p.representation, p -> p.command));
    }

    // コマンドインスタンスとコマンド文字列のペアを表現するクラス
    private static final class Pair<T extends ConsoleOptionCommand> {
        final T command;
        final String representation;

        Pair(T command, String representation) {
            super();
            this.command = command;
            this.representation = representation;
        }
    }
}
