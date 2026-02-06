/*
 * Copyright © 2026 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.2.5
 */
package matsu.num.statistics.kdeapp.kde1d;

import static matsu.num.statistics.kdeapp.kde1d.ArgumentRequiringCommand.*;

/**
 * {@link Kde1dSourceLoader} の構築器.
 * 
 * @author Matsuura Y.
 */
final class Kde1dSourceLoaderConstructor implements ComponentConstructor<Kde1dSourceLoader> {

    static final Kde1dSourceLoaderConstructor INSTANCE = new Kde1dSourceLoaderConstructor();

    /**
     * 非公開のコンストラクタ.
     */
    private Kde1dSourceLoaderConstructor() {
    }

    /**
     * @throws InvalidParameterException {@inheritDoc }
     */
    @Override
    public Kde1dSourceLoader construct(ConsoleParameterInterpreter interpreter) {

        String pathString = interpreter.valueOf(INPUT_FILE_PATH)
                .orElseThrow(() -> new InvalidParameterException("lack parameter: " + INPUT_FILE_PATH.commandString()));

        String escape = interpreter.valueOf(COMMENT_CHAR)
                .orElse("#");
        return new Kde1dSourceLoader(pathString, escape);
    }
}
