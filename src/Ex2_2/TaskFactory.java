import java.util.concurrent.Callable;

// This class is for sections 3, 4 of the "Task" class exercise
// A TaskFactory is a class that creates Task objects
public class TaskFactory {
    public Task createTask(Callable<?> callable, TaskType taskType) {
        return new Task(callable, taskType);
    }

    public Task createTask(Callable<?> callable) {
        return new Task(callable, TaskType.COMPUTATIONAL);
    }
}
