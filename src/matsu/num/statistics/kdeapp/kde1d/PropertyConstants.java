/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.4.20
 */
package matsu.num.statistics.kdeapp.kde1d;

import java.util.HashSet;
import java.util.Set;

import matsu.num.statistics.kdeapp.base.ConstantsCollector;
import matsu.num.statistics.kdeapp.comp.PropertyKey;
import matsu.num.statistics.kdeapp.comp.ResolverDesign;
import matsu.num.statistics.kdeapp.format.CommentPrefix;
import matsu.num.statistics.kdeapp.format.Separator;
import matsu.num.statistics.kdeapp.kde1d.comp.EchoPrinter;
import matsu.num.statistics.kdeapp.kde1d.comp.LabelPrefixSetting;

/**
 * kde1d で使用する, プロパティに関する定数など.
 * 
 * <p>
 * プロパティキーに関する定数と, Property から Resolver を生成する設計図.
 * </p>
 * 
 * @author Matsuura Y.
 */
public final class PropertyConstants {

    /**
     * ディスプレイ出力のOn/Offに関する PropertyKey. <br>
     * {@code echo=on} <br>
     * {@code echo=off}
     */
    public static final PropertyKey ECHO = PropertyKey.of("echo");

    /**
     * 入力ファイルフォーマットのコメント行Prefixに関する PropertyKey. <br>
     * {@code input.commentprefix=#}
     */
    public static final PropertyKey INPUT_COMMENT_PREFIX = PropertyKey.of("input.commentprefix");

    /**
     * 出力フォーマットの区切り文字に関する PropertyKey. <br>
     * {@code output.separator=,}
     */
    public static final PropertyKey OUTPUT_SEPARATOR = PropertyKey.of("output.separator");

    /**
     * 出力フォーマットのラベル出力を行うかに関する PropertyKey. <br>
     * {@code output.label=enabled} <br>
     * {@code output.label=disabled} <br>
     * {@code output.labelprefix} が指定される場合, このプロパティは必須である.
     */
    public static final PropertyKey OUTPUT_LABEL = PropertyKey.of("output.label");

    /**
     * 出力フォーマットのラベルの Prefix に関する PropertyKey. <br>
     * {@code output.labelprefix=//} <br>
     * {@code output.label=enabled} の場合は必須であり,
     * {@code output.label=disabled} の場合は無視される.
     */
    public static final PropertyKey OUTPUT_LABEL_PREFIX = PropertyKey.of("output.labelprefix");

    /**
     * このクラスで扱う PropertyKey に関わる Resolver の設計図.
     */
    static final Set<ResolverDesign<?>> RESOLVER_DESIGNS;

    static {
        Set<ResolverDesign<?>> set = new HashSet<>();

        // EchoPrinterDesign
        set.add(
                ResolverDesign.of(
                        Resolvers.ECHO, Set.of(ECHO),
                        p -> p.find(ECHO)
                                .map(echo -> switch (echo) {
                                    case "on" -> EchoPrinter.ON;
                                    case "off" -> EchoPrinter.OFF;
                                    default -> throw new IllegalArgumentException("unknown echo");
                                }).get()));

        // InputCommentPrefixDesign
        set.add(
                ResolverDesign.of(
                        Resolvers.INPUT_COMMENT_PREFIX, Set.of(INPUT_COMMENT_PREFIX),
                        p -> p.find(INPUT_COMMENT_PREFIX).map(CommentPrefix::of).get()));

        // OutputSeparatorDesign
        set.add(
                ResolverDesign.of(
                        Resolvers.OUTPUT_SEPARATOR, Set.of(OUTPUT_SEPARATOR),
                        p -> p.find(OUTPUT_SEPARATOR).map(Separator::from).get()));

        // OutputLabelPrefixSettingDesign
        set.add(
                ResolverDesign.of(
                        Resolvers.OUTPUT_LABEL_PREFIX_SETTING,
                        Set.of(OUTPUT_LABEL, OUTPUT_LABEL_PREFIX),
                        p -> switch (p.find(OUTPUT_LABEL).get()) {
                            case "disabled" -> LabelPrefixSetting.disable();
                            case "enabled" -> LabelPrefixSetting.enable(p.find(OUTPUT_LABEL_PREFIX).get());
                            default -> throw new IllegalArgumentException("output.label");
                        }));

        RESOLVER_DESIGNS = Set.copyOf(set);
    }

    /**
     * このクラスで扱われている PropertyKey のセットを取得する.
     * 
     * @return PropertyKey のセット
     */
    static Set<PropertyKey> getPropertyKeys() {
        return PropertyKeyHolder.values;
    }

    private PropertyConstants() {
        // インスタンス化不可
    }

    private static final class PropertyKeyHolder {

        /**
         * PropertyKey の集合. <br>
         * 不変.
         */
        static final Set<PropertyKey> values = Set.copyOf(
                ConstantsCollector.collect(PropertyConstants.class, PropertyKey.class));
    }
}
