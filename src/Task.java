
import java.util.concurrent.Callable;

/**
 * A Task is a class that implements the Callable interface and has a priority field
 * and a constructor that sets the priority field
 */
public class Task implements Callable {
    // This type is protected so that it can be accessed by the CustomExecutor class
    private TaskType taskType;// the type of the task
    private  int priority;// the priority of the task
    private Callable callable;// the callable task to be executed



    // this constructor is protected, so it can only be used by the TaskFactory class

    /**
     * Creates a new Task that will, upon running, execute the given
     * @param callable the callable task to be executed
     * @param taskType the type of the task
     * @param priority the priority of the task
     */
    protected Task(Callable callable, TaskType taskType, int priority) {
        this.callable = callable;
        this.taskType = taskType;
        this.priority = priority;
    }

    /**
     * returns the task
     * @return the Task
     */
    public Task getTask() { // section 2
        try {
            return this;
        } catch (Exception e) {
            System.err.println("Error: " + e);
        }
        return null;
    }

    /**
     *  creates a new Task that will, upon running, execute the given
     * @param callable the callable task to be executed
     * @return the Task
     */
    public static <T> Task createTask(Callable<T> callable){ // section 3 + 4
        return new Task(callable,TaskType.OTHER, 3);
    }

    /**
     * Creates a new Task that will, upon running, execute the given
     * @param callable the callable task to be executed
     * @param taskType the type of the task
     * @return the Task
     */
    public static <T> Task createTask(Callable<T> callable, TaskType taskType) { // section 3 + 4
        return new Task(callable, taskType, taskType.getPriorityValue());

    }

    /**
     * returns the priority of the task
     * @return the priority of the task
     */
    public int getPriority() {
        return this.priority;
    }

    /**
     * sets the priority of the task
     * @param priority the priority of the task
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * returns the Callable task to be executed
     * @return the Callable task to be executed
     */
    public <T> Callable<T> getCallable(){
        return this.callable;
    }

    /**
     * compares the priority of the task to the priority of another task
     * @param otherTask the other task to compare to
     * @return 1 if the priority of the task is greater than the priority of the other task
     *        -1 if the priority of the task is less than the priority of the other task
     *        0 if the priority of the task is equal to the priority of the other task
     */
    public int compareTo(Task otherTask) { // section 5
        if (this.taskType.getPriorityValue() > otherTask.taskType.getPriorityValue()) {
            return 1;
        } else if (this.taskType.getPriorityValue() < otherTask.taskType.getPriorityValue()) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    /**
     * calls the callable task to be executed
     * @return the result of the callable task to be executed
     */
    public Object call() throws Exception {
        return this.callable.call();
    }
}
