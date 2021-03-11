package sample.map.model;

// import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class LogModel {
  private final long time;
  private final int key;
  private final long threadId;
}
