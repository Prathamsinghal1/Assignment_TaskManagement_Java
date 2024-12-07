package com.helloPratham.springJwt.controller;


import com.helloPratham.springJwt.entity.SubTask;
import com.helloPratham.springJwt.service.SubTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sub-tasks")
public class SubTaskController {
    @Autowired
    private SubTaskService subTaskService;

    @PostMapping("/{taskId}")
    public ResponseEntity<String> createSubTask(@PathVariable Long taskId) {
        SubTask subTask = subTaskService.createSubTask(taskId);
        return ResponseEntity.ok("Subtask Created Successfully");
    }

    @PutMapping("/{id}/{status}")
    public ResponseEntity<String> updateSubTaskStatus(@PathVariable Long id, @PathVariable Integer status) {
        subTaskService.updateSubTaskStatus(id, status);
        return new ResponseEntity<>("Status Updated Successfully", HttpStatus.OK);
    }


    @DeleteMapping("/{subTastId}")
    public ResponseEntity<String> deleteSubTask(@PathVariable Long subTastId) {
        subTaskService.deleteSubTask(subTastId);
        return ResponseEntity.status(HttpStatus.OK).body("Subtask Deleted Successfully");
    }

    @GetMapping
    public ResponseEntity<List<SubTask>> getAllSubTask() {
        List<SubTask> allSubTasks =  subTaskService.getAllSubTask();
        return ResponseEntity.status(HttpStatus.OK).body(allSubTasks);
    }

    @GetMapping("/{task_id}")
    public ResponseEntity<List<SubTask>> getAllSubTaskLinkedWithTaskId(@PathVariable Long task_id) {
        List<SubTask> allSubTasks =  subTaskService.getAllSubTaskLinkedWithTaskId(task_id);
        return ResponseEntity.status(HttpStatus.OK).body(allSubTasks);
    }
}
