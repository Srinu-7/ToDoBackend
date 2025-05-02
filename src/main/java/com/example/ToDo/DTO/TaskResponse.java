package com.example.ToDo.DTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TaskResponse {
    Long id;
    String operation;
    String task;
    String urgency;
    String dateTime;
}
