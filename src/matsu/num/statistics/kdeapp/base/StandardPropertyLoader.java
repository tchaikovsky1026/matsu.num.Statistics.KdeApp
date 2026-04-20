/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.4.14
 */
package matsu.num.statistics.kdeapp.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Properties;

/**
 * {@link java.util.Properties} を構築する仕組み.
 * 
 * @author Matsuura Y.
 */
public abstract class StandardPropertyLoader {

    /**
     * 非公開のコンストラクタ. <br>
     * 外部での継承は想定されていない.
     */
    private StandardPropertyLoader() {
        super();
    }

    /**
     * {@link java.util.Properties} を構築する.
     * 
     * <p>
     * ファイルなどから構築する場合, ディスクアクセスなどが発生する.
     * </p>
     * 
     * @return プロパティ
     * @throws IllegalStateException プロパティを構築できなかった場合
     */
    public abstract java.util.Properties compute();

    /**
     * 空のプロパティを生成するローダを返す.
     * 
     * <p>
     * {@link #compute()} で例外はスローされない.
     * </p>
     * 
     * @return 空ローダ
     */
    public static StandardPropertyLoader emptyLoader() {
        return new StandardPropertyLoader() {
            @Override
            public Properties compute() {
                return new Properties();
            }
        };
    }

    /**
     * ファイルからプロパティを生成するローダを返す.
     * 
     * <p>
     * {@link #compute()} では,
     * ファイルが存在しない, ファイルを読み込めない, ファイルの内容が不正である場合に
     * {@link IllegalStateException} がスローされる.
     * </p>
     * 
     * @param path ファイルパス
     * @return ローダー
     * @throws NullPointerException 引数がnullの場合
     */
    public static StandardPropertyLoader fromFile(Path path) {
        Objects.requireNonNull(path);

        return new StandardPropertyLoader() {
            @Override
            public Properties compute() {
                if (Files.notExists(path)) {
                    throw new IllegalStateException("file not found: " + path);
                }

                Properties p = new Properties();
                try (BufferedReader br =
                        Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                    p.load(br);
                    return p;
                } catch (IOException | IllegalArgumentException e) {
                    throw new IllegalStateException("load failed: " + e.getMessage());
                }
            }
        };
    }
}
