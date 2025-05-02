package com.example.ToDo.Controller;

import com.example.ToDo.DTO.TaskRequest;
import com.example.ToDo.DTO.TaskResponse;
import com.example.ToDo.Exception.TaskNotFoundException;
import com.example.ToDo.Model.Task;
import com.example.ToDo.ServiceInterface.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/")
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest taskRequest) {
        return taskService.createTask(taskRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) throws TaskNotFoundException {
        return taskService.getTask(id);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody TaskRequest taskRequest) throws TaskNotFoundException {
        return taskService.updateTask(id, taskRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) throws TaskNotFoundException {
        return taskService.deleteTask(id);
    }
}
