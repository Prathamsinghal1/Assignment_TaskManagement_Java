package com.helloPratham.springJwt.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helloPratham.springJwt.entity.*;
import com.helloPratham.springJwt.repository.SubTaskRepository;
import com.helloPratham.springJwt.repository.TaskRepository;
import com.helloPratham.springJwt.repository.UserRepository;
import com.helloPratham.springJwt.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubTaskRepository subTaskRepository;

    @Autowired
    private JwtUtil jwtUtil;


//    @Autowired
//    private TwilioService twilioService;

    private Priority calculatePriority(LocalDate dueDate) {
        long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
        if (daysRemaining <= 0) return Priority.CRITICAL;
        if (daysRemaining <= 2) return Priority.HIGH;
        if (daysRemaining <= 4) return Priority.MEDIUM;
        return Priority.LOW;
    }

    public Task createTask(Task task, String token) {
        String userId = jwtUtil.extractUserIdFromToken(token);

        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));
        task.setUser(user);

        // Calculate priority and set status
        task.setPriority(calculatePriority(task.getDueDate()));
        task.setStatus(Status.TODO);

        // Save the task
        return taskRepository.save(task);
    }

    public Task updateTaskStatus(Long taskId, UpdateTask updateTask) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setStatus(updateTask.getStatus());
        task.setDueDate(updateTask.getDueDate());
        return taskRepository.save(task);
    }

    public void softDeleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        task.setDeletedAt(LocalDateTime.now());
        taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .filter(task -> task.getDeletedAt() == null)
                .toList(); // Requires Java 16+. Use `Collectors.toList()` for earlier versions.
    }

    public List<Task> getAllPriorityTasks(String priorityName) {
        List<Task> tasks = taskRepository.findAll();
        Priority priority;
        try {
            priority = Priority.valueOf(priorityName.toUpperCase()); // Ensure case matches the enum
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid priority name: " + priorityName);
        }

        // Filter tasks by priority
        return tasks.stream()
                .filter(task -> task.getDeletedAt() == null && task.getPriority() == priority)
                .toList();
    }

    public List<Task> getAllDueDateImportantTasks(String inputJson) {
        List<Task> tasks = taskRepository.findAll();

        try {
            // Parse JSON to extract the "dueDate" field
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(inputJson);
            String dueDate = jsonNode.get("dueDate").asText();

            // Convert the extracted due date to LocalDate
            LocalDate parsedDueDate = LocalDate.parse(dueDate);

            // Filter tasks by due date
            tasks = tasks.stream()
                    .filter(task -> task.getDeletedAt() == null &&
                            task.getDueDate().equals(parsedDueDate))
                    .toList();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid input or date format: " + inputJson, e);
        }

        return tasks;
    }

//    @Scheduled(cron = "0 0 0 * * ?") // Every Day at Midnight
//    public void updateTaskPriority() {
//        List<Task> tasks = taskRepository.findByDueDateBeforeAndDeletedAtIsNull(LocalDateTime.now());
//        for (Task task : tasks) {
//            Priority newPriority = calculatePriority(task.getDueDate());
//            task.setPriority(newPriority);
//            taskRepository.save(task);
//        }
//    }


//
//
//    @Scheduled(cron = "0 * * * * ?") // Every minute
//    public void callOverdueUsers() {
//        List<Task> overdueTasks = taskRepository.findByDueDateBeforeAndDeletedAtIsNull(LocalDateTime.now());
//        for (Task task : overdueTasks) {
//            twilioService.makeCall(task.getUser().getPhoneNumber(), "Task is overdue", true);
//        }
//    }

}
