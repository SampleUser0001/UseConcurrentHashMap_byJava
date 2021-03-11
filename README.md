# UseConcurrentHashMap_byJava

JavaのConcurrentHashMapの動作確認

## 概要


## 実行

``` sh
mvn clean compile exec:java -Dexec.mainClass="sample.map.App" -Dexec.args="'スレッド数' 'ループ数'"
```

mvn clean compile exec:java -Dexec.mainClass="sample.map.App" -Dexec.args="'100' '10'"

## 参考

- [kencharosの日記:ConcurrentHashMapを使うときの注意点](https://kencharos.hatenablog.com/entry/2013/08/23/131232)