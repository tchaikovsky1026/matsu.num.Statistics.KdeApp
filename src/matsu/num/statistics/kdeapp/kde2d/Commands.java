/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.18
 */
package matsu.num.statistics.kdeapp.kde2d;

import static matsu.num.statistics.kdeapp.command.ArgumentRequiringCommand.*;
import static matsu.num.statistics.kdeapp.command.CommandAssignmentRule.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import matsu.num.statistics.kdeapp.command.ArgumentRequiringCommand;
import matsu.num.statistics.kdeapp.command.CommandAssignmentRule;
import matsu.num.statistics.kdeapp.command.ConsoleParameters;
import matsu.num.statistics.kdeapp.command.NoArgumentCommand;
import matsu.num.statistics.kdeapp.command.util.SeparatorInterpreter;

/**
 * kde2d で取り扱う, コマンドに関するルールなど.
 * 
 * @author Matsuura Y.
 */
final class Commands {

    /**
     * 結果を標準出力しないことを表現するシングルトンインスタンス.
     */
    public static final NoArgumentCommand ECHO_OFF =
            NoArgumentCommand.of("ECHO_OFF", "--echo-off");

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
     * 強制上書きモードによる出力ファイルの指定を表現するシングルトンインスタンス.
     * 
     * <p>
     * 引数はバリデーションされない.
     * </p>
     */
    public static final ArgumentRequiringCommand<String> OUTPUT_FORCE_FILE_PATH =
            identifying("OUTPUT_FORCE_FILE_PATH", "--output-force", "-out-f");

    /**
     * 上書き禁止モードである出力ファイルの指定を表現するシングルトンインスタンス.
     * 
     * <p>
     * 引数はバリデーションされない.
     * </p>
     */
    public static final ArgumentRequiringCommand<String> OUTPUT_FILE_PATH =
            identifying("OUTPUT_FILE_PATH", "--output", "-out");

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
     * 入力ファイルの区切り文字の指定を表現するシングルトンインスタンス.
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
    public static final ArgumentRequiringCommand<Character> SEPARATOR_INPUT =
            ArgumentRequiringCommand.of(
                    "SEPARATOR_INPUT", Character.class,
                    s -> interpretSeparator(s),
                    "--separator-in", "-sep-i");

    /**
     * 出力ファイルの区切り文字の指定を表現するシングルトンインスタンス.
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
    public static final ArgumentRequiringCommand<Character> SEPARATOR_OUTPUT =
            ArgumentRequiringCommand.of(
                    "SEPARATOR_OUTPUT", Character.class,
                    s -> interpretSeparator(s),
                    "--separator-out", "-sep-o");

    /**
     * {@link SeparatorInterpreter#from(String)} を使用して文字列を char に変換する. <br>
     * 元のメソッドは例外をスローするが, それをnullを返すように翻訳する.
     */
    private static Character interpretSeparator(String s) {
        try {
            return SeparatorInterpreter.from(s);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * 出力のラベルに付与する prefix の指定を表現するシングルトンインスタンス.
     * 
     * <p>
     * 引数はバリデーションされない.
     * </p>
     */
    public static final ArgumentRequiringCommand<String> LABEL_HEADER =
            identifying("LABEL_HEADER", "--label-header");

    /**
     * コマンドの指定に関するルール.
     */
    private static final CommandAssignmentRule COMMAND_ASSIGNMENT_RULE;

    static {
        COMMAND_ASSIGNMENT_RULE = composite(
                singleRequiredRule(INPUT_FILE_PATH),
                singleOptionalRule(OUTPUT_FILE_PATH, OUTPUT_FORCE_FILE_PATH));
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
