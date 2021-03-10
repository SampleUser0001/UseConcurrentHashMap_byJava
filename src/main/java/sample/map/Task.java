package sample.map;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Task implements Runnable {
  private Map<String,String> map;
  private int loopCount;
  public Task(Map<String,String> map, int loopCount) {
    this.map = map;
    this.loopCount = loopCount;
  }

  @Override
  public void run(){
    String key = "";
    String value = "";

    for(int i=0;i<loopCount;i++){
      if(map instanceof ConcurrentHashMap) {
        map.putIfAbsent(key, value);
      } else {
        map.put(key,value);
      }
    }
  }
}
