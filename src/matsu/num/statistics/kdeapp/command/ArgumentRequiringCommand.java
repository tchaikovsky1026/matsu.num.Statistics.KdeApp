/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.19
 */
package matsu.num.statistics.kdeapp.command;

import java.util.Objects;
import java.util.function.Function;

import matsu.num.statistics.kdeapp.exception.IllegalParameterException;

/**
 * 引数をとるコマンドを扱うクラス.
 * 
 * <p>
 * このコマンドは, 引数となる文字列を受け取って任意の型に変換 (コンバート) する機能を持つ. <br>
 * そのコンバート先の型は, 型パラメータで与えられる.
 * </p>
 * 
 * @author Matsuura Y.
 * @param <T> コマンドの引数のコンバート先の型, see {@link #convertArg(String)}
 */
public final class ArgumentRequiringCommand<T> extends ConsoleOptionCommand {

    private final Class<T> valueType;
    private final Function<? super String, ? extends T> converter;

    /**
     * 内部から呼ばれる唯一のコンストラクタ.
     * 
     * <p>
     * コンバータは, コンバートできない場合 (例外をスローすべき引数の場合) は
     * {@link IllegalArgumentException} をスローするようにする. <br>
     * {@code null} を返してはいけない.
     * </p>
     * 
     * @param enumString インスタンスの文字列表現
     * @param valueType 変更先の型トークン
     * @param converter コンバータ
     * @param commandExpression コマンドの正式な文字列表現
     * @param otherExpressions 正式表現以外の文字列表現
     * @throws IllegalArgumentException 文字列がブランクを含む場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    private ArgumentRequiringCommand(
            String enumString,
            Class<T> valueType, Function<? super String, ? extends T> converter,
            String commandExpression, String... otherExpressions) {
        super(enumString, commandExpression, otherExpressions);

        this.valueType = valueType;
        this.converter = converter;
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
     * 文字列として与えられる引数を, このコマンドで使用する形に変換する. <br>
     * コンバートの詳細は, {@code public static final} な定数の説明に書かれるべきである.
     * 
     * @param arg 文字列引数
     * @return 変換後の値
     * @throws IllegalParameterException パラメータ不正の場合
     * @throws NullPointerException 引数がnullの場合(必ず)
     */
    public final T convertArg(String arg) {
        try {
            return converter.apply(Objects.requireNonNull(arg));
        } catch (IllegalArgumentException iae) {
            throw new IllegalParameterException(
                    "invalid-arg for <" + this.commandString() + ">: \"" + arg + "\"");
        }
    }

    /**
     * インスタンスを構築する.
     * 
     * <p>
     * コンバータは, コンバートできない場合 (例外をスローすべき引数の場合) は
     * {@link IllegalArgumentException} をスローするようにする. <br>
     * {@code null} を返してはいけない.
     * </p>
     * 
     * @param <T> コンバート先の型, see {@link #convertArg(String)}
     * @param enumString インスタンスの文字列表現
     * @param valueType コンバート先 ({@code T}) の型トークン
     * @param converter コンバータ
     * @param commandExpression コマンドの正式な文字列表現
     * @param otherExpressions 正式表現以外の文字列表現
     * @return インスタンス
     * @throws IllegalArgumentException 文字列がブランクを含む場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public static <T> ArgumentRequiringCommand<T> of(
            String enumString,
            Class<T> valueType, Function<? super String, ? extends T> converter,
            String commandExpression, String... otherExpressions) {

        return new ArgumentRequiringCommand<>(
                enumString, valueType, converter, commandExpression, otherExpressions);
    }

    /**
     * {@link #of(String, Class, Function, String, String...)}
     * メソッドのコンバータに恒等写像を与える形式で, インスタンスを生成する.
     * 
     * @param enumString インスタンスの文字列表現
     * @param commandExpression コマンドの正式な文字列表現
     * @param otherExpressions 正式表現以外の文字列表現
     * @return インスタンス
     * @throws IllegalArgumentException 文字列がブランクを含む場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public static ArgumentRequiringCommand<String> identifying(
            String enumString, String commandExpression, String... otherExpressions) {

        return new ArgumentRequiringCommand<String>(
                enumString, String.class, s -> s,
                commandExpression, otherExpressions);
    }
}
