
import java.util.HashMap;
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

    private HashMap<Integer, Integer> priorityToNumOfThreads = new HashMap<>(); // section 7

    private  int count_max_priority = 0;
    /**
     * Creates a new CustomExecutor that will, upon running, execute the given
     */
    public CustomExecutor() {
        super(Runtime.getRuntime().availableProcessors()/2, Runtime.getRuntime().availableProcessors()-1, 300L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
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
        if(task.getPriority() < currentMaxPriority){
            currentMaxPriority = task.getPriority();
        }
        priorityToNumOfThreads.put(taskType.getPriorityValue(), priorityToNumOfThreads.getOrDefault(taskType.getPriorityValue(), 0) + 1);
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
            if(task.getPriority() < currentMaxPriority){
                currentMaxPriority = task.getPriority();
            }
            priorityToNumOfThreads.put(task.getPriority(), priorityToNumOfThreads.getOrDefault(task.getPriority(), 0) + 1);
            execute(taskFactory);
            return taskFactory;
        } else {
            Task task = Task.createTask(callable);
//            tasks.add(task);
            TaskFactory<T> taskFactory = new TaskFactory<T>(task.getCallable(), task.getPriority());
            if(task.getPriority() < currentMaxPriority){
                currentMaxPriority = task.getPriority();
            }
            priorityToNumOfThreads.put(task.getPriority(), priorityToNumOfThreads.getOrDefault(task.getPriority(), 0) + 1);
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
     * before executing a task, checks if the task's priority is higher than the current max priority
     * if it is, the current max priority is updated
     * @param r the task to be executed
     *          @param t the thread that will execute the task
     *
     */
    protected void beforeExecute(Thread t, Runnable r) {
     priorityToNumOfThreads.put(((TaskFactory) r).getPriority(), priorityToNumOfThreads.get(((TaskFactory) r).getPriority()) - 1);
        if (((TaskFactory) r).getPriority() > currentMaxPriority) {
            currentMaxPriority = ((TaskFactory) r).getPriority();
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
        return this.currentMaxPriority;
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
