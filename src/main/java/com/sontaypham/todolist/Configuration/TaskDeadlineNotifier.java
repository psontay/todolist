package com.sontaypham.todolist.Configuration;

import com.sontaypham.todolist.Entities.EmailDetails;
import com.sontaypham.todolist.Entities.Task;
import com.sontaypham.todolist.Repository.EmailRepository;
import com.sontaypham.todolist.Repository.TaskRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class TaskDeadlineNotifier {
    TaskRepository taskRepository;
    EmailRepository emailRepository;
    @Scheduled( fixedRate = 5*60*1000)
    public void checkAndSendDeadlineWarnings() {
        LocalDateTime now = LocalDateTime.now();
        List<Task> tasks = taskRepository.findAll();

        for(Task task : tasks) {
            if(task.getDeadline() == null || task.getCreatedAt() == null) continue;

            Duration total = Duration.between(task.getCreatedAt(), task.getDeadline());
            Duration threshold = total.multipliedBy(4).dividedBy(5);
            LocalDateTime sendTime = task.getCreatedAt().plus(threshold);

            if(now.isAfter(sendTime) && !Boolean.FALSE.equals(task.getWarningEmailSent())) {
                emailRepository.sendSimpleMail(EmailDetails.builder()
                                                           .to(task.getUser().getEmail())
                                                           .subject("⏰ Task approaching deadline!")
                                                           .messageBody(
                                                                   "Task \"" + task.getTitle() + "\" is nearing its deadline.")
                                                           .build()
                                              );

                task.setWarningEmailSent(true);
                taskRepository.save(task);
            }
        }
    }
}
