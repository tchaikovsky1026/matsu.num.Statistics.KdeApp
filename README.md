# matsu.num.Statistics.KdeApp
`matsu.num.Statistics.KdeApp` は Java 言語でカーネル密度推定を実行する App を提供する.

現在は開発中バージョン `0.2.0` であり, Java 17 に準拠する.

## Dependency
このソフトウェアは次のモジュールを要求する.

- `matsu.num.Statistics.KernelDensity`, version `1` 系最新版

## Installation
- zipアーカイブを展開する.
    - `matsu.num.Statistics.KdeApp.jar` があるディレクトリを `root` とする.
- 依存ライブラリを `root/lib` に配置する.

---
## Application: kde1d
`kde1d` は1次元のカーネル密度推定の実行を表すコードネームである.

### Execution
実行方法は, Windows と macOS / Linux で次のようになる.

#### Windows

```
kde1d.bat <パラメータ>
```
または

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
パラメータは次の形で与える.

##### `--input-file <入力ファイルパス>` または `-f <入力ファイルパス>`
入力ファイルパスを指定するコマンドである.
このパラメータは必ず指定されなければならない.
指定されない場合, 例外がスローされる.

##### `--comment-char <文字列>`
入力ファイルのコメント行の開始文字列を指定するコマンドである.
指定されない場合, `#` がコメント開始文字列となる.

##### `--separator <文字>` または `-sep <文字>`
出力における区切り文字を指定するコマンドである.
指定されない場合, `\t` が区切り文字となる.

区切り文字に指定できる文字パターンは次の通りである.
- ASCII 1文字
- エスケープシーケンス: `"\t"`, `"\r"`, `"\n"`, `"\\"`

##### `--label-header <文字列>`
出力のラベル行の先頭につける文字列を指定するコマンドである.
指定されない場合, ラベルを出力しない.

##### `--output-force <出力ファイルパス>` または `-out-f <出力ファイルパス>`
結果のファイル強制出力を行うコマンドである.
ファイルが存在しても, 出力を試みる.
指定されない場合, ファイル出力されない.

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
出力は標準出力のみである.
- 先頭行はラベル (オプションで指定された場合)
- 2 columns で出力 (`<x><sep><density>`)

以下は, ラベルヘッダーを `//`, 区切り文字を `,` とした場合の例である.

```output-file-example.txt
//x,density
0.0,0.25
1.0,0.5
2.0,0.25
```

---
## History
更新履歴は history.txt を参照のこと.

## License
This project is licensed under the MIT License, see the LICENSE.txt file for details.
