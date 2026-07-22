package com.sontaypham.todolist.dto.event;

import com.sontaypham.todolist.entities.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserCreatedEvent extends ApplicationEvent {

    private final User user;

    public UserCreatedEvent(Object source, User user) {
        super(source);
        this.user = user;
    }

}
