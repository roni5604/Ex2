
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.*;

import static java.lang.Thread.sleep;

/**
 * A CustomExecutor is a class that implements the Executor interface and has a priority queue
 * and a constructor that sets the priority queue
 */
public class CustomExecutor  extends ThreadPoolExecutor {

    private Queue<Task> tasks;// the priority queue of tasks
    private final int numOfProcessors = Runtime.getRuntime().availableProcessors(); // number of processors
    private final int minNumOfThreads = numOfProcessors / 2; // section 5
    private final int maxNumOfThreads = numOfProcessors - 1; // section 6
    private int currentMaxPriority;// the current max priority of the tasks in the queue

    /**
     * Creates a new CustomExecutor that will, upon running, execute the given
     */
    public CustomExecutor() {
        super(1, 1, 300L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    }


    /**
     *  submits a task to the executor and adds it to the priority queue
     * @param callable the callable task to be executed
     * @param taskType the type of the task
     * @return the Task that was submitted
     */
    public <T> Future<T> submit(Callable<T> callable, TaskType taskType) throws Exception { // section 2
//        if (this.tasks.size() <= minNumOfThreads) { // section 8
//            throw new InterruptedException("The number of tasks is less than the minimum number of threads");
//        }
        if (callable == null) {
            throw new NullPointerException("Callable is null");
        }
        if (taskType == null) {
            throw new NullPointerException("TaskType is null");
        }
        Task task = Task.createTask(callable, taskType);
//        tasks.add(task);
        TaskFactory<T> taskFactory = new TaskFactory<T>(task.getCallable(), taskType.getPriorityValue());
        execute(taskFactory);
        return taskFactory;
    }

    /**
     * submits a task to the executor and adds it to the priority queue
     * @param callable the task to submit
     * @return the Task that was submitted
     */
    public <T> Future<T> submit(Callable<T> callable) { // section 3
        if (callable == null) {
            throw new NullPointerException("Callable is null");
        }
        if (callable.getClass().equals(Task.class)) {
            Task task = (Task) callable;
//            tasks.add(task);
            TaskFactory<T> taskFactory = new TaskFactory<T>(task.getCallable(), task.getPriority());
            execute(taskFactory);
            return taskFactory;
        } else {
            Task task = Task.createTask(callable);
//            tasks.add(task);
            TaskFactory<T> taskFactory = new TaskFactory<T>(task.getCallable(), task.getPriority());
            execute(taskFactory);
            return taskFactory;
        }
    }


    /**
     * blocks the current thread until the executor has no more tasks to execute
     */
    public BlockingQueue<Runnable> getQueueP() {
        return super.getQueue();
    }

    @Override
    /**
     * after the executor has no more tasks to execute, it shuts down
     */
    protected void afterExecute(Runnable r, Throwable t) {

        if (!super.getQueue().isEmpty()) {
            if (((TaskFactory) r).getPriority() < ((TaskFactory) super.getQueue().peek()).getPriority()) {
                this.currentMaxPriority = ((TaskFactory) super.getQueue().peek()).getPriority();
            }
        }

    }

    /**
     * this method shuts down the executor after it has no more tasks to execute
     */
    public void gracefullyTerminate() {
        super.shutdown();
    }


    /**
     * returns the current max priority of the tasks in the queue
     * @return the current max priority of the tasks in the queue
     */
    public int getCurrentMax() { // section 10
        return currentMaxPriority;
    }


    /**
     * ok to use this method to get the number of threads in the executor (for testing purposes)
     * @return true if the number of threads is between the min and max number of threads and false otherwise
     */
    private boolean okToExecuteThread() { // section 8
        if ((this.tasks.size() > minNumOfThreads) || (this.tasks.size() <= maxNumOfThreads)) {
            return true;
        } else {
            return false;
        }
    }



}
