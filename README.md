# matsu.num.Statistics.KdeApp
`matsu.num.Statistics.KdeApp` は Java 言語でカーネル密度推定を実行する App を提供する.

現在は開発中バージョン `0.10.0` であり, Java 17 に準拠する.

## Dependency
このソフトウェアは次のモジュールを要求する.

- `matsu.num.Statistics.KernelDensity`, version `1` 系最新版
([tchaikovsky1026/matsu.num.Statistics.KernelDensity](https://github.com/tchaikovsky1026/matsu.num.Statistics.KernelDensity.git))

## Installation
- zipアーカイブを展開する.
    - `matsu.num.Statistics.KdeApp.jar` があるディレクトリを `root` とする.
- 依存ライブラリを `root/lib` に配置する.

---
## Application: kde1d
`kde1d` は1次元のカーネル密度推定の実行を表すコードネームである.

### Execution
共通処理として, ターミナルを起動し, カレントディレクトリを `root` に移動する.
その後, Windows と macOS / Linux で次のようになる.

#### Windows
```
.\kde1d.bat <パラメータ>
```

コマンドプロンプトを使用している場合, 次でもよい.

```
kde1d <パラメータ>
```

#### macOS / Linux
```
./kde1d.sh <パラメータ>
```

スクリプトに実行権限がない場合は, 初回時に次を実行する.

```
chmod +x kde1d.sh
```

### Usage

#### Parameter
使用するパラメータは次である.

- `--input FILE` / `--in FILE`
- `--input-comment-prefix STR` / `--in-comment-prefix STR`
- `--output FILE` / `--out FILE`, または `--output-force FILE` / `--out-force FILE`
- `--output-separator CHAR` / `--out-sep CHAR`
- `--output-label-prefix STR` / `--out-label-prefix STR`
- `--echo-off`

##### `--input FILE` / `--in FILE`
入力ファイルパスを指定するコマンドである.
このパラメータは必ず指定されなければならない.
指定されない場合, 例外がスローされる.

##### `--input-comment-prefix STR` / `--in-comment-prefix STR`
入力ファイルのコメント行の開始文字列を指定するコマンドである.
指定されない場合, `#` がコメント開始文字列となる.

Prefixに空文字は禁止されている.
Prefix, 入力ファイルの各行の前後の空白は無視される.

##### `--output FILE` / `--out FILE`, または `--output-force FILE` / `--out-force FILE`
結果のファイル出力を行うコマンドである.
`--output` はファイルが存在した場合はエラーとなり,
`--output-force` はファイルが存在した場合は上書きする.
ファイルパスまでのディレクトリは自動で生成される.

`--output-force`, `--output`
はどちらかしか指定できない.
どちらも指定されない場合, ファイル出力されない.

##### `--output-separator CHAR` / `--out-sep CHAR`
出力における区切り文字を指定するコマンドである.
指定されない場合, `\t` が区切り文字となる.

区切り文字に指定できる文字パターンは次の通りである.
- ASCII 1文字
- エスケープシーケンス: `"\t"`, `"\\"`

##### `--output-label-prefix STR` / `--out-label-prefix STR`
出力のラベル行の先頭につける文字列を指定するコマンドである.
指定されない場合, ラベルを出力しない.

##### `--echo-off`
結果を標準出力しないようにするコマンドである.
指定されない場合, 標準出力に計算結果が表示される.

#### Input file format
入力ファイル形式は, 次の通りである.
- エスケープ文字はオプションで指定する.
- ソースの値は 1 column で縦に並べる.
- ソースの値には inf, NaN を含まない.

以下は, エスケープ文字を `#` とした場合の例である.

```input-file-example.txt
#data
0.0
1.0
2.0
```

#### Output
出力は標準出力であり,
オプションが指定された場合にはファイルにも出力される.
- 先頭行はラベル (オプションで指定された場合)
- 2 columns で出力 (`<x><sep><density>`)

以下は, ラベルヘッダーを `//`, 区切り文字を `,` とした場合の例である.

```output-file-example.txt
//x,density
0.0,0.25
1.0,0.5
2.0,0.25
```

## Application: kde2d
`kde2d` は2次元のカーネル密度推定の実行を表すコードネームである.

### Execution
共通処理として, ターミナルを起動し, カレントディレクトリを `root` に移動する.
その後, Windows と macOS / Linux で次のようになる.

#### Windows
```
.\kde2d.bat <パラメータ>
```

コマンドプロンプトを使用している場合, 次でもよい.

```
kde2d <パラメータ>
```

#### macOS / Linux
```
./kde2d.sh <パラメータ>
```

スクリプトに実行権限がない場合は, 初回時に次を実行する.

```
chmod +x kde2d.sh
```

### Usage

#### Parameter
使用するパラメータは次である.

- `--input FILE` / `--in FILE`
- `--input-comment-prefix STR` / `--in-comment-prefix STR` 
- `--input-separator CHAR` / `--in-sep CHAR`
- `--output FILE` / `--out FILE`, または `--output-force FILE` / `--out-force FILE`
- `--output-separator CHAR` / `--out-sep CHAR`
- `--output-format FORMAT` / `--out-format FORMAT`
- `--output-label-prefix STR` / `--out-label-prefix STR`
- `--echo-off`

##### `--input FILE` / `--in FILE`
入力ファイルパスを指定するコマンドである.
このパラメータは必ず指定されなければならない.
指定されない場合, 例外がスローされる.

##### `--input-comment-prefix STR` / `--in-comment-prefix STR`
入力ファイルのコメント行の開始文字列を指定するコマンドである.
指定されない場合, `#` がコメント開始文字列となる.

Prefixに空文字は禁止されている.
Prefix, 入力ファイルの各行の前後の空白は無視される.

##### `--input-separator CHAR` / `--in-sep CHAR`
入力ファイルにおける区切り文字を指定するコマンドである.
指定されない場合, `\t` が区切り文字となる.

区切り文字に指定できる文字パターンは次の通りである.
- ASCII 1文字
- エスケープシーケンス: `"\t"`, `"\\"`

##### `--output FILE` / `--out FILE`, または `--output-force FILE` / `--out-force FILE`
結果のファイル出力を行うコマンドである.
`--output` はファイルが存在した場合はエラーとなり,
`--output-force` はファイルが存在した場合は上書きする.
ファイルパスまでのディレクトリは自動で生成される.

`--output-force`, `--output`
はどちらかしか指定できない.
どちらも指定されない場合, ファイル出力されない.

##### `--output-separator CHAR` / `--out-sep CHAR`
出力における区切り文字を指定するコマンドである.
指定されない場合, `\t` が区切り文字となる.

区切り文字に指定できる文字パターンは次の通りである.
- ASCII 1文字
- エスケープシーケンス: `"\t"`, `"\\"`

##### `--output-format FORMAT` / `--out-format FORMAT`
結果出力のフォーマット形式を指定する.
形式名は次の通りである.

- `xyz`: 1行が1値を表す, 縦持ち形式を表す.
- `xyz-block`: 1行が1値を表す縦持ちで, メジャー値 (x値) のまとまりでブロック構造をとる形式を表す.
- `matrix`: 値を2次元に並べて表す, 行列形式を表す.

それぞれの形式に対する出力例は後述.
指定されない場合は `xyz` となる.

##### `--output-label-prefix STR` / `--out-label-prefix STR`
出力のラベル行の先頭につける文字列を指定するコマンドである.
指定されない場合, ラベルを出力しない.

##### `--echo-off`
結果を標準出力しないようにするコマンドである.
指定されない場合, 標準出力に計算結果が表示される.

#### Input file format
入力ファイル形式は, 次の通りである.
- ソースの値は 2 columns で記述 (`<x><sep><y>`).
- エスケープ文字, 区切り文字はオプションで指定する.
- ソースの値には inf, NaN を含まない.

以下は, エスケープ文字を `#`, 区切り文字を `,` とした場合の例である.

```input-file-example.txt
#x,y
0.0,1.0
1.0,2.0
2.0,1.0
```

#### Output
出力は標準出力であり,
オプションが指定された場合にはファイルにも出力される.

##### "xyz" format type
"xyz" 形式の場合は次のようになる.
- 先頭行はラベル (オプションで指定された場合)
- 3 columns で出力 (`<x><sep><y><sep><density>`)

以下は, ラベルヘッダーを `//`, 区切り文字を `,` とした場合の例である.

```output-file-example-xyz.txt
//x,y,density
0.0,0.0,0.0
0.0,1.0,0.125
0.0,2.0,0.0
1.0,0.0,0.125
1.0,1.0,0.5
1.0,2.0,0.125
2.0,0.0,0.0
2.0,1.0,0.125
2.0,2.0,0.0
```

##### "xyz-block" format type
"xyz-block" 形式の場合は次のようになる.
- 先頭行はラベル (オプションで指定された場合)
- 3 columns で出力 (`<x><sep><y><sep><density>`)
- `x` の値が変化するところに1行の空行

以下は, ラベルヘッダーを `//`, 区切り文字を `,` とした場合の例である.

```output-file-example-xyz-block.txt
//x,y,density
0.0,0.0,0.0
0.0,1.0,0.125
0.0,2.0,0.0

1.0,0.0,0.125
1.0,1.0,0.5
1.0,2.0,0.125

2.0,0.0,0.0
2.0,1.0,0.125
2.0,2.0,0.0
```

##### "matrix" format type
"matrix" 形式の場合は次のようになる.
- ラベル無し (`--output-label-prefix` オプションも無視する)
- 行列形式で出力.
    - 左上が空
    - 先頭行は `y` の値
    - 先頭列は `x` の値

以下は, 区切り文字を `,` とした場合の例である.

```output-file-example-matrix.txt
,0.0,1.0,2.0
0.0,0.0,0.125,0.0
1.0,0.125,0.5,0.125
2.0,0.0,0.125,0.0
```

## Help
`root` に移動後, 次のようなコマンドを実行することで,
パラメータの指定に関するヘルプを表示する.

```
.\kde1d-h.bat
.\kde2d-h.bat
./kde1d-h.sh
./kde2d-h.sh
```

## Debug Mode
実行の `kde1d`, `kde2d` を `kde1d-debug`, `kde2d-debug`
とすることで, エラーメッセージ等がより詳細に取得できるようになる.

---

## History
更新履歴は history.txt を参照のこと.

## License
This project is licensed under the MIT License, see the LICENSE.txt file for details.
