package sample.map;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Hello world!
 *
 */
public class App {
  public static final int GENARATE_THREAD_COUNT = 10;
  public static final int LOOP_COUNT = 1000;
  public static final int TASK_COUNT = 20;

  public static Map<String,String> map = new HashMap<String,String>();
  public static Map<String,String> concurrentMap = new ConcurrentHashMap<String,String>();

  public static void main( String[] args ){
    ExecutorService exec = Executors.newFixedThreadPool(GENARATE_THREAD_COUNT);
    for(int i=0; i<TASK_COUNT;i++){
      exec.submit(new Task(map, LOOP_COUNT));
      exec.submit(new Task(concurrentMap, LOOP_COUNT));
    }
    exec.shutdown();
    
    try{
      if( !exec.awaitTermination(60, TimeUnit.SECONDS) ){
        exec.shutdownNow();
        if(!exec.awaitTermination(60, TimeUnit.SECONDS)){
          System.err.println("cannot shutdown");
        }
      }
    } catch (InterruptedException e){
      exec.shutdownNow();
      Thread.currentThread().interrupt();
    }
  }
}
