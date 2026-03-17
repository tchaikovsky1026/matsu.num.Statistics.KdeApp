/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.17
 */
package matsu.num.statistics.kdeapp.help;

import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.stream.StreamSupport;

import matsu.num.statistics.kdeapp.command.ArgumentRequiringCommand;
import matsu.num.statistics.kdeapp.command.ConsoleOptionCommand;
import matsu.num.statistics.kdeapp.command.NoArgumentCommand;

/**
 * コマンドの説明を表現するクラス.
 * 
 * @author Matsuura Y.
 */
public final class CommandDescription {

    private final ConsoleOptionCommand command;
    private final String description;
    private final CommandCategory category;

    private UnaryOperator<String> mapperToUsageSyntax;

    /**
     * 唯一のコンストラクタ.
     * 
     * @param command このインスタンスに紐づくコマンド
     * @param description 説明, nullやブランクの場合は代替文字列
     * @param category コマンドカテゴリ
     * @param mapperToUsageSyntax マッパ
     * @throws NullPointerException null非許容な引数が与えられた場合
     */
    private CommandDescription(
            ConsoleOptionCommand command, String description, CommandCategory category,
            UnaryOperator<String> mapperToUsageSyntax) {
        super();
        this.command = Objects.requireNonNull(command);
        this.description = canonicalize(description);
        this.category = Objects.requireNonNull(category);

        this.mapperToUsageSyntax = Objects.requireNonNull(mapperToUsageSyntax);
    }

    /**
     * このコマンドの説明を返す.
     * 
     * @return 説明
     */
    final String description() {
        return description;
    }

    /**
     * コマンドの usage syntax を列挙した文字列集合を返す.
     * 
     * <p>
     * 並び順は規定されていないが, おそらくコマンド文字列の長い順 (full name が最初) であろう.
     * </p>
     * 
     * @return usage syntaxes
     */
    final List<String> getUsageSyntaxes() {
        return command.representations(comparingInt(String::length).reversed())
                .stream()
                .map(mapperToUsageSyntax)
                .toList();
    }

    /**
     * 引数を取らないコマンドについて, コマンド説明を構築する.
     * 
     * @param command コマンド
     * @param description 説明 (nullやブランクの場合は代替文字列に置き換えられる)
     * @param category コマンドカテゴリ
     * @return コマンド説明
     * @throws NullPointerException null非許容な引数が与えられた場合
     */
    public static CommandDescription of(
            NoArgumentCommand command, String description, CommandCategory category) {
        return new CommandDescription(
                command, description, category,
                UnaryOperator.identity());
    }

    /**
     * 引数を取るコマンドについて, コマンド説明を構築する.
     * 
     * <p>
     * 引数名は, ブランク禁止かつ, 空白を含んではいけない.
     * </p>
     * 
     * @param command コマンド
     * @param argName 引数名
     * @param description 説明 (nullやブランクの場合は代替文字列に置き換えられる)
     * @param category コマンドカテゴリ
     * @return コマンド説明
     * @throws IllegalArgumentException argNameが不正の場合
     * @throws NullPointerException null非許容な引数が与えられた場合
     */
    public static CommandDescription of(
            ArgumentRequiringCommand<?> command, String argName,
            String description, CommandCategory category) {

        validateArgName(argName);
        return new CommandDescription(
                command, description, category,
                cn -> cn + " " + argName);
    }

    /**
     * description を正規化する.
     * 
     * <p>
     * 正規化とは, null や ブランクに対するフォローである. <br>
     * 合わせて前後の white space は削除される.
     * </p>
     * 
     * @param description 文字列 (null許容)
     * @return 正規化結果
     */
    private static String canonicalize(String description) {
        return Objects.isNull(description) || description.isBlank()
                ? "no description"
                : description.strip();
    }

    /**
     * コマンドの引数名 (argName) をバリデーションする.
     * 
     * <p>
     * 引数名は, ブランク禁止かつ, 空白を含んではいけない. <br>
     * これが満たされない場合に例外スロー.
     * </p>
     * 
     * @param argName コマンドの引数名
     * @throws IllegalArgumentException argNameが不正の場合
     * @throws NullPointerException 引数がnullの場合
     */
    private static void validateArgName(String argName) {
        if (argName.isBlank()) {
            throw new IllegalArgumentException("argName is blank");
        }
        if (argName.chars().anyMatch(Character::isWhitespace)) {
            throw new IllegalArgumentException("argName includes white space");
        }
    }

    /**
     * コマンド説明をカテゴリでグルーピングする.
     * 
     * <p>
     * マップの key, value リストの順番は, Iterable の登場順と一致する.
     * </p>
     * 
     * @param commands コマンド説明の集合
     * @return グルーピングの結果
     * @throws NullPointerException 引数やその要素にnullを含む場合
     */
    static Map<CommandCategory, List<CommandDescription>> groupingByCategory(
            Iterable<? extends CommandDescription> commands) {

        return StreamSupport.stream(commands.spliterator(), false)
                .<CommandDescription> map(c -> c)
                .collect(
                        groupingBy(c -> c.category, LinkedHashMap::new, toList()));
    }
}
