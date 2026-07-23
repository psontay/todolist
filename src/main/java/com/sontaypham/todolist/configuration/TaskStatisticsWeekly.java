package com.sontaypham.todolist.configuration;

import com.sontaypham.todolist.dto.request.NotificationRequest;
import com.sontaypham.todolist.entities.Task;
import com.sontaypham.todolist.entities.User;
import com.sontaypham.todolist.enums.NotificationChannel;
import com.sontaypham.todolist.enums.TaskStatus;
import com.sontaypham.todolist.notification.NotificationFactory;
import com.sontaypham.todolist.repository.TaskRepository;
import com.sontaypham.todolist.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,
        makeFinal = true)
public class TaskStatisticsWeekly {

    TaskRepository taskRepository;
    NotificationFactory notificationFactory;
    UserRepository userRepository;

    @Scheduled(cron = "0 0 0 * * SUN")
    public void taskStatistics() {
        log.info("Starting weekly task statistics batch job...");

        int pageSize = 100;
        int pageNumber = 0;

        while (true) {
            Page<User> userPage = userRepository.findAll(PageRequest.of(pageNumber, pageSize));
            if (userPage.isEmpty()) {
                break;
            }

            for (User user : userPage.getContent()) {
                if (user.getEmail() == null) {
                    continue;
                }

                long total = taskRepository.countByUserId(user.getId());
                long completed = taskRepository.countByUserIdAndStatus(user.getId(), TaskStatus.COMPLETED);
                long pending = taskRepository.countByUserIdAndStatus(user.getId(), TaskStatus.PENDING);

                List<Task> pendingTasks = taskRepository.findByUserIdAndStatus(user.getId(), TaskStatus.PENDING);

                NotificationRequest request = NotificationRequest.builder()
                                                                 .subject("Your Weekly Task Summary")
                                                                 .to(user.getEmail())
                                                                 .templateName("task-statistics")
                                                                 .variables(Map.of(
                                                                         "total", total,
                                                                         "completed", completed,
                                                                         "pending", pending,
                                                                         "username", user.getUsername(),
                                                                         "pendingTasks", pendingTasks
                                                                                  ))
                                                                 .build();
                notificationFactory.sendNotification(NotificationChannel.EMAIL, request);
            }
            pageNumber++;
        }
        log.info("Weekly batch job completed successfully.");
    }

}
