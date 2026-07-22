package com.sontaypham.todolist.nofitication;

import com.sontaypham.todolist.dto.request.NotificationRequest;
import com.sontaypham.todolist.enums.NotificationChannel;

public interface NotificationHandler {

    NotificationChannel getChannel();

    void send(NotificationRequest request);

}


