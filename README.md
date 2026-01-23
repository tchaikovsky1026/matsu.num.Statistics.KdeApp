# matsu.num.Statistics.KdeApp
`matsu.num.Statistics.KdeApp` は Java 言語でカーネル密度推定を実行する App を提供する.

現在は開発中バージョン `0.1.0` であり, Java 17 に準拠する.

## Dependency
このソフトウェアは次のモジュールを要求する.

- `matsu.num.Statistics.KernelDensity`

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
パラメータは単一で, `<入力ファイル名>` のみである.

#### 入力ファイル形式
入力ファイル形式は, 次の通りである.
- エスケープ文字が "#"
- ソースの値は 1 column で縦に並べる
- ソースの値には inf, NaN を含まない

```input-file-example.txt
#data
0.0
1.0
2.0
```

#### 出力
出力は標準出力のみである.
- ラベル無しのタブ区切り 2 columns で出力, `<x><\tab><density>`

---
## History
更新履歴は history.txt を参照のこと.

## License
This project is licensed under the MIT License, see the LICENSE.txt file for details.
