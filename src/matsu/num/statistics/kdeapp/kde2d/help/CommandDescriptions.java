/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.22
 */
package matsu.num.statistics.kdeapp.kde2d.help;

import java.util.ArrayList;
import java.util.List;

import matsu.num.statistics.kdeapp.help.CommandCategory;
import matsu.num.statistics.kdeapp.help.CommandDescription;
import matsu.num.statistics.kdeapp.kde2d.Commands;

/**
 * kde2dで使用されているコマンド群.
 * 
 * @author Matsuura Y.
 */
final class CommandDescriptions {

    private static final CommandCategory catInput = CommandCategory.from("Input");
    private static final CommandCategory catOutput = CommandCategory.from("Output");
    private static final CommandCategory catOther = CommandCategory.from("Other");

    private static final List<CommandDescription> commands;

    /**
     * 結果を標準出力しないことを表現するシングルトンインスタンス.
     */
    static final CommandDescription ECHO_OFF;

    /**
     * 結果を標準出力することを表現するシングルトンインスタンス.
     */
    static final CommandDescription ECHO_ON;

    /**
     * 入力ファイルの指定を表現するシングルトンインスタンス.
     */
    static final CommandDescription INPUT_FILE_PATH;

    /**
     * 強制上書きモードによる出力ファイルの指定を表現するシングルトンインスタンス.
     */
    static final CommandDescription OUTPUT_FORCE_FILE_PATH;

    /**
     * 上書き禁止モードである出力ファイルの指定を表現するシングルトンインスタンス.
     */
    static final CommandDescription OUTPUT_FILE_PATH;

    /**
     * ファイル出力しないことを表現するシングルトンインスタンス.
     */
    static final CommandDescription OUTPUT_NONE;

    /**
     * 入力のコメント行の prefix の指定を表現するシングルトンインスタンス.
     */
    static final CommandDescription INPUT_COMMENT_PREFIX;

    /**
     * 入力ファイルの区切り文字の指定を表現するシングルトンインスタンス.
     */
    static final CommandDescription INPUT_SEPARATOR;

    /**
     * 出力ファイルの区切り文字の指定を表現するシングルトンインスタンス.
     */
    static final CommandDescription OUTPUT_SEPARATOR;

    /**
     * 出力ファイルのフォーマットの指定を表現するシングルトンインスタンス.
     */
    static final CommandDescription OUTPUT_FORMAT_TYPE;

    /**
     * 出力のラベルに付与する prefix の指定を表現するシングルトンインスタンス.
     */
    static final CommandDescription OUTPUT_LABEL_PREFIX;

    /**
     * ラベルを出力しないことを表現するシングルトンインスタンス.
     */
    static final CommandDescription OUTPUT_NO_LABEL;

    static {
        List<CommandDescription> list = new ArrayList<CommandDescription>();

        INPUT_FILE_PATH =
                CommandDescription.of(
                        Commands.INPUT_FILE_PATH, "FILE",
                        "input data file",
                        catInput);
        list.add(INPUT_FILE_PATH);

        INPUT_COMMENT_PREFIX =
                CommandDescription.of(
                        Commands.INPUT_COMMENT_PREFIX, "STR",
                        "comment prefix in the input file",
                        catInput);
        list.add(INPUT_COMMENT_PREFIX);

        INPUT_SEPARATOR =
                CommandDescription.of(
                        Commands.INPUT_SEPARATOR, "CHAR",
                        " field separator of the input data",
                        catInput);
        list.add(INPUT_SEPARATOR);

        OUTPUT_FILE_PATH =
                CommandDescription.of(
                        Commands.OUTPUT_FILE_PATH, "FILE",
                        "output result to file (ERROR if it exists)",
                        catOutput);
        list.add(OUTPUT_FILE_PATH);

        OUTPUT_FORCE_FILE_PATH =
                CommandDescription.of(
                        Commands.OUTPUT_FORCE_FILE_PATH, "FILE",
                        "overwrite the output file if it exists",
                        catOutput);
        list.add(OUTPUT_FORCE_FILE_PATH);

        OUTPUT_NONE =
                CommandDescription.of(
                        Commands.OUTPUT_NONE,
                        "does not output result to file",
                        catOutput);
        list.add(OUTPUT_NONE);

        OUTPUT_SEPARATOR =
                CommandDescription.of(
                        Commands.OUTPUT_SEPARATOR, "CHAR",
                        "field separator of the output data",
                        catOutput);
        list.add(OUTPUT_SEPARATOR);

        OUTPUT_FORMAT_TYPE =
                CommandDescription.of(
                        Commands.OUTPUT_FORMAT_TYPE, "FORMAT",
                        "format of output result",
                        catOutput);
        list.add(OUTPUT_FORMAT_TYPE);

        OUTPUT_LABEL_PREFIX =
                CommandDescription.of(
                        Commands.OUTPUT_LABEL_PREFIX, "STR",
                        "prefix added to the label line",
                        catOutput);
        list.add(OUTPUT_LABEL_PREFIX);

        OUTPUT_NO_LABEL =
                CommandDescription.of(
                        Commands.OUTPUT_NO_LABEL,
                        "does not write label to the output file",
                        catOutput);
        list.add(OUTPUT_NO_LABEL);

        ECHO_OFF =
                CommandDescription.of(
                        Commands.ECHO_OFF,
                        "not display the result to stdout",
                        catOther);
        list.add(ECHO_OFF);

        ECHO_ON =
                CommandDescription.of(
                        Commands.ECHO_ON,
                        "display the result to stdout",
                        catOther);
        list.add(ECHO_ON);

        commands = List.copyOf(list);
    }

    /**
     * コマンド群を取得する.
     */
    static List<CommandDescription> get() {
        return commands;
    }

    private CommandDescriptions() {
        // インスタンス化不可
        throw new AssertionError();
    }
}
