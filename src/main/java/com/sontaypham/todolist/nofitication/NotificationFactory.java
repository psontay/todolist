package com.sontaypham.todolist.nofitication;

import com.sontaypham.todolist.dto.request.NotificationRequest;
import com.sontaypham.todolist.enums.NotificationChannel;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NotificationFactory {

    private final Map<NotificationChannel, NotificationHandler> handlerMap;

    public NotificationFactory(List<NotificationHandler> handlers) {
        this.handlerMap = handlers.stream()
                                  .collect(Collectors.toMap(NotificationHandler :: getChannel, Function.identity()));
    }

    public void sendNotification(NotificationChannel channel, NotificationRequest request) {
        NotificationHandler handler = handlerMap.get(channel);
        if (handler == null) {
            throw new IllegalArgumentException("No handler registered for notification channel: " + channel);
        }
        handler.send(request);
    }

}
