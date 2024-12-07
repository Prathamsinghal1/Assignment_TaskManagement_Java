package com.helloPratham.springJwt.repository;

import com.helloPratham.springJwt.entity.SubTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubTaskRepository extends JpaRepository<SubTask, Long> {
    @Query("SELECT st FROM SubTask st WHERE st.deletedAt IS NULL AND st.task.id = :taskId")
    List<SubTask> findByTaskId(@Param("taskId") Long taskId);
}