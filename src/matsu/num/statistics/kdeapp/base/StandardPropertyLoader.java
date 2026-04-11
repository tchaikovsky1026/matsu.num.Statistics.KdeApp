/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.4.11
 */
package matsu.num.statistics.kdeapp.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Properties;

/**
 * {@link java.util.Properties} を構築する仕組み.
 * 
 * @author Matsuura Y.
 */
public abstract class StandardPropertyLoader {

    /**
     * 非公開のコンストラクタ.
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
     * ファイルが存在しない, ファイルの内容が不正の場合に例外がスローされる.
     * </p>
     * 
     * @param path ファイルパス
     * @return ローダー
     * @throws NullPointerException 引数がnullの場合
     */
    public static StandardPropertyLoader fromFile(Path path) {
        File f = path.toFile();

        return new StandardPropertyLoader() {
            @Override
            public Properties compute() {
                Properties p = new Properties();
                try (InputStreamReader isr =
                        new InputStreamReader(
                                new FileInputStream(f), StandardCharsets.UTF_8)) {
                    p.load(isr);
                    return p;
                } catch (FileNotFoundException fne) {
                    throw new IllegalStateException("file not found: " + f.getPath());
                } catch (IOException | IllegalArgumentException e) {
                    throw new IllegalStateException("load failed: " + e.getMessage());
                }
            }
        };
    }
}
