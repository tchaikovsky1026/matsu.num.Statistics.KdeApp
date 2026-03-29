/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.24
 */
package matsu.num.statistics.kdeapp.kde1d;

import static matsu.num.statistics.kdeapp.comp.CommandAssignmentRule.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import matsu.num.statistics.kdeapp.base.ConstantsCollector;
import matsu.num.statistics.kdeapp.comp.ArgumentRequiringCommand;
import matsu.num.statistics.kdeapp.comp.CommandAssignmentRule;
import matsu.num.statistics.kdeapp.comp.ConsoleOptionCommand;
import matsu.num.statistics.kdeapp.comp.ConsoleParameterInterpreter;
import matsu.num.statistics.kdeapp.comp.NoArgumentCommand;
import matsu.num.statistics.kdeapp.format.CommentPrefix;
import matsu.num.statistics.kdeapp.format.Separator;
import matsu.num.statistics.kdeapp.kde1d.task.ResultFileWriter;
import matsu.num.statistics.kdeapp.kde1d.task.ResultWriter;

/**
 * kde1d で取り扱う, コマンドに関するルールなど.
 * 
 * @apiNote
 *              リフレクションのため,
 *              クラス, static フィールドとも {@code public} でなければならない.
 * @author Matsuura Y.
 */
public final class Commands {

    /**
     * 結果を標準出力しないことを表現するシングルトンインスタンス.
     */
    public static final NoArgumentCommand<?> ECHO_OFF =
            NoArgumentCommand.of("ECHO_OFF", Resolvers.ECHO, () -> false, "--echo-off");

    /**
     * 結果を標準出力することを表現するシングルトンインスタンス.
     */
    public static final NoArgumentCommand<?> ECHO_ON =
            NoArgumentCommand.of("ECHO_ON", Resolvers.ECHO, () -> true, "--echo-on");

    /**
     * 入力ファイルの指定を表現するシングルトンインスタンス.
     */
    public static final ArgumentRequiringCommand<Path> INPUT_FILE_PATH =
            ArgumentRequiringCommand.of(
                    "INPUT_FILE_PATH", Resolvers.INPUT_FILE_PATH, Path::of,
                    "--input", "--in");

    /**
     * 入力のコメント行の prefix の指定を表現するシングルトンインスタンス.
     */
    public static final ArgumentRequiringCommand<CommentPrefix> INPUT_COMMENT_PREFIX =
            ArgumentRequiringCommand.of(
                    "INPUT_COMMENT_PREFIX", Resolvers.INPUT_COMMENT_PREFIX, CommentPrefix::of,
                    "--input-comment-prefix", "--in-comment-prefix");

    /**
     * 強制上書きモードによる出力ファイルの指定を表現するシングルトンインスタンス.
     */
    public static final ArgumentRequiringCommand<ResultWriter> OUTPUT_FORCE_FILE_PATH =
            ArgumentRequiringCommand.of(
                    "OUTPUT_FORCE_FILE_PATH", Resolvers.OUTPUT_FILE_WRITER,
                    s -> ResultFileWriter.forceWriter(Path.of(s)),
                    "--output-force", "--out-force");

    /**
     * 上書き禁止モードである出力ファイルの指定を表現するシングルトンインスタンス.
     */
    public static final ArgumentRequiringCommand<ResultWriter> OUTPUT_FILE_PATH =
            ArgumentRequiringCommand.of(
                    "OUTPUT_FILE_PATH", Resolvers.OUTPUT_FILE_WRITER,
                    s -> ResultFileWriter.regularWriter(Path.of(s)),
                    "--output", "--out");

    /**
     * ファイル出力しないことを表現するシングルトンインスタンス.
     */
    public static final NoArgumentCommand<ResultWriter> OUTPUT_NONE =
            NoArgumentCommand.of(
                    "OUTPUT_NONE", Resolvers.OUTPUT_FILE_WRITER,
                    () -> ResultWriter.nullWriter(),
                    "--output-none", "--out-none");

    /**
     * 区切り文字の指定を表現するシングルトンインスタンス.
     */
    public static final ArgumentRequiringCommand<Separator> OUTPUT_SEPARATOR =
            ArgumentRequiringCommand.of(
                    "OUTPUT_SEPARATOR", Resolvers.OUTPUT_SEPARATOR, Separator::from,
                    "--output-separator", "--out-sep");

    /**
     * 出力のラベルに付与する prefix の指定を表現するシングルトンインスタンス.
     */
    public static final ArgumentRequiringCommand<OutputLabelPrefixConfig> OUTPUT_LABEL_PREFIX =
            ArgumentRequiringCommand.of(
                    "OUTPUT_LABEL_PREFIX", Resolvers.OUTPUT_LABEL_PREFIX,
                    OutputLabelPrefixConfig::withLabel,
                    "--output-label-prefix", "--out-label-prefix");

    /**
     * ラベルを出力しないことを表現するシングルトンインスタンス.
     */
    public static final NoArgumentCommand<OutputLabelPrefixConfig> OUTPUT_NO_LABEL =
            NoArgumentCommand.of(
                    "OUTPUT_NO_LABEL", Resolvers.OUTPUT_LABEL_PREFIX,
                    () -> OutputLabelPrefixConfig.nonLabel(),
                    "--output-no-label", "--out-no-label");

    /**
     * コマンドの指定に関するルール.
     */
    private static final CommandAssignmentRule COMMAND_ASSIGNMENT_RULE;

    static {
        COMMAND_ASSIGNMENT_RULE = composite(
                singleRequiredRule(INPUT_FILE_PATH),
                singleOptionalRule(OUTPUT_FILE_PATH, OUTPUT_FORCE_FILE_PATH, OUTPUT_NONE),
                singleOptionalRule(OUTPUT_LABEL_PREFIX, OUTPUT_NO_LABEL),
                singleOptionalRule(ECHO_OFF, ECHO_ON));
    }

    /**
     * kde1dのアプリケーションで使用されるパラメータ解釈器を返す.
     * 
     * @return パラメータ解釈器
     */
    public static ConsoleParameterInterpreter getInterpreter() {
        return ConsoleParameterInterpreter.of(
                NoArgCommandsHolder.values,
                ArgumentRequiringCommandsHolder.values,
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
    static List<ConsoleOptionCommand<?>> getCommands() {
        List<ConsoleOptionCommand<?>> list = new ArrayList<>();
        list.addAll(NoArgCommandsHolder.values);
        list.addAll(ArgumentRequiringCommandsHolder.values);
        return list;
    }

    private static final class ArgumentRequiringCommandsHolder {

        /**
         * オプションコマンドの集合. <br>
         * 不変になるようにすること.
         */
        static final Set<ArgumentRequiringCommand<?>> values = Set.copyOf(
                ConstantsCollector.collect(Commands.class, ArgumentRequiringCommand.class));
    }

    private static final class NoArgCommandsHolder {

        /**
         * オプションコマンドの集合. <br>
         * 不変になるようにすること.
         */
        static final Set<NoArgumentCommand<?>> values = Set.copyOf(
                ConstantsCollector.collect(Commands.class, NoArgumentCommand.class));
    }
}
