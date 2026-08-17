# Spring Cache Self-Invocation Debugging Lab

`@Cacheable` を付けたメソッドを同じSpring beanから直接呼び出すと、HTTP応答は正常でもキャッシュが使われず、下流の読み出しが毎回発生する不具合を再現・修正する最小教材です。

## 前提

Java 21 と Maven 3.8 以降を使用します。外部データベースや資格情報は不要です。

## 実行

```bash
mvn test
```

回帰テストは2回の `GET /products/p-1` が同じJSONを返し、`ProductRepository` の読み出し回数が1回であることを、HTTP境界と独立した観測値で確認します。

## Git履歴

| コミット | 内容 |
| --- | --- |
| `7301aa4` | バグ状態。`ProductService` 内の自己呼び出しにより `@Cacheable` が適用されない。テストは `expected: 1, but was: 2` で失敗する。 |
| 修正コミット | `ProductCacheService` を分離し、Spring proxyを通る呼び出しへ変更する。 |

詳細な観測、仮説の切り分け、制約は `DEBUGGING_RECORD.md` に記録しています。
