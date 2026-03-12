/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.12
 */
package matsu.num.statistics.kdeapp.kde2d.task;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import matsu.num.statistics.kdeapp.exception.ProgrammingBugException;

/**
 * kde2d の出力フォーマットの形式を扱う列挙型.
 * 
 * @author Matsuura Y.
 */
public enum OutputFormatType {

    /**
     * 1行が1値を表す, 縦持ち形式を表現するシングルトンインスタンス.
     */
    XYZ("xyz"),

    /**
     * 1行が1値を表す縦持ち形式で,
     * メジャー値 (x値) のまとまりでブロック構造をとることを表現するシングルトンインスタンス.
     */
    XYZ_BLOCK("xyz-block"),

    /**
     * 値を2次元に並べて表す, 行列形式を表現するシングルトンインスタンス.
     */
    MATRIX("matrix");

    private final String formatRepresentation;

    private OutputFormatType(String formatRepresentation) {
        this.formatRepresentation = formatRepresentation;
        if (this.formatRepresentation.isBlank()) {
            throw new ProgrammingBugException(this.toString() + ": blank representation");
        }
    }

    /**
     * このフォーマット形式のパラメータ指定用文字列を取得する.
     * 
     * @return パラメータ指定用文字列
     */
    String representation() {
        return formatRepresentation;
    }

    /**
     * 文字列を与えて, 対応するフォーマット形式プを取得する.
     * 
     * @param arg 文字列
     * @return フォーマット形式
     * @throws IllegalArgumentException 与えた文字列が不適の場合
     * @throws NullPointerException 引数がnullの場合
     */
    public static OutputFormatType from(String arg) {
        OutputFormatType out =
                StringToFormatTypeMapperHolder.MAPPER.get(Objects.requireNonNull(arg));
        if (Objects.isNull(out)) {
            throw new IllegalArgumentException("illegal type: " + arg);
        }
        return out;
    }

    /**
     * 文字列からインスタンスを取得するマッパのホルダ. <br>
     * {@link OutputFormatType#from(String)} が呼ばれるときに初期化される.
     * 
     * <p>
     * {@link OutputFormatType} の初期化後にマッパが生成される必要があるので,
     * 遅延初期化でなければならない.
     * </p>
     */
    private static final class StringToFormatTypeMapperHolder {

        static final Map<String, OutputFormatType> MAPPER;

        static {
            MAPPER = new HashMap<>();

            for (OutputFormatType format : OutputFormatType.values()) {
                OutputFormatType duplicated = MAPPER.put(format.representation(), format);

                if (Objects.nonNull(duplicated)) {
                    throw new ProgrammingBugException("duplicate: " + duplicated.representation());
                }
            }
        }
    }
}
