/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.29
 */
package matsu.num.statistics.kdeapp.kde1d;

import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.Set;

import matsu.num.statistics.kdeapp.base.ConstantsCollector;
import matsu.num.statistics.kdeapp.comp.ResolverContainer;
import matsu.num.statistics.kdeapp.comp.ResolverKey;
import matsu.num.statistics.kdeapp.exception.ProgrammingBugException;
import matsu.num.statistics.kdeapp.format.CommentPrefix;
import matsu.num.statistics.kdeapp.format.Separator;
import matsu.num.statistics.kdeapp.kde1d.comp.LabelPrefixSetting;

/**
 * このパッケージで扱うプロパティ.
 * 
 * @apiNote
 *              リフレクションのため,
 *              クラス, static フィールドとも {@code public} でなければならない.
 * @author Matsuura Y.
 */
public final class Resolvers {

    /*
     * コンパイルエラーを回避するために, 暫定的に定数を用意する.
     */
    public static final ResolverKey<Boolean> ECHO = ResolverKey.of("echo", Boolean.class);

    public static final ResolverKey<Path> INPUT_FILE_PATH = ResolverKey.of(
            "input-file", Path.class);
    public static final ResolverKey<CommentPrefix> INPUT_COMMENT_PREFIX = ResolverKey.of(
            "input-comment-prefix", CommentPrefix.class);

    public static final ResolverKey<OutputFileConfig> OUTPUT_FILE = ResolverKey.of(
            "output-file", OutputFileConfig.class);
    public static final ResolverKey<Separator> OUTPUT_SEPARATOR = ResolverKey.of(
            "OUTPUT_SEPARATOR", Separator.class);
    public static final ResolverKey<LabelPrefixSetting> OUTPUT_LABEL_PREFIX_SETTING = ResolverKey.of(
            "OUTPUT_LABEL_PREFIX_SETTING", LabelPrefixSetting.class);

    public static final ResolverContainer DEFAULT_PROPERTY;

    static {
        var builder = new ResolverContainer.Builder();

        builder.put(ECHO, true);
        builder.put(INPUT_COMMENT_PREFIX, CommentPrefix.of("#"));
        builder.put(OUTPUT_FILE, OutputFileConfig.none());
        builder.put(OUTPUT_SEPARATOR, Separator.from("\t"));
        builder.put(OUTPUT_LABEL_PREFIX_SETTING, LabelPrefixSetting.disable());

        DEFAULT_PROPERTY = builder.build();
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
        static final Set<ResolverKey<?>> properties = Set.copyOf(
                ConstantsCollector.collect(Resolvers.class, ResolverKey.class));
    }
}
