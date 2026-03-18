/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.18
 */
package matsu.num.statistics.kdeapp.help;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * コマンドをカテゴリ毎に表示するフォーマッタ.
 * 
 * @author Matsuura Y.
 */
public final class CategorizationFormatter implements DescriptionFormatter {

    private final int indentWidth;

    /**
     * 公開された唯一のコンストラクタ.
     */
    public CategorizationFormatter() {
        this(4);
    }

    /**
     * @param indentWidth インデント幅 (1インデントで挿入されるスペースの数)
     * @throws IllegalArgumentException
     *             {@code indentWidth <= 0 || indentWidth >= 100}
     */
    private CategorizationFormatter(int indentWidth) {
        super();
        if (indentWidth <= 0 || indentWidth >= 100) {
            throw new IllegalArgumentException("illegal indent width");
        }
        this.indentWidth = indentWidth;
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * {@link CategorizationFormatter} でのフォーマッタ仕様は (おそらく) 次のようなものだろう.
     * </p>
     * 
     * <ul>
     * <li>カテゴリ名の前行にブランク行</li>
     * <li>カテゴリ名は1インデント</li>
     * <li>コマンド名は2インデント</li>
     * <li>コマンド説明は3インデント</li>
     * <li>コマンド説明の後にブランク行
     * (結果として, コマンド説明の後にカテゴリ名が来る場合は2ブランク行が入っている)</li>
     * </ul>
     * 
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public List<String> format(Iterable<? extends CommandDescription> commands) {

        Map<CommandCategory, List<CommandDescription>> categorized =
                CommandDescription.groupingByCategory(commands);

        List<String> formatted = new ArrayList<>();
        for (CommandCategory cat : categorized.keySet()) {

            // カテゴリ名の前行ブランク
            formatted.add("");
            formatted.add(indentStr(1) + cat.categoryName() + ":");

            for (CommandDescription c : categorized.get(cat)) {
                String usageSyntaxes = c.getUsageSyntaxes()
                        .stream()
                        .collect(joining(", "));
                formatted.add(indentStr(2) + usageSyntaxes);
                formatted.add(indentStr(3) + c.description());

                // コマンド説明の後行ブランク行
                formatted.add("");
            }
        }

        return formatted;
    }

    /**
     * インデント文字列を生成する.
     * 
     * @param indentLevel インデントレベル
     * @return インデント文字列
     * @throws IllegalArgumentException
     *             {@code indentLevel <= 0 || indentLevel >= 10000}
     */
    private String indentStr(int indentLevel) {
        if (indentLevel <= 0 || indentLevel >= 10000) {
            throw new IllegalArgumentException("illegal indent level");
        }
        return " ".repeat(indentLevel * indentWidth);
    }
}
