import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.Callable;

import static java.lang.Thread.sleep;

public class CustomExecutor {
    private Queue<Task> tasks;
    private final int numOfProcessors = Runtime.getRuntime().availableProcessors();
    private final int minNumOfThreads = numOfProcessors / 2; // section 5
    private final int maxNumOfThreads = numOfProcessors - 1; // section 6
    private Task currentMaxPriority;

    public void addTask(Task task){ // section 1
        tasks.add(task);
        this.tasks = orderQueueByPriority();
    }

    public void addTask(Callable<?> callable, TaskType taskType) throws InterruptedException { // section 2
        if (this.tasks.size() <= minNumOfThreads){ // section 8
            return;
        }
        Task task = Task.createTask(callable, taskType);
        if (!okToExecuteThread()){
            sleep(300);
            if (!okToExecuteThread()){
                task = null; // terminate the thread
                return;
            }
        }
        tasks.add(task);
        this.tasks = orderQueueByPriority();
    }

    public void addTask(Callable<?> callable){ // section 3
        Task task = Task.createTask(callable);
        tasks.add(task);
        this.tasks = orderQueueByPriority();
    }

    private Queue<Task> orderQueueByPriority() { // section 7
        Queue<Task> priorityQueue = new PriorityQueue<Task>(new CustomExecutorComparator());
        priorityQueue.addAll(this.tasks);

        currentMaxPriority = priorityQueue.peek(); // section 10
        return priorityQueue;
    }

    public Task getCurrentMaxPriority() { // section 10
        return currentMaxPriority;
    }

    private boolean okToExecuteThread() { // section 8
        if ((this.tasks.size() > minNumOfThreads) || (this.tasks.size() <= maxNumOfThreads)) {
            return true;
        } else {
            return false;
        }
    }


}
