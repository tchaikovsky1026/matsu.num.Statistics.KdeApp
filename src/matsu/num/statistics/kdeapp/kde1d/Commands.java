/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.22
 */
package matsu.num.statistics.kdeapp.kde1d;

import static matsu.num.statistics.kdeapp.command.ArgumentRequiringCommand.*;
import static matsu.num.statistics.kdeapp.command.CommandAssignmentRule.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import matsu.num.statistics.kdeapp.command.ArgumentRequiringCommand;
import matsu.num.statistics.kdeapp.command.CommandAssignmentRule;
import matsu.num.statistics.kdeapp.command.ConsoleOptionCommand;
import matsu.num.statistics.kdeapp.command.ConsoleParameters;
import matsu.num.statistics.kdeapp.command.NoArgumentCommand;
import matsu.num.statistics.kdeapp.format.CommentPrefix;
import matsu.num.statistics.kdeapp.format.Separator;

/**
 * kde1d で取り扱う, コマンドに関するルールなど.
 * 
 * @author Matsuura Y.
 */
public final class Commands {

    /**
     * 結果を標準出力しないことを表現するシングルトンインスタンス.
     */
    public static final NoArgumentCommand ECHO_OFF =
            NoArgumentCommand.of("ECHO_OFF", "--echo-off");

    /**
     * 結果を標準出力することを表現するシングルトンインスタンス.
     */
    public static final NoArgumentCommand ECHO_ON =
            NoArgumentCommand.of("ECHO_ON", "--echo-on");

    /**
     * 入力ファイルの指定を表現するシングルトンインスタンス.
     * 
     * <p>
     * 引数は {@link Path} への変換される.
     * </p>
     */
    public static final ArgumentRequiringCommand<Path> INPUT_FILE_PATH =
            ArgumentRequiringCommand.of(
                    "INPUT_FILE_PATH", Path.class,
                    Path::of,
                    "--input", "--in");

    /**
     * 強制上書きモードによる出力ファイルの指定を表現するシングルトンインスタンス.
     * 
     * <p>
     * 引数は {@link Path} への変換される.
     * </p>
     */
    public static final ArgumentRequiringCommand<Path> OUTPUT_FORCE_FILE_PATH =
            ArgumentRequiringCommand.of(
                    "OUTPUT_FORCE_FILE_PATH", Path.class,
                    Path::of,
                    "--output-force", "--out-force");

    /**
     * 上書き禁止モードである出力ファイルの指定を表現するシングルトンインスタンス.
     * 
     * <p>
     * 引数は {@link Path} への変換される.
     * </p>
     */
    public static final ArgumentRequiringCommand<Path> OUTPUT_FILE_PATH =
            ArgumentRequiringCommand.of(
                    "OUTPUT_FILE_PATH", Path.class,
                    Path::of,
                    "--output", "--out");

    /**
     * ファイル出力しないことを表現するシングルトンインスタンス.
     */
    public static final NoArgumentCommand OUTPUT_NONE =
            NoArgumentCommand.of(
                    "OUTPUT_NONE",
                    "--output-none", "--out-none");

    /**
     * 入力のコメント行の prefix の指定を表現するシングルトンインスタンス.
     * 
     * <p>
     * インスタンス生成時にバリデーションされる.
     * </p>
     */
    public static final ArgumentRequiringCommand<CommentPrefix> INPUT_COMMENT_PREFIX =
            ArgumentRequiringCommand.of(
                    "INPUT_COMMENT_PREFIX", CommentPrefix.class,
                    CommentPrefix::of,
                    "--input-comment-prefix", "--in-comment-prefix");

    /**
     * 区切り文字の指定を表現するシングルトンインスタンス.
     * 
     * <p>
     * インスタンス生成時にバリデーションされる.
     * </p>
     */
    public static final ArgumentRequiringCommand<Separator> OUTPUT_SEPARATOR =
            ArgumentRequiringCommand.of(
                    "OUTPUT_SEPARATOR", Separator.class,
                    Separator::from,
                    "--output-separator", "--out-sep");

    /**
     * 出力のラベルに付与する prefix の指定を表現するシングルトンインスタンス.
     * 
     * <p>
     * 引数はバリデーションされない.
     * </p>
     */
    public static final ArgumentRequiringCommand<String> OUTPUT_LABEL_PREFIX =
            identifying("OUTPUT_LABEL_PREFIX", "--output-label-prefix", "--out-label-prefix");

    /**
     * コマンドの指定に関するルール.
     */
    private static final CommandAssignmentRule COMMAND_ASSIGNMENT_RULE;

    static {
        COMMAND_ASSIGNMENT_RULE = composite(
                singleRequiredRule(INPUT_FILE_PATH),
                singleOptionalRule(OUTPUT_FILE_PATH, OUTPUT_FORCE_FILE_PATH, OUTPUT_NONE),
                singleOptionalRule(ECHO_OFF, ECHO_ON));
    }

    /**
     * kde1dのアプリケーションで使用されるパラメータ解釈器を返す.
     * 
     * @return パラメータ解釈器
     */
    public static ConsoleParameters.Interpreter getInterpreter() {
        return ConsoleParameters.Interpreter.of(
                Set.copyOf(NoArgCommandsHolder.values),
                Set.copyOf(ArgumentRequiringCommandsHolder.values),
                COMMAND_ASSIGNMENT_RULE);
    }

    private Commands() {
        // インスタンス化不可
    }

    /**
     * このクラスで扱われているコマンドのリストを取得する.
     * 
     * @return コマンドリスト
     */
    static List<ConsoleOptionCommand> getCommands() {
        List<ConsoleOptionCommand> list = new ArrayList<>();
        list.addAll(NoArgCommandsHolder.values);
        list.addAll(ArgumentRequiringCommandsHolder.values);
        return list;
    }

    private static final class ArgumentRequiringCommandsHolder {

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
            for (Field f : Commands.class.getFields()) {
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

    private static final class NoArgCommandsHolder {

        /**
         * オプションコマンドの集合. <br>
         * 不変になるようにすること.
         */
        static final Collection<NoArgumentCommand> values;

        static {
            List<NoArgumentCommand> constantFieldList = new ArrayList<>();

            Class<NoArgumentCommand> clazz = NoArgumentCommand.class;

            // staticかつ互換性のあるフィールドのみが対象
            for (Field f : Commands.class.getFields()) {
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
