package sample.map.enums;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;

import sample.map.model.LogModel;
import sample.map.task.Task;


public enum GeneratorEnum {
  NORMAL(
    true,
    new HashMap<Integer,Long>(),
    new ArrayList<LogModel>()){
    @Override
    public Task taskFactory(int loopCount) {
      return new Task(
        NORMAL.getMap(),
        loopCount,
        NORMAL.getLogList()) {

          @Override
          public void run(){
            for(int i=0;i<loopCount;i++){
              map.put(i,Thread.currentThread().getId());
              logList.add(new LogModel(System.currentTimeMillis(), i, Thread.currentThread().getId()));
              try {
                Thread.sleep(500);
              } catch(InterruptedException e){}
            }
          }
      };
    }
  },
  CONCURRENT_NOT_ATOMIC(
    true,
    new ConcurrentHashMap<Integer,Long>(),
    new ArrayList<LogModel>()){
    public Task taskFactory(int loopCount) {
      return new Task(
        CONCURRENT_NOT_ATOMIC.getMap(),
        loopCount,
        CONCURRENT_NOT_ATOMIC.getLogList()) {

          @Override
          public void run(){
            for(int i=0;i<loopCount;i++){
              map.put(i,Thread.currentThread().getId());
              logList.add(new LogModel(System.currentTimeMillis(), i, Thread.currentThread().getId()));
              try {
                Thread.sleep(500);
              } catch(InterruptedException e){}
            }
          }
      };
    }
  },
  CONCURRENT_ATOMIC(
    true, 
    new ConcurrentHashMap<Integer,Long>(),
    new ArrayList<LogModel>()){
    public Task taskFactory(int loopCount) {
      return new Task(
        CONCURRENT_ATOMIC.getMap(),
        loopCount,
        CONCURRENT_ATOMIC.getLogList()) {

          @Override
          public void run(){
            for(int i=0;i<loopCount;i++){
              Integer key = i;
              long concurrentThreadId = Thread.currentThread().getId();
              boolean isFirst = false;
              do {
                // putIfAbsentを使ってputする。
                // mapに存在しないキーでputするとnullが帰ってくる。（ループを抜ける）
                // mapに存在するキーでputした場合、putしようとした値が帰ってくる。（キーを変えてもう一度putする。）
                // （通常のputの場合は、もともとmapで保持していた値が帰ってくる。）
                isFirst = map.putIfAbsent(key,concurrentThreadId) == null;

                // ループを抜けられない場合は別のキーを生成する。
                if(!isFirst){
                  key = random.nextInt();
                }
              } while (!isFirst);
              logList.add(new LogModel(System.currentTimeMillis(), key, Thread.currentThread().getId()));
              try {
                Thread.sleep(500);
              } catch(InterruptedException e){}
          }
        }
      };
    }
  };

  private static Random random = new Random();

  private boolean isExec;
  private List<LogModel> logList;
  private Map<Integer,Long> map;

  private GeneratorEnum(boolean isExec , Map<Integer,Long> map, List<LogModel> list){
    this.isExec = isExec;
    this.map = map;
    this.logList = list;
  }

  public Map<Integer,Long> getMap(){
    return this.map;
  }
  public List<LogModel> getLogList(){
    return this.logList;
  }
  public boolean isExec(){
    return this.isExec;
  }

  public abstract Runnable taskFactory(int loopCount);

  private static final int nextInt(){
    return random.nextInt();
  }
}