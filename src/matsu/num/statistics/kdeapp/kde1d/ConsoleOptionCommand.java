/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.5
 */
package matsu.num.statistics.kdeapp.kde1d;

import static java.util.stream.Collectors.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * コンソールオプションコマンドの列挙を表現するクラス.
 * 
 * @author Matsuura Y.
 * @param <T> コマンドの引数の変更先の型, see {@link #convertArg(String)}
 */
final class ConsoleOptionCommand<T> {

    /**
     * 入力ファイルの指定を表現するシングルトンインスタンス.
     * 
     * <p>
     * 要arg.
     * </p>
     */
    public static final ConsoleOptionCommand<String> INPUT_FILE_PATH =
            new ConsoleOptionCommand<>("INPUT_FILE_PATH", true, "--input-file", "-f");

    /**
     * 入力のコメント行の prefix の指定を表現するシングルトンインスタンス.
     * 
     * <p>
     * 要arg.
     * </p>
     */
    public static final ConsoleOptionCommand<String> COMMENT_CHAR =
            new ConsoleOptionCommand<>("COMMENT_CHAR", true, "--comment-char");

    /**
     * 区切り文字の指定を表現するシングルトンインスタンス.
     * 
     * <p>
     * 要arg.
     * </p>
     */
    public static final ConsoleOptionCommand<Character> SEPARATOR =
            new ConsoleOptionCommand<>("SEPARATOR", true, "--separator", "-sep");

    /**
     * 出力のラベルに付与する prefix の指定を表現するシングルトンインスタンス.
     * 
     * <p>
     * 要arg.
     * </p>
     */
    public static final ConsoleOptionCommand<String> LABEL_HEADER =
            new ConsoleOptionCommand<>("LABEL_HEADER", true, "--label-header");

    /**
     * 引数をとらないダミーオプション, シングルトンインスタンス.
     * 
     * <p>
     * 引数をとらないオプションが導入されたら, このオプションは削除する.
     * </p>
     * 
     * @deprecated ダミーオプション, プロダクトコードから参照してはいけない.
     */
    @Deprecated
    public static final ConsoleOptionCommand<?> DUMMY_NO_ARG =
            new ConsoleOptionCommand<>("DUMMY_NO_ARG", false, "--dummy-no-arg");

    private final String enumString;

    /**
     * オプションに続くパラメータを持つかどうか.
     */
    private final boolean hasArg;

    private final String commandString;
    private final List<String> listOfAsString;

    private ConsoleOptionCommand(String enumString,
            boolean hasArg, String commandString, String... asString) {

        this.enumString = enumString;

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
     * このインスタンスの文字列表現を返す.
     */
    @Override
    public String toString() {
        return this.enumString;
    }

    /**
     * 与えられた文字列からオプションコマンドを解釈して返す.
     * 
     * @param commandAsString 文字列表現
     * @return 該当するオプションコマンド, 該当なしなら空
     * @throws NullPointerException 引数がnull
     */
    static Optional<ConsoleOptionCommand<?>> interpret(String commandAsString) {
        return OptionCommandStringInterpreter.interpret(commandAsString);
    }

    /**
     * このクラスのシングルトンインスタンスの集合を取得する. <br>
     * 不変コレクション.
     * 
     * @return シングルトンインスタンスの集合
     */
    static Collection<ConsoleOptionCommand<?>> values() {
        // ここはリフレクションで処理したい
        return SingletonHolder.values;
    }

    private static final class OptionCommandStringInterpreter {

        private static final Map<String, ConsoleOptionCommand<?>> toOptionMapper;

        static {

            // フラット化
            List<Pair> pairs = ConsoleOptionCommand.values().stream()
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
        static Optional<ConsoleOptionCommand<?>> interpret(String commandAsString) {
            return Optional.ofNullable(
                    toOptionMapper.get(Objects.requireNonNull(commandAsString)));
        }

        // option と String のペアを表現するクラス
        private static final class Pair {
            final ConsoleOptionCommand<?> command;
            final String asString;

            Pair(ConsoleOptionCommand<?> command, String asString) {
                super();
                this.command = command;
                this.asString = asString;
            }
        }
    }

    private static final class SingletonHolder {

        /**
         * オプションコマンドの集合. <br>
         * 不変になるようにすること.
         */
        static final Collection<ConsoleOptionCommand<?>> values;

        static {
            // ここはリフレクションで処理したい
            //            values = List.of(INPUT_FILE_PATH, COMMENT_CHAR, SEPARATOR, LABEL_HEADER, DUMMY_NO_ARG);

            List<ConsoleOptionCommand<?>> constantFieldList = new ArrayList<>();

            @SuppressWarnings("rawtypes")
            Class<ConsoleOptionCommand> clazz = ConsoleOptionCommand.class;

            // staticかつ互換性のあるフィールドのみが対象
            for (Field f : ConsoleOptionCommand.class.getFields()) {
                if ((f.getModifiers() & Modifier.STATIC) == 0) {
                    continue;
                }
                try {
                    constantFieldList.add(clazz.cast(f.get(null)));
                } catch (IllegalAccessException | ClassCastException ignore) {
                    //無関係なフィールドなら無視する
                }
            }

            values = List.copyOf(constantFieldList);
        }
    }
}
