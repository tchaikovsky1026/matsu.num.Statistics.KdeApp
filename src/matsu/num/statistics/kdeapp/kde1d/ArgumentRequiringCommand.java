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
 * 引数をとるコマンドを扱うクラス.
 * 各インスタンスはシングルトンで表現される.
 * 
 * @author Matsuura Y.
 * @param <T> コマンドの引数の変更先の型, see {@link #convertArg(String)}
 */
final class ArgumentRequiringCommand<T> extends ConsoleOptionCommand {

    /**
     * 入力ファイルの指定を表現するシングルトンインスタンス.
     */
    public static final ArgumentRequiringCommand<String> INPUT_FILE_PATH =
            new ArgumentRequiringCommand<>("INPUT_FILE_PATH", "--input-file", "-f");

    /**
     * 入力のコメント行の prefix の指定を表現するシングルトンインスタンス.
     */
    public static final ArgumentRequiringCommand<String> COMMENT_CHAR =
            new ArgumentRequiringCommand<>("COMMENT_CHAR", "--comment-char");

    /**
     * 区切り文字の指定を表現するシングルトンインスタンス.
     */
    public static final ArgumentRequiringCommand<Character> SEPARATOR =
            new ArgumentRequiringCommand<>("SEPARATOR", "--separator", "-sep");

    /**
     * 出力のラベルに付与する prefix の指定を表現するシングルトンインスタンス.
     */
    public static final ArgumentRequiringCommand<String> LABEL_HEADER =
            new ArgumentRequiringCommand<>("LABEL_HEADER", "--label-header");

    /**
     * 内部から呼ばれる唯一のコンストラクタ.
     */
    private ArgumentRequiringCommand(String enumString, String commandString, String... asString) {
        super(enumString, commandString, asString);
    }

    /**
     * このクラスのシングルトンインスタンスの集合を取得する. <br>
     * 不変コレクション.
     * 
     * @return シングルトンインスタンスの集合
     */
    static Collection<ArgumentRequiringCommand<?>> values() {
        // ここはリフレクションで処理したい
        return SingletonHolder.values;
    }

    /**
     * テスト用. <br>
     * シングルトンインスタンスの全コマンド文字列表現のリストを得る.
     * 
     * @return 文字列表現リスト
     */
    static List<String> allExpressions() {

        return values().stream()
                .flatMap(command -> command.expressions().stream())
                .toList();
    }

    private static final class SingletonHolder {

        /**
         * オプションコマンドの集合. <br>
         * 不変になるようにすること.
         */
        static final Collection<ArgumentRequiringCommand<?>> values;

        static {
            List<ArgumentRequiringCommand<?>> constantFieldList = new ArrayList<>();

            @SuppressWarnings("rawtypes")
            Class<ArgumentRequiringCommand> clazz = ArgumentRequiringCommand.class;

            // staticかつ互換性のあるフィールドのみが対象
            for (Field f : ArgumentRequiringCommand.class.getFields()) {
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

    /**
     * 与えられた文字列からオプションコマンドを解釈して返す.
     * 
     * @param commandAsString 文字列表現
     * @return 該当するオプションコマンド, 該当なしなら空
     * @throws NullPointerException 引数がnull
     */
    static Optional<ArgumentRequiringCommand<?>> interpret(String commandAsString) {
        return OptionCommandStringInterpreter.interpret(commandAsString);
    }

    private static final class OptionCommandStringInterpreter {

        private static final Map<String, ArgumentRequiringCommand<?>> toOptionMapper;

        static {

            // フラット化
            List<Pair> pairs = ArgumentRequiringCommand.values().stream()
                    .flatMap(
                            o -> o.expressions().stream()
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
        static Optional<ArgumentRequiringCommand<?>> interpret(String commandAsString) {
            return Optional.ofNullable(
                    toOptionMapper.get(Objects.requireNonNull(commandAsString)));
        }

        // option と String のペアを表現するクラス
        private static final class Pair {
            final ArgumentRequiringCommand<?> command;
            final String asString;

            Pair(ArgumentRequiringCommand<?> command, String asString) {
                super();
                this.command = command;
                this.asString = asString;
            }
        }
    }
}
