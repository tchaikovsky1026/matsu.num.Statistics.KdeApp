/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.6
 */
package matsu.num.statistics.kdeapp.kde1d;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * コンソールパラメータの解釈器.
 * 
 * @author Matsuura Y.
 */
final class ConsoleParameterInterpreter {

    private final Map<ArgumentRequiringCommand<?>, String> argCommandMapper;
    private final Set<NoArgumentCommand> noArgCommandSet;

    private ConsoleParameterInterpreter(
            Map<ArgumentRequiringCommand<?>, String> argCommandMapper,
            Set<NoArgumentCommand> noArgCommandSet) {
        this.argCommandMapper = Objects.requireNonNull(argCommandMapper);
        this.noArgCommandSet = Objects.requireNonNull(noArgCommandSet);
    }

    /**
     * 引数有りオプションの値を取得する.
     * 
     * <p>
     * 戻り値型はオプショナルである. <br>
     * オプションが指定されていない場合は, 空である.
     * </p>
     * 
     * @param command オプションの属性
     * @return オプションの値, 指定されていない場合は空.
     * @throws NullPointerException 引数がnullの場合
     */
    Optional<String> valueOf(ArgumentRequiringCommand<?> command) {
        return Optional.ofNullable(argCommandMapper.get(Objects.requireNonNull(command)));
    }

    /**
     * 引数無しオプションが指定されているかを判定する.
     * 
     * @param option オプションの属性
     * @return オプションの値, 指定されていない場合は空.
     * @throws NullPointerException 引数がnullの場合
     */
    boolean contains(NoArgumentCommand option) {
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
     * @throws InvalidParameterException パラメータの形式が不正の場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    static ConsoleParameterInterpreter from(String[] args)
            throws InvalidParameterException {

        /*
         * パラメータのフォーマットはすべて,
         * "--xxx <arg>" または "--xxx"
         * である.
         * つまり, 必須パラメータ (inputFilePath) もオプションを要求する.
         */

        final int size = args.length;

        int cursor = 0;
        Map<ArgumentRequiringCommand<?>, String> argCommandMapper =
                new HashMap<>();
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

                    if (noArgCommandSet.contains(command)) {
                        throw new InvalidParameterException(
                                "duplicate: <" + command.commandString() + ">");
                    }
                    noArgCommandSet.add(command);

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

                    if (argCommandMapper.keySet().contains(command)) {
                        throw new InvalidParameterException(
                                "duplicate: <" + command.commandString() + ">");
                    }

                    // 後続のパラメータが必要な場合, 存在しているかを確かめる
                    if (cursor >= size) {
                        throw new InvalidParameterException(
                                "args lack: <" + command.commandString() + ">");
                    }
                    argCommandMapper.put(command, Objects.requireNonNull(args[cursor]));
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
