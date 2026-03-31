/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.4.12
 */
package matsu.num.statistics.kdeapp.kde1d;

import static java.util.stream.Collectors.*;

import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.Set;

import matsu.num.statistics.kdeapp.base.ConstantsCollector;
import matsu.num.statistics.kdeapp.base.StandardPropertyLoader;
import matsu.num.statistics.kdeapp.comp.ResolverContainer;
import matsu.num.statistics.kdeapp.comp.ResolverKey;
import matsu.num.statistics.kdeapp.exception.ProgrammingBugException;
import matsu.num.statistics.kdeapp.format.CommentPrefix;
import matsu.num.statistics.kdeapp.format.Separator;
import matsu.num.statistics.kdeapp.kde1d.comp.EchoPrinter;
import matsu.num.statistics.kdeapp.kde1d.comp.LabelPrefixSetting;
import matsu.num.statistics.kdeapp.kde1d.task.ResultWriter;

/**
 * このパッケージで扱うプロパティ.
 * 
 * @apiNote
 *              リフレクションのため,
 *              クラス, static フィールドとも {@code public} でなければならない.
 * @author Matsuura Y.
 */
public final class Resolvers {

    /** ディスプレイ出力のOn/Offに関する ResolverKey. */
    public static final ResolverKey<EchoPrinter> ECHO =
            ResolverKey.of("ECHO", EchoPrinter.class);

    /** 入力ファイルパスの ResolverKey. */
    public static final ResolverKey<Path> INPUT_FILE_PATH = ResolverKey.of(
            "INPUT_FILE", Path.class);

    /** 入力ファイルフォーマットのコメント行Prefixの ResolverKey. */
    public static final ResolverKey<CommentPrefix> INPUT_COMMENT_PREFIX = ResolverKey.of(
            "INPUT_COMMENT_PREFIX", CommentPrefix.class);

    /**
     * ファイル出力に関する ResolverKey. <br>
     * (出力を行う場合は) ファイルパスや上書き可能かどうかなどが紐づけられる.
     */
    public static final ResolverKey<ResultWriter> OUTPUT_FILE_WRITER = ResolverKey.of(
            "OUTPUT_FILE_WRITER", ResultWriter.class);

    /** 出力フォーマットの区切り文字の ResolverKey. */
    public static final ResolverKey<Separator> OUTPUT_SEPARATOR = ResolverKey.of(
            "OUTPUT_SEPARATOR", Separator.class);

    /** 出力フォーマットのラベル出力設定に関する ResolverKey. */
    public static final ResolverKey<LabelPrefixSetting> OUTPUT_LABEL_PREFIX_SETTING = ResolverKey.of(
            "OUTPUT_LABEL_PREFIX_SETTING", LabelPrefixSetting.class);

    /** Resolvers を Config から読むことに関する ResolverKey. */
    public static final ResolverKey<StandardPropertyLoader> CONFIG = ResolverKey.of(
            "CONFIG", StandardPropertyLoader.class);

    /** デフォルトの Resolver群. */
    public static final ResolverContainer DEFAULT_RESOLVERS;

    static {
        var builder = new ResolverContainer.Builder();

        builder.put(ECHO, EchoPrinter.ON);
        builder.put(INPUT_COMMENT_PREFIX, CommentPrefix.of("#"));
        builder.put(OUTPUT_SEPARATOR, Separator.from("\t"));
        builder.put(OUTPUT_FILE_WRITER, ResultWriter.nullWriter());
        builder.put(OUTPUT_LABEL_PREFIX_SETTING, LabelPrefixSetting.disable());
        builder.put(CONFIG, StandardPropertyLoader.emptyLoader());

        DEFAULT_RESOLVERS = builder.build();
    }

    private Resolvers() {
        // インスタンス化不可
        throw new AssertionError();
    }

    /**
     * {@link ResolverContainer} が完全であるかどうかを検証する.
     * 
     * <p>
     * プロパティは, コマンドインタプリタでの必須宣言, オプションの場合はデフォルト値の用意によって,
     * 完全なものが生成されなければならない. <br>
     * 完全でないのはプログラムのバグである. <br>
     * このメソッドは, それを検証するものである.
     * </p>
     * 
     * @param property ConfigProperty
     * @throws ProgrammingBugException プロパティが網羅されていない場合
     */
    static void validateCompleteness(ResolverContainer property) {
        ResolverKey<?> key = null;
        try {
            for (ResolverKey<?> k : PropertyKeyHolder.properties) {
                key = k;
                property.get(k);
            }
        } catch (NoSuchElementException e) {
            throw new ProgrammingBugException("requiring: " + key);
        }
    }

    private static final class PropertyKeyHolder {

        static final Set<ResolverKey<?>> properties;

        static {
            @SuppressWarnings("rawtypes")
            Set<ResolverKey> raw =
                    ConstantsCollector.collect(Resolvers.class, ResolverKey.class);

            properties = Set.copyOf(
                    raw.stream()
                            .map(c -> (ResolverKey<?>) c)
                            .collect(toSet()));
        }
    }
}
