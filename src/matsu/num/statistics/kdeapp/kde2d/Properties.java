/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.24
 */
package matsu.num.statistics.kdeapp.kde2d;

import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.Set;

import matsu.num.statistics.kdeapp.config.ConfigProperty;
import matsu.num.statistics.kdeapp.config.PropertyKey;
import matsu.num.statistics.kdeapp.exception.ProgrammingBugException;
import matsu.num.statistics.kdeapp.format.CommentPrefix;
import matsu.num.statistics.kdeapp.format.Separator;

/**
 * このパッケージで扱うプロパティ.
 * 
 * @author Matsuura Y.
 */
public final class Properties {

    /*
     * コンパイルエラーを回避するために, 暫定的に定数を用意する.
     */

    public static final PropertyKey<Boolean> ECHO = PropertyKey.of("echo", Boolean.class);

    public static final PropertyKey<Path> INPUT_FILE_PATH = PropertyKey.of(
            "input-file", Path.class);
    public static final PropertyKey<CommentPrefix> INPUT_COMMENT_PREFIX = PropertyKey.of(
            "input-comment-prefix", CommentPrefix.class);
    public static final PropertyKey<Separator> INPUT_SEPARATOR = PropertyKey.of(
            "input-separator", Separator.class);

    public static final PropertyKey<OutputFileConfig> OUTPUT_FILE = PropertyKey.of(
            "output-file", OutputFileConfig.class);
    public static final PropertyKey<Separator> OUTPUT_SEPARATOR = PropertyKey.of(
            "output-separator", Separator.class);
    public static final PropertyKey<OutputLabelPrefixConfig> OUTPUT_LABEL_PREFIX = PropertyKey.of(
            "output-label-prefix", OutputLabelPrefixConfig.class);
    public static final PropertyKey<FormatterBuilderSupplier> OUTPUT_FORMAT_TYPE = PropertyKey.of(
            "output-format", FormatterBuilderSupplier.class);

    public static final ConfigProperty DEFAULT_PROPERTY;

    static {
        var builder = new ConfigProperty.Builder();

        builder.put(ECHO, true);
        builder.put(INPUT_COMMENT_PREFIX, CommentPrefix.of("#"));
        builder.put(INPUT_SEPARATOR, Separator.from("\t"));

        builder.put(OUTPUT_FILE, OutputFileConfig.none());
        builder.put(OUTPUT_SEPARATOR, Separator.from("\t"));
        builder.put(OUTPUT_LABEL_PREFIX, OutputLabelPrefixConfig.nonLabel());
        builder.put(OUTPUT_FORMAT_TYPE, FormatterBuilderSupplier.XYZ);

        DEFAULT_PROPERTY = builder.build();
    }

    private Properties() {
        // インスタンス化不可
        throw new AssertionError();
    }

    /**
     * {@link ConfigProperty} が完全であるかどうかを検証する.
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
    static void validateCompleteness(ConfigProperty property) {
        PropertyKey<?> key = null;
        try {
            for (PropertyKey<?> k : PropertyKeyHolder.properties) {
                key = k;
                property.get(k);
            }
        } catch (NoSuchElementException e) {
            throw new ProgrammingBugException("requiring: " + key);
        }
    }

    private static final class PropertyKeyHolder {
        static final Set<PropertyKey<?>> properties;

        static {
            properties = Set.copyOf(PropertyKey.constantsOf(Properties.class));
        }
    }
}
