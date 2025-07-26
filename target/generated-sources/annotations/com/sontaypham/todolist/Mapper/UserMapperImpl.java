package com.sontaypham.todolist.Mapper;

import com.sontaypham.todolist.DTO.Request.UserCreationRequest;
import com.sontaypham.todolist.DTO.Response.TaskResponse;
import com.sontaypham.todolist.DTO.Response.UserResponse;
import com.sontaypham.todolist.Entities.Task;
import com.sontaypham.todolist.Entities.User;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-26T20:35:16+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(UserCreationRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.name( request.getName() );
        user.email( request.getEmail() );
        user.password( request.getPassword() );

        return user.build();
    }

    @Override
    public UserResponse toUserResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.roles( UserMapper.rolesToStrings( user.getRoles() ) );
        userResponse.tasks( taskSetToTaskResponseSet( user.getTasks() ) );
        userResponse.id( user.getId() );
        userResponse.name( user.getName() );
        userResponse.email( user.getEmail() );

        return userResponse.build();
    }

    protected TaskResponse taskToTaskResponse(Task task) {
        if ( task == null ) {
            return null;
        }

        TaskResponse.TaskResponseBuilder taskResponse = TaskResponse.builder();

        taskResponse.id( task.getId() );
        taskResponse.title( task.getTitle() );
        if ( task.getStatus() != null ) {
            taskResponse.status( task.getStatus().name() );
        }
        if ( task.getCreatedAt() != null ) {
            taskResponse.createdAt( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( task.getCreatedAt() ) );
        }

        return taskResponse.build();
    }

    protected Set<TaskResponse> taskSetToTaskResponseSet(Set<Task> set) {
        if ( set == null ) {
            return null;
        }

        Set<TaskResponse> set1 = new LinkedHashSet<TaskResponse>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Task task : set ) {
            set1.add( taskToTaskResponse( task ) );
        }

        return set1;
    }
}
