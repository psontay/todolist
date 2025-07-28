package com.sontaypham.todolist.Configuration;

import com.sontaypham.todolist.Entities.EmailDetails;
import com.sontaypham.todolist.Entities.Task;
import com.sontaypham.todolist.Entities.User;
import com.sontaypham.todolist.Enums.TaskStatus;
import com.sontaypham.todolist.Repository.EmailRepository;
import com.sontaypham.todolist.Repository.TaskRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskStatisticsWeekly {
  TaskRepository taskRepository;
  EmailRepository emailRepository;

  @Scheduled(fixedRate = 7 * 24 * 60 * 60 * 1000)
  public void taskStatistics() {
    List<Task> allTasks = taskRepository.findAll();
    Map<User, List<Task>> tasksByUser =
        allTasks.stream()
            .filter(task -> task.getUser() != null && task.getUser().getEmail() != null)
            .collect(Collectors.groupingBy(Task::getUser));
    for (Map.Entry<User, List<Task>> entry : tasksByUser.entrySet()) {
      User user = entry.getKey();
      List<Task> tasks = entry.getValue();
      long total = tasks.size();
      long completed = tasks.stream().filter(t -> t.getStatus() == TaskStatus.COMPLETED).count();
      long pending = tasks.stream().filter(t -> t.getStatus() == TaskStatus.PENDING).count();
      String report =
          """
                📝 Weekly Task Summary:
                - Total tasks: %d
                - Completed: %d
                - Pending: %d
                """
              .formatted(total, completed, pending);
      emailRepository.sendSimpleMail(
          EmailDetails.builder()
              .subject("\uD83D\uDCCA Your Weekly Task Summary")
              .to(user.getEmail())
              .messageBody(report)
              .build());
      log.info("Sent weekly task summary to user: {}", user.getEmail());
    }
  }
}
