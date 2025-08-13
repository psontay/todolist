package com.sontaypham.todolist.configuration;

import com.sontaypham.todolist.entities.EmailDetails;
import com.sontaypham.todolist.entities.Task;
import com.sontaypham.todolist.repository.EmailRepository;
import com.sontaypham.todolist.repository.TaskRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskDeadlineNotifier {
  TaskRepository taskRepository;
  EmailRepository emailRepository;

  @Scheduled(fixedRate = 7 * 24 * 60 * 60 * 1000)
  public void checkAndSendDeadlineWarnings() {
    LocalDateTime now = LocalDateTime.now();
    log.info("Running TaskDeadlineNotifier at {}", now);
    List<Task> tasks = taskRepository.findAll();
    log.info("Found {} tasks to evaluate", tasks.size());

    for (Task task : tasks) {
      if (task.getDeadline() == null || task.getCreatedAt() == null) {
        log.warn("Skipping task {} due to null createdAt or deadline", task.getId());
        continue;
      }

      Duration total = Duration.between(task.getCreatedAt(), task.getDeadline());
      Duration threshold = total.multipliedBy(4).dividedBy(5);
      LocalDateTime sendTime = task.getCreatedAt().plus(threshold);
      log.info(
          "Task {} - now: {}, sendTime: {}, warningEmailSent: {}",
          task.getId(),
          now,
          sendTime,
          task.getWarningEmailSent());
      if (now.isAfter(sendTime)
          && now.isBefore(task.getDeadline())
          && Boolean.FALSE.equals(task.getWarningEmailSent())) {
        log.info("Sending warning email for task: {}", task.getId());
        emailRepository.sendSimpleMail(
            EmailDetails.builder()
                .to(task.getUser().getEmail())
                .subject("⏰ Task approaching deadline!")
                .messageBody("Task \"" + task.getTitle() + "\" is nearing its deadline.")
                .build());
        task.setWarningEmailSent(true);
        taskRepository.save(task);
        log.info("Email sent and task {} updated with warningEmailSent = true", task.getId());
      }
    }
  }
}
