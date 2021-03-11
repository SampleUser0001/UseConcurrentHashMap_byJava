package sample.map.task;

import java.util.Map;

import sample.map.model.LogModel;

import java.util.List;

public abstract class Task implements Runnable {
  protected Map<Integer,Long> map;
  protected int loopCount;
  protected List<LogModel> logList;
  public Task(Map<Integer,Long> map, int loopCount, List<LogModel> logList) {
    this.map = map;
    this.loopCount = loopCount;
    this.logList = logList;
  }
}
