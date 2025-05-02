package com.example.ToDo.Converter;

import com.example.ToDo.DTO.TaskRequest;
import com.example.ToDo.DTO.TaskResponse;
import com.example.ToDo.Model.Task;

public class TaskConverter {

    public static Task TaskRequestToTask(TaskRequest taskRequest) {
        return Task.
                builder().
                operation(taskRequest.getOperation()).
                task(taskRequest.getTask()).
                urgency(taskRequest.getUrgency()).
                dateTime(taskRequest.getDateTime()).
                build();
    }

    public static TaskResponse TaskToTaskResponse(Task task) {
        return TaskResponse.
                builder().
                operation(task.getOperation()).
                task(task.getTask()).
                urgency(task.getUrgency()).
                dateTime(task.getDateTime()).
                build();
    }
}
