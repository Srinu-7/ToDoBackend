package com.example.ToDo.ServiceInterface;

import com.example.ToDo.DTO.TaskRequest;
import com.example.ToDo.Exception.TaskNotFoundException;
import com.example.ToDo.Model.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService {
    ResponseEntity<String> createTask(TaskRequest taskRequest);
    ResponseEntity<Task> getTask(Long id) throws TaskNotFoundException;
    ResponseEntity<Task> updateTask(Long id, TaskRequest taskRequest) throws TaskNotFoundException;
    ResponseEntity<List<Task>> getAllTasks();
    ResponseEntity<String> deleteTask(Long id) throws TaskNotFoundException;
}
