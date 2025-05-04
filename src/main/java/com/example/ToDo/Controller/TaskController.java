package com.example.ToDo.Controller;

import com.example.ToDo.DTO.TaskRequest;
import com.example.ToDo.Exception.TaskNotFoundException;
import com.example.ToDo.Exception.UserNotFoundException;
import com.example.ToDo.Model.Task;
import com.example.ToDo.Model.User;
import com.example.ToDo.Notification.NotificationService;
import com.example.ToDo.Repository.TaskRepository;
import com.example.ToDo.Repository.UserRepository;
import com.example.ToDo.ServiceInterface.TaskService;
import com.example.ToDo.ServiceInterface.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public TaskController(TaskService taskService, UserService userService, TaskRepository taskRepository, UserRepository userRepository, NotificationService notificationService) {
        this.taskService = taskService;
        this.userService = userService;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @PostMapping("/")
    public ResponseEntity<Task> createTask(@RequestBody TaskRequest taskRequest, @RequestHeader("Authorization") String jwt) throws UserNotFoundException {
        User user = userService.findUserProfileByJwt(jwt);

        if (taskRequest == null || taskRequest.getTask() == null || taskRequest.getUrgency() == null || taskRequest.getOperation() == null) {
            throw new IllegalArgumentException("Task, urgency, or operation must not be null or empty");
        }

        Task task = taskService.createTask(taskRequest);
        task.setUser(user);
        task = taskRepository.save(task);

        user.getTasks().add(task);
        userRepository.save(user);

        notificationService.sendEmailNotification(task, user, false);
        notificationService.sendSMSNotification(task, user, false);

        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id, @RequestHeader("Authorization") String jwt) throws TaskNotFoundException, UserNotFoundException {
        User user = userService.findUserProfileByJwt(jwt);
        Task task = taskService.findTask(user.getTasks(), id, true);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTasks(@RequestHeader("Authorization") String jwt) throws UserNotFoundException {
        User user = userService.findUserProfileByJwt(jwt);
        List<Task> tasks = user.getTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody TaskRequest taskRequest, @RequestHeader("Authorization") String jwt) throws TaskNotFoundException, UserNotFoundException {
        User user = userService.findUserProfileByJwt(jwt);
        Task updatedTask = taskService.updateTask(id, taskRequest, user.getTasks());
        notificationService.sendEmailNotification(updatedTask, user, true);
        notificationService.sendSMSNotification(updatedTask, user, true);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id, @RequestHeader("Authorization") String jwt) throws TaskNotFoundException, UserNotFoundException {
        User user = userService.findUserProfileByJwt(jwt);
        Task task = taskService.findTask(user.getTasks(), id, true);

        user.getTasks().remove(task);
        taskService.deleteTask(task);
        userRepository.save(user);

        return new ResponseEntity<>("Task deleted successfully", HttpStatus.OK);
    }
}
