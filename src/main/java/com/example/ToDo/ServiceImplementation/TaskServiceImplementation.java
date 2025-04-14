package com.example.ToDo.ServiceImplementation;

import com.example.ToDo.Converter.TaskConverter;
import com.example.ToDo.DTO.TaskRequest;
import com.example.ToDo.Exception.TaskNotFoundException;
import com.example.ToDo.Model.Task;
import com.example.ToDo.Repository.TaskRepository;
import com.example.ToDo.ServiceInterface.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImplementation implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImplementation(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public ResponseEntity<String> createTask(TaskRequest taskRequest) {
        if(taskRequest == null) throw new RuntimeException("TaskRequest is null");
        if(taskRequest.getTask() == null || taskRequest.getTask().isEmpty()) return new ResponseEntity<>("Task is null", HttpStatus.BAD_REQUEST);
        if(taskRequest.getUrgency() == null || taskRequest.getUrgency().isEmpty()) return new ResponseEntity<>("Urgency is null", HttpStatus.BAD_REQUEST);
        if(taskRequest.getOperation() == null || taskRequest.getOperation().isEmpty()) return new ResponseEntity<>("Operation is null", HttpStatus.BAD_REQUEST);
        Task task = TaskConverter.TaskRequestToTask(taskRequest);
        taskRepository.save(task);
        return new ResponseEntity<>("Task created", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Task> getTask(Long id) throws TaskNotFoundException {
        try{
            Task task = taskRepository.findById(id).get();
            if(task == null) throw new TaskNotFoundException("Task not found with id: " + id);
            return new ResponseEntity<>(task, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<Task> updateTask(Long id, TaskRequest taskRequest) throws TaskNotFoundException {
        try{
            Task task = taskRepository.findById(id).get();
            if(task == null) throw new TaskNotFoundException("Task not found with id: " + id);
            task.setTask(taskRequest.getTask());
            task.setUrgency(taskRequest.getUrgency());
            task.setOperation(taskRequest.getOperation());
            taskRepository.save(task);
            return new ResponseEntity<>(task, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteTask(Long id) throws TaskNotFoundException {
        try{
            Task task = taskRepository.findById(id).get();
            if(task == null) throw new TaskNotFoundException("Task not found with id: " + id);
            taskRepository.delete(task);
            return new ResponseEntity<>("Task deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
