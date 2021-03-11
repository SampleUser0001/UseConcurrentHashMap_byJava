package sample.map;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import sample.map.enums.GeneratorEnum;
import sample.map.model.LogModel;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class App {
  private static final int WAIT_TIME = 60;
  
  public static void main( String[] args ){
    int argsIndex = 0;
    final int THREAD_COUNT = Integer.parseInt(args[argsIndex++]);
    final int LOOP_COUNT = Integer.parseInt(args[argsIndex++]);

    List<ExecutorService> execList = new ArrayList<ExecutorService>(GeneratorEnum.values().length);
    for(GeneratorEnum gen : GeneratorEnum.values()){
      if(gen.isExec()){
        ExecutorService exec = Executors.newFixedThreadPool(THREAD_COUNT);
        for(int i=0;i<THREAD_COUNT;i++){
          exec.execute(gen.taskFactory(LOOP_COUNT));
        }
        execList.add(exec);
      }
    }

    for(ExecutorService exec : execList){
      exec.shutdown();
      try {
        if( !exec.awaitTermination(WAIT_TIME, TimeUnit.SECONDS) ){
          exec.shutdownNow();
          if(!exec.awaitTermination(WAIT_TIME, TimeUnit.SECONDS)){
            System.err.println("cannot shutdown");
          }
        }
      } catch (InterruptedException e){
        exec.shutdownNow();
        Thread.currentThread().interrupt();
      }
    }

    for(GeneratorEnum result : GeneratorEnum.values()){
      if(result.isExec()){
        String name = result.name();
        System.out.println(result.name() + " : Log");
        getLastLogs(result.getLogList())
          .forEach(list -> {
            list.stream().forEach(log -> {System.out.printf("%s,%s\n",name,log);});
          });
        // result.getLogList()
        //   .stream()
        //   .forEach(log -> {
        //     System.out.printf("All %s,%s\n",name,log);
        //   });

        System.out.println(name + " : Map");
        result.getMap()
              .forEach((k,v) -> {
                System.out.printf("%s,%d,%d\n",name,k,v);
              });
      }
    }
  }

  private static final Stream<List<LogModel>> getLastLogs(List<LogModel> originalLog) throws IllegalArgumentException {
    Map<Integer,List<LogModel>> lastLogMap = new HashMap<Integer,List<LogModel>>();
    for(LogModel log : originalLog){
      if((!lastLogMap.containsKey(log.getKey()))){
        List<LogModel> logList = new ArrayList<LogModel>();
        logList.add(log);
        lastLogMap.put(log.getKey(), logList);
      } else if(lastLogMap.get(log.getKey()).get(0).getTime() < log.getTime()){
        List<LogModel> logList = new ArrayList<LogModel>();
        logList.add(log);
        lastLogMap.put(log.getKey(), logList);
      } else if(lastLogMap.get(log.getKey()).get(0).getTime() == log.getTime()){
        lastLogMap.get(log.getKey()).add(log);
      }
    }
    return lastLogMap.values().stream();
  }

}
