package com.helloPratham.springJwt.controller;

import com.helloPratham.springJwt.entity.Status;
import com.helloPratham.springJwt.entity.Task;
import com.helloPratham.springJwt.entity.UpdateTask;
import com.helloPratham.springJwt.security.JwtUtil;
import com.helloPratham.springJwt.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseEntity<String> createTask(@RequestBody Task task,
                                           @RequestHeader("Authorization") String token) {
        Task createdTask = taskService.createTask(task, token);
        return ResponseEntity.status(HttpStatus.CREATED).body("Task Created Successfully with Task-id: "+ task.getId());
    }

    @PostMapping("/{taskId}")
    public ResponseEntity<String> updateTaskStatus(@PathVariable Long taskId, @RequestBody UpdateTask updateTask) {
        taskService.updateTaskStatus(taskId, updateTask);
        return new ResponseEntity<>("Updated Successfully", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return new ResponseEntity<>(taskService.getAllTasks(), HttpStatus.OK);
    }

    @GetMapping("/{priorityName}")
    public ResponseEntity<List<Task>> getAllPriorityTasks(@PathVariable String priorityName) {
        List<Task> prioritizedTasks = taskService.getAllPriorityTasks(priorityName);
        return new ResponseEntity<>(prioritizedTasks, HttpStatus.OK);
    }


    @PostMapping("/dueDate")
    public ResponseEntity<List<Task>> getAllDueDateImportantTasks(@RequestBody String dueDate) {
        List<Task> dueDateImportantTasks = taskService.getAllDueDateImportantTasks(dueDate);
        return new ResponseEntity<>(dueDateImportantTasks, HttpStatus.OK);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        taskService.softDeleteTask(id);
        return new ResponseEntity<>("Task Deleted Successfully",HttpStatus.OK);
    }
}

