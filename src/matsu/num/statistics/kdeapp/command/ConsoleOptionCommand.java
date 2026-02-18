/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.18
 */
package matsu.num.statistics.kdeapp.command;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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

    private final String commandExpression;
    private final List<String> expressions;

    /**
     * 唯一のコンストラクタ. <br>
     * パッケージ外での継承が許可されないので非公開である.
     * 
     * @param enumString インスタンスの文字列表現
     * @param commandExpression コマンドの正式な文字列表現
     * @param otherExpressions 正式表現以外の文字列表現
     * @throws IllegalArgumentException ブランクを含む場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    ConsoleOptionCommand(String enumString, String commandExpression, String... otherExpressions) {

        this.enumString = enumString;
        if (this.enumString.isBlank()) {
            throw new IllegalArgumentException("enumString is blank");
        }

        this.commandExpression = commandExpression;
        this.expressions = new ArrayList<>();
        this.expressions.add(commandExpression);
        this.expressions.addAll(List.of(otherExpressions));

        if (this.expressions.stream().anyMatch(String::isBlank)) {
            throw new IllegalArgumentException(this.toString() + ": blank string expression");
        }
    }

    /**
     * コマンドの正式な文字列表現を返す.
     * 
     * @return 正式な文字列表現
     */
    public final String commandString() {
        return commandExpression;
    }

    /**
     * コマンド文字列表現のリストを得る. <br>
     * おそらくイミュータブルである.
     * 
     * @return 文字列表現リスト
     */
    final List<String> expressions() {
        return List.copyOf(expressions);
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
                        o -> o.expressions().stream()
                                .map(str -> new Pair<T>(o, str)))
                .toList();

        // オプションの文字列表現定義に重複がないことを確認する.
        Map<String, Long> map = pairs.stream()
                .map(p -> p.expression)
                .collect(groupingBy(s -> s, counting()));
        for (Map.Entry<String, Long> e : map.entrySet()) {
            if (e.getValue().longValue() >= 2) {
                throw new AssertionError("duplicate: " + e.getKey());
            }
        }

        return pairs.stream()
                .collect(toMap(p -> p.expression, p -> p.command));
    }

    // コマンドインスタンスとコマンド文字列のペアを表現するクラス
    private static final class Pair<T extends ConsoleOptionCommand> {
        final T command;
        final String expression;

        Pair(T command, String expression) {
            super();
            this.command = command;
            this.expression = expression;
        }
    }
}
