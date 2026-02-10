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

import static java.util.stream.Collectors.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import matsu.num.statistics.kdeapp.kde1d.exception.InvalidParameterException;

/**
 * 引数をとるコマンドを扱うクラス.
 * 各インスタンスはシングルトンで表現される.
 * 
 * @author Matsuura Y.
 * @param <T> コマンドの引数の変更先の型, see {@link #convertArg(String)}
 */
public final class ArgumentRequiringCommand<T> extends ConsoleOptionCommand {

    /**
     * 入力ファイルの指定を表現するシングルトンインスタンス.
     * 
     * <p>
     * 引数はバリデーションされない.
     * </p>
     */
    public static final ArgumentRequiringCommand<String> INPUT_FILE_PATH =
            identifying("INPUT_FILE_PATH", "--input-file", "-f");

    /**
     * 出力ファイルの指定を表現するシングルトンインスタンス.
     * 
     * <p>
     * 引数はバリデーションされない.
     * </p>
     */
    public static final ArgumentRequiringCommand<String> OUTPUT_FORCE_FILE_PATH =
            identifying("OUTPUT_FORCE_FILE_PATH", "--output-force", "-out-f");

    /**
     * 入力のコメント行の prefix の指定を表現するシングルトンインスタンス.
     * 
     * <p>
     * 引数はバリデーションされない.
     * </p>
     */
    public static final ArgumentRequiringCommand<String> COMMENT_CHAR =
            identifying("COMMENT_CHAR", "--comment-char");

    /**
     * 区切り文字の指定を表現するシングルトンインスタンス.
     * 
     * <p>
     * 引数はバリデーションされたうえで, {@code char} に変換される. <br>
     * 区切り文字の正当性ルールは次の通りである. <br>
     * (エスケープシーケンスは, 列挙定数で用意されている,
     * 自動テストで文字列出力される.)
     * </p>
     * 
     * <ul>
     * <li>ASCII 1文字</li>
     * <li>エスケープシーケンス</li>
     * </ul>
     */
    public static final ArgumentRequiringCommand<Character> SEPARATOR =
            new ArgumentRequiringCommand<>(
                    "SEPARATOR", Character.class,
                    SeparatorInterpreter::from,
                    "--separator", "-sep");

    /**
     * 出力のラベルに付与する prefix の指定を表現するシングルトンインスタンス.
     * 
     * <p>
     * 引数はバリデーションされない.
     * </p>
     */
    public static final ArgumentRequiringCommand<String> LABEL_HEADER =
            identifying("LABEL_HEADER", "--label-header");

    private final Class<T> valueType;
    private final Function<? super String, ? extends T> converter;

    /**
     * 内部から呼ばれる唯一のコンストラクタ.
     * 
     * <p>
     * コンバータは, コンバートできない場合 (例外をスローすべき引数の場合) は {@code null} を返す. <br>
     * 正常系で {@code null} を返してはいけない. <br>
     * コンバータ (Function) に {@code null} が与えられることはない.
     * </p>
     * 
     * @param valueType 変更先の型トークン
     * @param converter コンバータ
     */
    private ArgumentRequiringCommand(
            String enumString,
            Class<T> valueType, Function<? super String, ? extends T> converter,
            String commandString, String... asString) {
        super(enumString, commandString, asString);

        this.valueType = valueType;
        this.converter = converter;
    }

    /**
     * コンバータに恒等写像を与える形式で, インスタンスを生成.
     */
    private static ArgumentRequiringCommand<String> identifying(
            String enumString, String commandString, String... asString) {

        return new ArgumentRequiringCommand<String>(
                enumString, String.class, s -> s,
                commandString, asString);
    }

    /**
     * 与えたインスタンスを自身の型にキャストする.
     * 
     * <p>
     * {@code null} が与えられれば {@code null} を返す.
     * </p>
     * 
     * @param obj インスタンス
     * @return キャストしたobj
     * @throws ClassCastException キャストに失敗した場合
     */
    final T cast(Object obj) {
        return this.valueType.cast(obj);
    }

    /**
     * 文字列として与えられる引数を, このコマンドで解釈できる形に変換する. <br>
     * 変換に関しては, シングルトン定数の説明に書かれるべきである.
     * 
     * @param arg 文字列
     * @return 変換後の値
     * @throws InvalidParameterException パラメータ不正の場合
     * @throws NullPointerException 引数がnullの場合(必ず)
     */
    final T convertArg(String arg) {
        T out = converter.apply(Objects.requireNonNull(arg));
        if (Objects.isNull(out)) {
            throw new InvalidParameterException(
                    "invalid-arg for <" + this.commandString() + ">: \"" + arg + "\"");
        }
        return out;
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
