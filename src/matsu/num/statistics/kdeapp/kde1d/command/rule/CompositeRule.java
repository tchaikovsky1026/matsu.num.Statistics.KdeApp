/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.11
 */
package matsu.num.statistics.kdeapp.kde1d.command.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import matsu.num.statistics.kdeapp.kde1d.command.ConsoleOptionCommand;
import matsu.num.statistics.kdeapp.kde1d.exception.InvalidParameterException;

/**
 * 複数のルールを結合した (バリデーションを "and" でつないだ) ルールを表現する.
 * 
 * @author Matsuura Y.
 */
final class CompositeRule implements CommandAssignmentRule {

    private final Set<CommandAssignmentRule> ruleElements;

    /**
     * 非公開のコンストラクタ. <br>
     * 与えたルールセットをすべて検証するというルールを構築する.
     * 
     * <p>
     * このコンストラクタは, 引数のチェックなどは行わない, コピーも行わない.
     * </p>
     */
    private CompositeRule(Set<CommandAssignmentRule> ruleElements) {
        super();
        this.ruleElements = ruleElements;
    }

    /**
     * @throws InvalidParameterException {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public void validate(Set<ConsoleOptionCommand> allCommands) {
        ruleElements.stream()
                .forEach(rule -> rule.validate(allCommands));
    }

    /**
     * 与えたルールを結合した新しいルールを返す.
     * 
     * @param one ルールの1つ
     * @param others それ以外
     * @return 結合したルール
     * @throws NullPointerException nullを含む場合
     */
    static CompositeRule composite(CommandAssignmentRule one, CommandAssignmentRule... others) {
        // nullチェックは List.of メソッドでなされる
        List<CommandAssignmentRule> rules = new ArrayList<>(List.of(one));
        rules.addAll(List.of(others));

        // 入れ子になる構造を展開する
        Set<CommandAssignmentRule> expandedRules = rules.stream()
                .flatMap(
                        rule -> rule instanceof CompositeRule target
                                ? target.ruleElements.stream()
                                : Stream.of(rule))
                .collect(Collectors.toSet());

        return new CompositeRule(expandedRules);
    }
}
