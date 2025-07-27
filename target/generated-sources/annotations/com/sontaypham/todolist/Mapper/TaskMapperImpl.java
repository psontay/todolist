package com.sontaypham.todolist.Mapper;

import com.sontaypham.todolist.DTO.Request.TaskCreationRequest;
import com.sontaypham.todolist.DTO.Request.TaskUpdateRequest;
import com.sontaypham.todolist.DTO.Response.TaskResponse;
import com.sontaypham.todolist.Entities.Task;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-27T10:00:04+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public Task toTask(TaskCreationRequest request) {
        if ( request == null ) {
            return null;
        }

        Task.TaskBuilder task = Task.builder();

        task.title( request.getTitle() );

        return task.build();
    }

    @Override
    public Task toTask(TaskUpdateRequest request) {
        if ( request == null ) {
            return null;
        }

        Task.TaskBuilder task = Task.builder();

        task.id( request.getId() );
        task.title( request.getTitle() );
        task.status( request.getStatus() );

        return task.build();
    }

    @Override
    public TaskResponse toTaskResponse(Task task) {
        if ( task == null ) {
            return null;
        }

        TaskResponse.TaskResponseBuilder taskResponse = TaskResponse.builder();

        taskResponse.status( enumToString( task.getStatus() ) );
        taskResponse.createdAt( dateToString( task.getCreatedAt() ) );
        taskResponse.id( task.getId() );
        taskResponse.title( task.getTitle() );

        return taskResponse.build();
    }
}
