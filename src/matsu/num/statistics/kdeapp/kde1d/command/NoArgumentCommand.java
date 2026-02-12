/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.12
 */
package matsu.num.statistics.kdeapp.kde1d.command;

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
 * 引数をとらないコマンドを扱うクラス.
 * 各インスタンスはシングルトンで表現される.
 * 
 * @author Matsuura Y.
 */
public final class NoArgumentCommand extends ConsoleOptionCommand {

    /**
     * 結果を標準出力しないことを表現するシングルトンインスタンス.
     */
    public static final NoArgumentCommand ECHO_OFF =
            new NoArgumentCommand("ECHO_OFF", "--echo-off");

    /**
     * 内部から呼ばれる唯一のコンストラクタ.
     */
    private NoArgumentCommand(String enumString, String commandString, String... asString) {
        super(enumString, commandString, asString);
    }

    /**
     * このクラスのシングルトンインスタンスの集合を取得する. <br>
     * 不変コレクション.
     * 
     * @return シングルトンインスタンスの集合
     */
    static Collection<NoArgumentCommand> values() {
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
        static final Collection<NoArgumentCommand> values;

        static {
            List<NoArgumentCommand> constantFieldList = new ArrayList<>();

            Class<NoArgumentCommand> clazz = NoArgumentCommand.class;

            // staticかつ互換性のあるフィールドのみが対象
            for (Field f : NoArgumentCommand.class.getFields()) {
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
    static Optional<NoArgumentCommand> interpret(String commandAsString) {
        return OptionCommandStringInterpreter.interpret(commandAsString);
    }

    private static final class OptionCommandStringInterpreter {

        private static final Map<String, NoArgumentCommand> toOptionMapper;

        static {

            // フラット化
            List<Pair> pairs = NoArgumentCommand.values().stream()
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
        static Optional<NoArgumentCommand> interpret(String commandAsString) {
            return Optional.ofNullable(
                    toOptionMapper.get(Objects.requireNonNull(commandAsString)));
        }

        // option と String のペアを表現するクラス
        private static final class Pair {
            final NoArgumentCommand command;
            final String asString;

            Pair(NoArgumentCommand command, String asString) {
                super();
                this.command = command;
                this.asString = asString;
            }
        }
    }

}
