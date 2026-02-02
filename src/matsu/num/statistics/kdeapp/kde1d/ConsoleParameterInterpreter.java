/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.2
 */
package matsu.num.statistics.kdeapp.kde1d;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * コンソールパラメータの解釈器.
 * 
 * @author Matsuura Y.
 */
final class ConsoleParameterInterpreter {

    private final Map<ConsoleOptionCommand, Optional<String>> optionMapper;

    private ConsoleParameterInterpreter(
            Map<ConsoleOptionCommand, Optional<String>> optionMapper) {
        this.optionMapper = Objects.requireNonNull(optionMapper);
    }

    /**
     * オプションの値を取得する.
     * 
     * <p>
     * 戻り値型は二重のオプショナルである. <br>
     * オプションが指定されていない場合は, 外側が空である.
     * </p>
     * 
     * <p>
     * 内側のオプショナルは, コマンドの性質に依存する. <br>
     * コマンドがパラメータをとる場合 ({@link ConsoleOptionCommand#hasArg()}),
     * 内側はパラメータである文字列をラップしたオプショナルを返す. <br>
     * パラメータをとらない場合, 内側は空になる.
     * </p>
     * 
     * <p>
     * <i>
     * (設計コメント) <br>
     * もしかすると, 独自のクラスで表現したほうが良いかもしれない.
     * </i>
     * </p>
     * 
     * @param option
     * @return オプションの値, 指定されていない場合は外側が空, 内側はメソッド説明文を参照.
     * @throws NullPointerException 引数がnullの場合
     */
    Optional<Optional<String>> valueOf(ConsoleOptionCommand option) {
        return Optional.ofNullable(optionMapper.get(Objects.requireNonNull(option)));
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
        Map<ConsoleOptionCommand, Optional<String>> optionMapper =
                new EnumMap<>(ConsoleOptionCommand.class);
        while (cursor < size) {
            // オプションコマンドを同定
            String commandAsString = args[cursor];
            ConsoleOptionCommand command = ConsoleOptionCommand.interpret(commandAsString)
                    .orElseThrow(
                            () -> new InvalidParameterException(
                                    "unknown command: <" + commandAsString + ">"));
            cursor++;

            // オプションの重複確認
            if (optionMapper.keySet().contains(command)) {
                throw new InvalidParameterException(
                        "duplicate: <" + command.commandString() + ">");
            }

            // 後続のパラメータが必要ない場合は, オプションコマンドの存在を記録
            if (!command.hasArg()) {
                optionMapper.put(command, Optional.empty());
                continue;
            }

            // 後続のパラメータが必要な場合, 存在しているかを確かめる
            if (cursor >= size) {
                throw new InvalidParameterException(
                        "args lack: <" + command.commandString() + ">");
            }
            optionMapper.put(command, Optional.of(args[cursor]));
            cursor++;
        }

        return new ConsoleParameterInterpreter(optionMapper);
    }
}
