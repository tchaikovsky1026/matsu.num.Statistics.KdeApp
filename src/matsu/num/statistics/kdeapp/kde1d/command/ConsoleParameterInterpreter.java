/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.6
 */
package matsu.num.statistics.kdeapp.kde1d.command;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import matsu.num.statistics.kdeapp.kde1d.exception.InvalidParameterException;

/**
 * コンソールパラメータの解釈器.
 * 
 * @author Matsuura Y.
 */
public final class ConsoleParameterInterpreter {

    private final Map<ArgumentRequiringCommand<?>, Object> argCommandMapper;
    private final Set<NoArgumentCommand> noArgCommandSet;

    private ConsoleParameterInterpreter(
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
     * 与えられた raw なコンソール引数で解釈された, コンソールパラメータ解釈を返す.
     * 
     * <p>
     * <i>
     * (設計コメント) <br>
     * オプションコマンドの後続の文字列について, バリデーションはされていない.
     * </i>
     * </p>
     * 
     * @param args raw なコンソール引数
     * @return (解釈された) コンソールパラメータ
     * @throws InvalidParameterException パラメータの形式が不正の場合, 必須パラメータが登録されなかった場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public static ConsoleParameterInterpreter from(String[] args)
            throws InvalidParameterException {

        /*
         * パラメータのフォーマットはすべて,
         * "--xxx <arg>" または "--xxx"
         * である.
         * つまり, 必須パラメータ (inputFilePath) もオプションを要求する.
         */

        final int size = args.length;

        int cursor = 0;
        Map<ArgumentRequiringCommand<?>, Object> argCommandMapper =
                new HashMap<>();

        // 引数なしコマンドの設定されている化のセット
        Set<NoArgumentCommand> noArgCommandSet = new HashSet<>();

        while (cursor < size) {
            // オプションコマンドを同定し, 分岐
            String commandAsString = args[cursor];

            // 引数なしコマンドを検索
            {
                Optional<NoArgumentCommand> op =
                        NoArgumentCommand.interpret(commandAsString);
                if (op.isPresent()) {
                    NoArgumentCommand command = op.get();
                    cursor++;

                    // すでにコマンドが登録されていたら例外スロー
                    if (!noArgCommandSet.add(command)) {
                        throw new InvalidParameterException(
                                "duplicate: <" + command.commandString() + ">");
                    }

                    continue;
                }
            }

            // 引数有りコマンドを検索
            {
                Optional<ArgumentRequiringCommand<?>> op =
                        ArgumentRequiringCommand.interpret(commandAsString);
                if (op.isPresent()) {
                    ArgumentRequiringCommand<?> command = op.get();
                    cursor++;

                    // 後続のパラメータが必要な場合, 存在しているかを確かめる
                    if (cursor >= size) {
                        throw new InvalidParameterException(
                                "args lack for <" + command.commandString() + ">");
                    }

                    // すでにコマンドが登録されていたら例外スロー
                    // コンバートに失敗した場合, 例外スロー
                    if (Objects.nonNull(
                            argCommandMapper.put(
                                    command,
                                    command.convertArg(Objects.requireNonNull(args[cursor]))))) {
                        throw new InvalidParameterException(
                                "duplicate: <" + command.commandString() + ">");
                    }
                    cursor++;

                    continue;
                }
            }

            // オプションが不明である場合は例外をスローする
            throw new InvalidParameterException(
                    "unknown command: <" + commandAsString + ">");
        }

        return new ConsoleParameterInterpreter(argCommandMapper, noArgCommandSet);
    }
}
