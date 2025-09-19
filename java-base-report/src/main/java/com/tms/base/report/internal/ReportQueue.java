package com.tms.base.report.internal;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Queue management for report tasks.
 * 
 * @since 1.0.0
 */
public class ReportQueue {
    
    private final BlockingQueue<ReportTask<?>> taskQueue;
    private final Map<String, ReportTask<?>> activeTasks;
    private final AtomicLong taskCounter = new AtomicLong(0);
    private final int maxQueueSize;
    private final int maxActiveTasks;
    
    public ReportQueue(int maxQueueSize, int maxActiveTasks) {
        this.maxQueueSize = maxQueueSize;
        this.maxActiveTasks = maxActiveTasks;
        this.taskQueue = new LinkedBlockingQueue<>(maxQueueSize);
        this.activeTasks = new ConcurrentHashMap<>();
    }
    
    /**
     * Add task to queue.
     * 
     * @param task Report task
     * @return true if added successfully
     */
    public boolean addTask(ReportTask<?> task) {
        if (taskQueue.size() >= maxQueueSize) {
            return false;
        }
        return taskQueue.offer(task);
    }
    
    /**
     * Take next task from queue.
     * 
     * @return Next task or null if queue is empty
     */
    public ReportTask<?> takeTask() {
        try {
            return taskQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
    
    /**
     * Poll next task from queue.
     * 
     * @return Next task or null if queue is empty
     */
    public ReportTask<?> pollTask() {
        return taskQueue.poll();
    }
    
    /**
     * Register active task.
     * 
     * @param task Report task
     * @return true if registered successfully
     */
    public boolean registerActiveTask(ReportTask<?> task) {
        if (activeTasks.size() >= maxActiveTasks) {
            return false;
        }
        activeTasks.put(task.getTaskId(), task);
        return true;
    }
    
    /**
     * Unregister active task.
     * 
     * @param taskId Task ID
     * @return Removed task or null if not found
     */
    public ReportTask<?> unregisterActiveTask(String taskId) {
        return activeTasks.remove(taskId);
    }
    
    /**
     * Get active task by ID.
     * 
     * @param taskId Task ID
     * @return Active task or null if not found
     */
    public ReportTask<?> getActiveTask(String taskId) {
        return activeTasks.get(taskId);
    }
    
    /**
     * Generate next task ID.
     * 
     * @return Next task ID
     */
    public String generateTaskId() {
        return "task-" + taskCounter.incrementAndGet() + "-" + System.currentTimeMillis();
    }
    
    /**
     * Get queue size.
     * 
     * @return Current queue size
     */
    public int getQueueSize() {
        return taskQueue.size();
    }
    
    /**
     * Get active tasks count.
     * 
     * @return Current active tasks count
     */
    public int getActiveTasksCount() {
        return activeTasks.size();
    }
    
    /**
     * Check if queue is full.
     * 
     * @return true if queue is full
     */
    public boolean isQueueFull() {
        return taskQueue.size() >= maxQueueSize;
    }
    
    /**
     * Check if max active tasks reached.
     * 
     * @return true if max active tasks reached
     */
    public boolean isMaxActiveTasksReached() {
        return activeTasks.size() >= maxActiveTasks;
    }
    
    /**
     * Clear all tasks.
     */
    public void clear() {
        taskQueue.clear();
        activeTasks.clear();
    }
}
