package com.ssp.api.monitor;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/18/17
 * Time: 10:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExecutorServiceStatus {

    private Integer poolSize;
    private Integer corePoolSize;
    private Integer maximumPoolSize;
    private Integer activeCount;
    private Long completedTaskCount;
    private Long taskCount;
    private boolean terminated;

    public Integer getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(Integer poolSize) {
        this.poolSize = poolSize;
    }

    public Integer getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public Integer getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(Integer maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public Integer getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(Integer activeCount) {
        this.activeCount = activeCount;
    }

    public Long getCompletedTaskCount() {
        return completedTaskCount;
    }

    public void setCompletedTaskCount(Long completedTaskCount) {
        this.completedTaskCount = completedTaskCount;
    }

    public Long getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Long taskCount) {
        this.taskCount = taskCount;
    }

    public boolean isTerminated() {
        return terminated;
    }

    public void setTerminated(boolean terminated) {
        this.terminated = terminated;
    }


    @Override
    public String toString() {
        return "ExecutorServiceStatus{" +
                "Current Pool Size=" + poolSize +
                ", Core Pool Size=" + corePoolSize +
                ", Maximum Pool Size=" + maximumPoolSize +
                ", Active Count=" + activeCount +
                ", Completed Task Count=" + completedTaskCount +
                ", Total Task Count=" + taskCount +
                ", terminated=" + terminated +
                '}';
    }
}
