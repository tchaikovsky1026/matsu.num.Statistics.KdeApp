/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.24
 */
package matsu.num.statistics.kdeapp.comp;

import static java.util.stream.Collectors.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import matsu.num.statistics.kdeapp.exception.IllegalParameterException;
import matsu.num.statistics.kdeapp.logging.AppLogger;

/**
 * 入力されたコンソールパラメータの解釈を扱うクラス.
 * 
 * <p>
 * インスタンスの生成時にコンソールパラメータが解釈され,
 * 即時に解析される. <br>
 * 解析時に, 次が検証される.
 * </p>
 * 
 * <ul>
 * <li>
 * 各コマンドの引数形式に問題ないか <br>
 * ({@link ArgumentRequiringCommand#convertArg(String)} で判断)</li>
 * <li>
 * コマンドの組み合わせに問題ないか <br>
 * ({@link CommandAssignmentRule} で判断)</li>
 * </ul>
 * 
 * @author Matsuura Y.
 */
public final class ConsoleParameterInterpreter {

    private static final AppLogger LOGGER =
            AppLogger.getLogger(ConsoleParameterInterpreter.class);

    private final Map<String, NoArgumentCommand<?>> mapperToNoArgCommand;
    private final Map<String, ArgumentRequiringCommand<?>> mapperToArgCommand;
    private final CommandAssignmentRule rule;

    /**
     * 唯一のコンストラクタ.
     * 引数のバリデーションは行われない.
     * 
     * <p>
     * コマンド文字列が重複しないことを呼び出し元で確かめる必要がある.
     * </p>
     */
    private ConsoleParameterInterpreter(Map<String, NoArgumentCommand<?>> mapperToNoArgCommand,
            Map<String, ArgumentRequiringCommand<?>> mapperToArgCommand,
            CommandAssignmentRule rule) {
        super();
        this.mapperToNoArgCommand = mapperToNoArgCommand;
        this.mapperToArgCommand = mapperToArgCommand;
        this.rule = rule;
    }

    /**
     * 与えられた raw なコンソール引数を解釈する.
     * 
     * <p>
     * オプションコマンドの後続の文字列について, 各コマンドの特性に応じて,
     * また, あらかじめ準備されたコマンドの組み合わせ指定に関するルールに基づいてバリデーションされる.
     * </p>
     * 
     * @param args raw なコンソール引数
     * @return (解釈された) コンソールパラメータ
     * @throws IllegalParameterException パラメータの形式が不正の場合, コマンドの組み合わせが不正の場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public ResolverContainer interpret(String[] args) {

        final int size = args.length;

        int cursor = 0;

        // コマンドの重複を検出するためのコマンドセット
        Set<ConsoleOptionCommand<?>> commandSet = new HashSet<>();

        // コマンドの登録状況
        var propertyBuilder = new ResolverContainer.Builder();

        LOGGER.info("=== Console parameter interpreting ===");

        while (cursor < size) {
            // オプションコマンドを同定し, 分岐
            String commandAsString = args[cursor];
            Objects.requireNonNull(commandAsString);

            // 引数なしコマンドを検索
            NoArgumentCommand<?> noArgCommand =
                    mapperToNoArgCommand.get(commandAsString);
            if (Objects.nonNull(noArgCommand)) {
                LOGGER.info("command=\"" + noArgCommand.commandString() + "\"");

                cursor++;

                // すでにコマンドが登録されていたら例外スロー
                if (!commandSet.add(noArgCommand)) {
                    throw new IllegalParameterException(
                            "duplicate: <" + noArgCommand.commandString() + ">");
                }

                register(propertyBuilder, noArgCommand);

                continue;
            }

            // 引数有りコマンドを検索
            ArgumentRequiringCommand<?> argCommand =
                    mapperToArgCommand.get(commandAsString);
            if (Objects.nonNull(argCommand)) {

                cursor++;

                // 後続のパラメータが必要な場合, 存在しているかを確かめる
                if (cursor >= size) {
                    throw new IllegalParameterException(
                            "args lack for <" + argCommand.commandString() + ">");
                }
                String commandParameter = args[cursor];
                LOGGER.info(
                        "command=\"" + argCommand.commandString() + "\", "
                                + "arg=\"" + commandParameter + "\"");

                // すでにコマンドが登録されていたら例外スロー
                if (!commandSet.add(argCommand)) {
                    throw new IllegalParameterException(
                            "duplicate: <" + argCommand.commandString() + ">");
                }

                register(propertyBuilder, argCommand, commandParameter);
                cursor++;

                continue;
            }

            // オプションが不明である場合は例外をスローする
            throw new IllegalParameterException(
                    "unknown command: <" + commandAsString + ">");
        }

        // パラメータの指定に関するルールでバリデーション
        rule.validate(commandSet);

        LOGGER.info("============== interpreted.");
        return propertyBuilder.build();
    }

    /**
     * 
     * @param <T>
     * @param propertyBuilder
     * @param noArgCommand
     * @return
     */
    private static <T> T register(
            ResolverContainer.Builder propertyBuilder,
            NoArgumentCommand<T> noArgCommand) {
        return propertyBuilder.put(noArgCommand.propertyKey(), noArgCommand.get());
    }

    private static <T> T register(ResolverContainer.Builder propertyBuilder,
            ArgumentRequiringCommand<T> argCommand,
            String commandParameter) {
        return propertyBuilder.put(argCommand.propertyKey(), argCommand.convertArg(commandParameter));
    }

    /**
     * 解釈器を構築する.
     * 
     * <p>
     * 構築の際に, 解釈器が取り扱うことができるコマンドの集合を渡す. <br>
     * ここで渡されたコマンドのみが, {@link #interpret(String[])} で解釈され得る.
     * </p>
     * 
     * <p>
     * 渡されたコマンド集合の中で, コマンド文字列 (representation) が重複してはならない.
     * </p>
     * 
     * @param noArgCommands 解釈される引数なしコマンドの集合
     * @param argCommands 解釈される引数ありコマンドの集合
     * @param rule コマンドの指定ルール
     * @return 解釈器
     * @throws IllegalArgumentException コマンド文字列が重複する場合
     * @throws NullPointerException 引数にnullを含む場合
     */
    public static ConsoleParameterInterpreter of(
            Set<? extends NoArgumentCommand<?>> noArgCommands,
            Set<? extends ArgumentRequiringCommand<?>> argCommands,
            CommandAssignmentRule rule) {

        Set<ConsoleOptionCommand<?>> commands =
                Stream.of(noArgCommands, argCommands)
                        .flatMap(set -> set.stream())
                        .collect(toSet());

        // 文字列に関する重複の確認
        ConsoleOptionCommand.requireNoRepresentationDuplicates(commands);

        Map<String, NoArgumentCommand<?>> mapperToNoArgCommand =
                ConsoleOptionCommand.toCommandMapper(noArgCommands);
        Map<String, ArgumentRequiringCommand<?>> mapperToArgCommand =
                ConsoleOptionCommand.toCommandMapper(argCommands);

        return new ConsoleParameterInterpreter(
                mapperToNoArgCommand, mapperToArgCommand,
                Objects.requireNonNull(rule));
    }
}
