# UseConcurrentHashMap_byJava

JavaのConcurrentHashMapの動作確認

## 概要

マルチスレッドでMap.put, ConcurrentHashMap.put, ConcurrentHashMap.putIfAbsentでmapに登録した場合の動作確認プロジェクト。

## 詳細

下記の表のインスタンスに対して、表に記載したメソッドで登録する。

| 登録対象インスタンス | 登録メソッド | 出力ワード |
| :-- | :-- | :-- |
| HashMap | put | NORMAL |
| ConcurrentHashMap | put | CONCURRENT_NOT_ATOMIC |
| ConcurrentHashMap | putIfAbsent | CONCURRENT_ATOMIC |

起動時にスレッド数と、設定する各スレッドが実行するput回数（キーの数）を指定する。  
各インスタンスに対して、指定したスレッド数×指定した回数putする。  
putした直後に、実行した時刻と、putしたキー、値をログとしてListに詰める。(実態はArrayList。)  

最後に各Mapインスタンスで保持している値と、ログとして保持しているListの値を出力ワードとともに出力する。
ログはキーが同一の値のうち、実行時刻が最大（最後にputされたと思われるもの）のみを出力する。
Mapインスタンスの値とログとして出力された値とが一致しない場合、上書きが発生していると推測できる。

### 備考

1. Javaは実質ミリ秒までしか扱えないので、ログ時刻が同値になる可能性がある。その場合は両方出力。
2. CONCURRENT_ATOMICのみ、挙動が異なる。
   - putIfAbsentメソッドの効果を見るため、キーの重複が判明した場合は乱数でキーを再生性してputしている。

## 実行

``` sh
mvn clean compile exec:java -Dexec.mainClass="sample.map.App" -Dexec.args="'スレッド数' 'put回数'"
```

下記の場合は、100スレッドで、1つのスレッドがputする回数が10回。

``` sh
mvn clean compile exec:java -Dexec.mainClass="sample.map.App" -Dexec.args="'100' '10'"
```

## 参考

- [kencharosの日記:ConcurrentHashMapを使うときの注意点](https://kencharos.hatenablog.com/entry/2013/08/23/131232)
