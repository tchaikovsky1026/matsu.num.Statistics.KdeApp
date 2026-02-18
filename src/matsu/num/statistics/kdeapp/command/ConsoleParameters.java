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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import matsu.num.statistics.kdeapp.exception.IllegalParameterException;

/**
 * 入力されたコンソールパラメータを扱うクラス.
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
 * <p>
 * インスタンス生成は, {@link ConsoleParameters.Interpreter} によって行う.
 * </p>
 * 
 * @author Matsuura Y.
 */
public final class ConsoleParameters {

    private final Map<ArgumentRequiringCommand<?>, Object> argCommandMapper;
    private final Set<NoArgumentCommand> noArgCommandSet;

    private ConsoleParameters(
            Map<ArgumentRequiringCommand<?>, Object> argCommandMapper,
            Set<NoArgumentCommand> noArgCommandSet) {
        this.argCommandMapper = Objects.requireNonNull(argCommandMapper);
        this.noArgCommandSet = Objects.requireNonNull(noArgCommandSet);
    }

    /**
     * 引数有りオプションの値を取得する.
     * 
     * <p>
     * 戻り値型はオプショナルである.
     * </p>
     * 
     * @param <T> オプション引数の変換後の型
     * @param command オプションの属性
     * @return オプションの値, 指定されていない場合は空.
     * @throws NullPointerException 引数がnullの場合
     */
    public <T> Optional<T> valueOf(ArgumentRequiringCommand<T> command) {
        return Optional.ofNullable(
                command.cast(argCommandMapper.get(Objects.requireNonNull(command))));
    }

    /**
     * 引数無しオプションが指定されているかを判定する.
     * 
     * @param option オプションの属性
     * @return オプションの値, 指定されていない場合は空.
     * @throws NullPointerException 引数がnullの場合
     */
    public boolean contains(NoArgumentCommand option) {
        return noArgCommandSet.contains(Objects.requireNonNull(option));
    }

    /**
     * 
     * コンソールパラメータの解釈器を表す.
     * 
     * <p>
     * 解釈の仕様は, {@link ConsoleParameters} の説明の通り.
     * </p>
     */
    public static final class Interpreter {

        private final Map<String, NoArgumentCommand> mapperToNoArgCommand;
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
        private Interpreter(Map<String, NoArgumentCommand> mapperToNoArgCommand,
                Map<String, ArgumentRequiringCommand<?>> mapperToArgCommand, CommandAssignmentRule rule) {
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
        public ConsoleParameters interpret(String[] args) {

            final int size = args.length;

            int cursor = 0;
            Map<ArgumentRequiringCommand<?>, Object> argCommandMapper =
                    new HashMap<>();

            // 引数なしコマンドの設定されているものセット
            Set<NoArgumentCommand> noArgCommandSet = new HashSet<>();

            while (cursor < size) {
                // オプションコマンドを同定し, 分岐
                String commandAsString = args[cursor];
                Objects.requireNonNull(commandAsString);

                // 引数なしコマンドを検索
                {
                    NoArgumentCommand noArgCommand =
                            mapperToNoArgCommand.get(commandAsString);
                    if (Objects.nonNull(noArgCommand)) {
                        cursor++;

                        // すでにコマンドが登録されていたら例外スロー
                        if (!noArgCommandSet.add(noArgCommand)) {
                            throw new IllegalParameterException(
                                    "duplicate: <" + noArgCommand.commandString() + ">");
                        }

                        continue;
                    }
                }

                // 引数有りコマンドを検索
                {
                    ArgumentRequiringCommand<?> argCommand =
                            mapperToArgCommand.get(commandAsString);
                    if (Objects.nonNull(argCommand)) {
                        cursor++;

                        // 後続のパラメータが必要な場合, 存在しているかを確かめる
                        if (cursor >= size) {
                            throw new IllegalParameterException(
                                    "args lack for <" + argCommand.commandString() + ">");
                        }

                        // すでにコマンドが登録されていたら例外スロー
                        // コンバートに失敗した場合, 例外スロー
                        if (Objects.nonNull(
                                argCommandMapper.put(
                                        argCommand,
                                        argCommand.convertArg(Objects.requireNonNull(args[cursor]))))) {
                            throw new IllegalParameterException(
                                    "duplicate: <" + argCommand.commandString() + ">");
                        }
                        cursor++;

                        continue;
                    }
                }

                // オプションが不明である場合は例外をスローする
                throw new IllegalParameterException(
                        "unknown command: <" + commandAsString + ">");
            }

            // パラメータの指定に関するルールでバリデーション
            Set<ConsoleOptionCommand> commandSet = new HashSet<>(noArgCommandSet);
            commandSet.addAll(argCommandMapper.keySet());
            rule.validate(commandSet);

            return new ConsoleParameters(argCommandMapper, noArgCommandSet);
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
         * 渡されたコマンド集合の中で, コマンド文字列 (expression) が重複してはならない.
         * </p>
         * 
         * @param noArgCommands 解釈される引数なしコマンドの集合
         * @param argCommands 解釈される引数ありコマンドの集合
         * @param rule コマンドの指定ルール
         * @return 解釈器
         * @throws IllegalArgumentException コマンド文字列が重複する場合
         * @throws NullPointerException 引数にnullを含む場合
         */
        public static Interpreter of(
                Set<? extends NoArgumentCommand> noArgCommands,
                Set<? extends ArgumentRequiringCommand<?>> argCommands,
                CommandAssignmentRule rule) {

            Map<String, NoArgumentCommand> mapperToNoArgCommand =
                    ConsoleOptionCommand.toCommandMapper(noArgCommands);
            Map<String, ArgumentRequiringCommand<?>> mapperToArgCommand =
                    ConsoleOptionCommand.toCommandMapper(argCommands);

            validateCommandDuplication(
                    mapperToNoArgCommand.keySet(), mapperToArgCommand.keySet());

            return new Interpreter(
                    mapperToNoArgCommand, mapperToArgCommand,
                    Objects.requireNonNull(rule));
        }

        /**
         * {@link NoArgumentCommand} と {@link ArgumentRequiringCommand}
         * の文字列表現に重複がないことを確かめる.
         * 
         * @throws IllegalArgumentException コマンド文字列が重複する場合
         * @throws NullPointerException nullを含む場合
         */
        private static void validateCommandDuplication(
                Set<String> noArgCommandExpressions,
                Set<String> argCommandExpressions) {

            Set<String> merged = new HashSet<>(argCommandExpressions);
            merged.addAll(noArgCommandExpressions);

            int overlapCount = argCommandExpressions.size() +
                    noArgCommandExpressions.size() - merged.size();

            if (overlapCount > 0) {
                throw new IllegalArgumentException(
                        "command expressions are duplicated");
            }
        }
    }
}
