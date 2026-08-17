# デバッグ記録

## 契約

同じ商品IDへの2回の `GET /products/{id}` は同じJSONを返し、2回目はRepositoryへ再読込を発生させない。HTTP 200だけではキャッシュの成立を証明しないため、Repositoryの読み出し回数を独立に確認する。

## 環境とコマンド

| 項目 | 実測値 |
| --- | --- |
| Java | 21.0.11 |
| Spring Boot | 3.3.5 |
| Maven | 3.8.7 |
| 失敗確認 | `mvn -q test` |
| 修正後確認 | `mvn -q test` |

最初はJDKのコンパイラが不足していたため `release version 21 not supported` となった。これはアプリケーション不具合ではなく、`openjdk-21-jdk` を導入して解消した。

## バグ状態の観測

2回のHTTP応答はどちらも `200` で、本文は次のとおりだった。

```json
{"id":"p-1","name":"Notebook"}
```

しかし、回帰テストの独立観測値は次の差分になった。

```text
expected: 1
 but was: 2
```

## 仮説の切り分け

| 仮説 | 観測 | 判断 |
| --- | --- | --- |
| ControllerのJSON変換が壊れている | 2回ともHTTP 200、本文も同一 | 除外 |
| Repositoryが呼ばれていない | 読み出し回数は2 | 除外 |
| Cacheableのキーが異なる | 2回とも同じID `p-1` | 可能性を下げる |
| `@Cacheable` のインターセプト境界を通っていない | `ProductService.get` から同一インスタンスの `load` を直接呼んでいる | 採用 |

## 原因

`@EnableCaching` によりキャッシュ処理はSpringのproxyベースのアドバイスで適用される。`ProductService.get` から `this` 相当の同一bean内部呼び出しで `load` を実行すると、proxyを経由しないため、`@Cacheable` の処理が実行されない。

## 最小修正

`@Cacheable` を `ProductCacheService` へ移し、`ProductService` が別Spring beanの `load` を呼ぶ構成に変更した。これにより呼び出しはSpring proxyを通り、2回目はRepositoryを読まない。

## 回帰確認

修正後に同じ `mvn -q test` を実行し成功した。テストはJSONの内容だけでなく、Repositoryの読み出し回数1回を保持している。

## 制約

この教材はSpringのproxyモードを説明するため、単純なインメモリRepositoryを使う。RedisやCaffeineなどの本番キャッシュ製品のTTL、分散無効化、シリアライズは対象外である。また、AspectJ modeへ切り替える設計ではなく、責務分離による最小修正を選んだ。
