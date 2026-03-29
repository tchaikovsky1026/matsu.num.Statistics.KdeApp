/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.29
 */
package matsu.num.statistics.kdeapp.kde2d.comp;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import matsu.num.statistics.kdeapp.exception.ProgrammingBugException;
import matsu.num.statistics.kdeapp.format.Separator;
import matsu.num.statistics.kdeapp.kde2d.task.MatrixTypeFormatterBuilder;
import matsu.num.statistics.kdeapp.kde2d.task.WritingFormatter.Builder;
import matsu.num.statistics.kdeapp.kde2d.task.XyzBlockTypeFormatterBuilder;
import matsu.num.statistics.kdeapp.kde2d.task.XyzTypeFormatterBuilder;

/**
 * kde2d の出力フォーマットタイプ.
 * 
 * @author Matsuura Y.
 */
public enum BuilderType {

    /**
     * 1行が1値を表す, 縦持ち形式を表現するシングルトンインスタンス. <br>
     * デフォルトはタブ区切り, ラベル無しである.
     */
    XYZ("xyz", sep -> new XyzTypeFormatterBuilder(sep)),

    /**
     * 1行が1値を表す縦持ち形式で,
     * メジャー値 (x値) のまとまりでブロック構造をとることを表現するシングルトンインスタンス. <br>
     * デフォルトはタブ区切り, ラベル無しである.
     */
    XYZ_BLOCK("xyz-block",
            sep -> new XyzBlockTypeFormatterBuilder(sep).setBlankGap(1)),

    /**
     * 値を2次元に並べて表す, 行列形式を表現するシングルトンインスタンス. <br>
     * デフォルトはタブ区切り, ラベル無しである (ただし, ラベルの有無の設定は, このビルダでは無効である).
     */
    MATRIX("matrix", sep -> new MatrixTypeFormatterBuilder(sep));

    private final String formatRepresentation;

    private final Function<Separator, Builder<? extends Builder<?>>> builderCreator;

    /**
     * @param formatRepresentation 文字列での表現, Stringからこのクラスへの変換で使用する
     * @param builderCreator フォーマッタービルダの生成
     */
    private BuilderType(
            String formatRepresentation,
            Function<Separator, Builder<? extends Builder<?>>> builderCreator) {
        this.formatRepresentation = formatRepresentation;
        if (this.formatRepresentation.isBlank()) {
            throw new ProgrammingBugException(this.toString() + ": blank representation");
        }

        this.builderCreator = Objects.requireNonNull(builderCreator);
    }

    /**
     * このフォーマット形式のパラメータ指定用文字列を取得する.
     * 
     * <p>
     * このメソッドは, テストでしか使用されていない.
     * </p>
     * 
     * @return パラメータ指定用文字列
     */
    String representation() {
        return formatRepresentation;
    }

    /**
     * ビルダを取得する.
     * 
     * @param separator separator
     * @return ビルダ
     * @throws NullPointerException 引数がnullの場合
     */
    public Builder<? extends Builder<?>> createBuilder(Separator separator) {
        return builderCreator.apply(separator);
    }

    /**
     * 文字列を与えて, 対応するフォーマット形式プを取得する.
     * 
     * @param arg 文字列
     * @return フォーマット形式
     * @throws IllegalArgumentException 与えた文字列が不適の場合
     * @throws NullPointerException 引数がnullの場合
     */
    public static BuilderType from(String arg) {
        BuilderType out =
                StringToFormatTypeMapperHolder.MAPPER.get(Objects.requireNonNull(arg));
        if (Objects.isNull(out)) {
            throw new IllegalArgumentException("illegal type: " + arg);
        }
        return out;
    }

    /**
     * 文字列からインスタンスを取得するマッパのホルダ. <br>
     * {@link BuilderType#from(String)} が呼ばれるときに初期化される.
     * 
     * <p>
     * {@link BuilderType} の初期化後にマッパが生成される必要があるので,
     * 遅延初期化でなければならない.
     * </p>
     */
    private static final class StringToFormatTypeMapperHolder {

        static final Map<String, BuilderType> MAPPER;

        static {
            MAPPER = new HashMap<>();

            for (BuilderType format : BuilderType.values()) {
                BuilderType duplicated = MAPPER.put(format.representation(), format);

                if (Objects.nonNull(duplicated)) {
                    throw new ProgrammingBugException("duplicate: " + duplicated.representation());
                }
            }
        }
    }
}
