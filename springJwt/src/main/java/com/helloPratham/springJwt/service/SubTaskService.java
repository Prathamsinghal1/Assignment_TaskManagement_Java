package com.helloPratham.springJwt.service;

import com.helloPratham.springJwt.entity.SubTask;
import com.helloPratham.springJwt.entity.Task;
import com.helloPratham.springJwt.repository.SubTaskRepository;
import com.helloPratham.springJwt.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class SubTaskService {
    @Autowired
    private SubTaskRepository subTaskRepository;

    @Autowired
    private TaskRepository taskRepository;

    public SubTask createSubTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        SubTask subTask = new SubTask();
        subTask.setCreatedAt(LocalDateTime.now());
        subTask.setUpdatedAt(LocalDateTime.now());

        subTask.setTask(task);
        return subTaskRepository.save(subTask);
    }

    public void updateSubTaskStatus(Long subTaskId, Integer status) {
        SubTask subTask = subTaskRepository.findById(subTaskId)
                .orElseThrow(() -> new RuntimeException("SubTask not found"));
        subTask.setStatus(status);
        subTask.setUpdatedAt(LocalDateTime.now());
        subTaskRepository.save(subTask);
    }


    public void deleteSubTask(Long subTastId) {
        SubTask subTask = subTaskRepository.findById(subTastId)
                .orElseThrow(() -> new RuntimeException("SubTask not found"));
        subTask.setDeletedAt(LocalDateTime.now());
        subTaskRepository.save(subTask);
    }

    public List<SubTask> getAllSubTask() {
        return subTaskRepository.findAll().stream().filter(item->item.getDeletedAt()==null).toList();
    }

    public List<SubTask> getAllSubTaskLinkedWithTaskId(Long taskId) {
        return subTaskRepository.findAll().stream().filter(item->item.getTask().getId()==taskId).toList();
    }
}
