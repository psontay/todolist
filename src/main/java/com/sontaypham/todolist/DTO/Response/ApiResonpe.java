package com.sontaypham.todolist.DTO.Response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResonpe<T> {
    int status; // 1 for success 0 for fail
    String message;
    T data;
}
