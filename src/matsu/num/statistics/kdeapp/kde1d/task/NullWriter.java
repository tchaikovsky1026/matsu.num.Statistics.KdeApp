/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.3.11
 */
package matsu.num.statistics.kdeapp.kde1d.task;

/**
 * null-出力を返す.
 * 
 * <p>
 * null-出力とは, 何もしない出力のことである.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class NullWriter implements ResultWriter {

    /**
     * このクラスの唯一のインスタンスを表す.
     */
    static final NullWriter SINGLETON = new NullWriter();

    /**
     * 非公開のコンストラクタ.
     */
    private NullWriter() {

    }

    @Override
    public void write(WritableKde1dResult result, WritingFormatter writingFormatter) {
        // 何もしない.
    }
}
