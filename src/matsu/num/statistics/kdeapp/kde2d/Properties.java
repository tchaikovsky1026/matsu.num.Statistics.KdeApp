/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.23
 */
package matsu.num.statistics.kdeapp.kde2d;

import java.nio.file.Path;

import matsu.num.statistics.kdeapp.config.PropertyKey;
import matsu.num.statistics.kdeapp.format.CommentPrefix;
import matsu.num.statistics.kdeapp.format.Separator;

/**
 * プロパティ.
 * 
 * @author Matsuura Y.
 */
public final class Properties {

    /*
     * コンパイルエラーを回避するために, 暫定的に定数を用意する.
     */

    public static final PropertyKey<?> ECHO = PropertyKey.of(Object.class);

    public static final PropertyKey<Path> INPUT_FILE = PropertyKey.of(Path.class);
    public static final PropertyKey<Path> OUTPUT_FILE = PropertyKey.of(Path.class);

    public static final PropertyKey<CommentPrefix> COMMENT_PREFIX = PropertyKey.of(CommentPrefix.class);

    public static final PropertyKey<Separator> INPUT_SEPARATOR = PropertyKey.of(Separator.class);
    public static final PropertyKey<Separator> OUTPUT_SEPARATOR = PropertyKey.of(Separator.class);

    public static final PropertyKey<String> OUTPUT_LABEL_PREFIX = PropertyKey.of(String.class);

    public static final PropertyKey<FormatterBuilderSupplier> OUTPUT_FORMAT_TYPE = 
            PropertyKey.of(FormatterBuilderSupplier.class);

    private Properties() {
        // インスタンス化不可
        throw new AssertionError();
    }
}
