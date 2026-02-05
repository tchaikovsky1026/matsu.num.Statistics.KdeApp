/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.1.30
 */
package matsu.num.statistics.kdeapp.kde1d;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * コンソールオプションコマンドを表現するクラス.
 * 
 * @author Matsuura Y.
 */
@Deprecated
enum ConsoleOptionCommand {

    INPUT_FILE_PATH(true, "--input-file", "-f"),
    COMMENT_CHAR(true, "--comment-char"),
    SEPARATOR(true, "--separator", "-sep"),
    LABEL_HEADER(true, "--label-header"),

    /**
     * 引数をとらないダミーオプション.
     * 
     * <p>
     * 引数をとらないオプションが導入されたら, このオプションは削除する.
     * </p>
     * 
     * @deprecated ダミーオプション, プロダクトコードから参照してはいけない.
     */
    @Deprecated
    DUMMY_NO_ARG(false, "--dummy-no-arg");

    /**
     * オプションに続くパラメータを持つかどうか.
     */
    private final boolean hasArg;

    private final String commandString;
    private final List<String> listOfAsString;

    private ConsoleOptionCommand(boolean hasArg, String commandString, String... asString) {
        this.hasArg = hasArg;
        this.commandString = commandString;

        this.listOfAsString = new ArrayList<>();
        this.listOfAsString.add(commandString);
        this.listOfAsString.addAll(List.of(asString));

        if (this.listOfAsString.stream().anyMatch(String::isBlank)) {
            throw new AssertionError(this.toString() + ": blank string expression");
        }
    }

    /**
     * コマンドの正式な文字列表現を返す.
     * 
     * @return 正式な文字列表現
     */
    String commandString() {
        return commandString;
    }

    /**
     * コマンドに続くべきパラメータの個数を返す.
     * 
     * @return パラメータの個数
     */
    boolean hasArg() {
        return hasArg;
    }

    /**
     * 与えられた文字列からオプションコマンドを解釈して返す.
     * 
     * @param commandAsString 文字列表現
     * @return 該当するオプションコマンド, 該当なしなら空
     * @throws NullPointerException 引数がnull
     */
    static Optional<ConsoleOptionCommand> interpret(String commandAsString) {
        return OptionCommandStringInterpreter.interpret(commandAsString);
    }

    private static final class OptionCommandStringInterpreter {

        private static final Map<String, ConsoleOptionCommand> toOptionMapper;

        static {

            // フラット化
            List<Pair> pairs = Arrays.stream(ConsoleOptionCommand.values())
                    .flatMap(
                            o -> o.listOfAsString.stream()
                                    .map(str -> new Pair(o, str)))
                    .toList();

            // オプションの文字列表現定義に重複がないことを確認する.
            Map<String, Long> map = pairs.stream()
                    .map(p -> p.asString)
                    .collect(groupingBy(s -> s, counting()));
            for (Map.Entry<String, Long> e : map.entrySet()) {
                if (e.getValue().longValue() >= 2) {
                    throw new AssertionError("duplicate: " + e.getKey());
                }
            }

            toOptionMapper = pairs.stream()
                    .collect(toMap(p -> p.asString, p -> p.command));
        }

        /**
         * 与えられた文字列からオプションコマンドを解釈して返す.
         * 
         * @throws NullPointerException 引数がnull
         */
        static Optional<ConsoleOptionCommand> interpret(String commandAsString) {
            return Optional.ofNullable(
                    toOptionMapper.get(Objects.requireNonNull(commandAsString)));
        }

        // option と String のペアを表現するクラス
        private static final class Pair {
            final ConsoleOptionCommand command;
            final String asString;

            Pair(ConsoleOptionCommand command, String asString) {
                super();
                this.command = command;
                this.asString = asString;
            }
        }
    }
}
