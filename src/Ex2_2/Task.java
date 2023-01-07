import java.util.concurrent.Callable;

public class Task implements Comparable<Task> {
    // This type is protected so that it can be accessed by the CustomExecutor class
    protected TaskType taskType; // section 1

    private Callable<?> callable;

    // this constructor is protected, so it can only be used by the TaskFactory class
    protected Task(Callable<?> callable, TaskType taskType) {
        this.callable = callable;
        this.taskType = taskType;
    }

    public Task getTask() { // section 2
        try {
            return this;
        } catch (Exception e) {
            System.err.println("Error: " + e);
        }
        return null;
    }

    public static Task createTask(Callable<?> callable){ // section 3 + 4
        TaskFactory taskFactory = new TaskFactory();
        return taskFactory.createTask(callable);
    }

    public static Task createTask(Callable<?> callable, TaskType taskType) { // section 3 + 4
        TaskFactory taskFactory = new TaskFactory();
        return taskFactory.createTask(callable, taskType);
    }


    @Override
    public int compareTo(Task otherTask) { // section 5
        if (this.taskType.getPriorityValue() > otherTask.taskType.getPriorityValue()) {
            return 1;
        } else if (this.taskType.getPriorityValue() < otherTask.taskType.getPriorityValue()) {
            return -1;
        } else {
            return 0;
        }
    }
}
