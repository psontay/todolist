package com.sontaypham.todolist.configuration;

import com.sontaypham.todolist.entities.EmailDetails;
import com.sontaypham.todolist.entities.Task;
import com.sontaypham.todolist.entities.User;
import com.sontaypham.todolist.enums.TaskStatus;
import com.sontaypham.todolist.repository.TaskRepository;
import com.sontaypham.todolist.service.EmailService;
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
    EmailService emailService;

    @Scheduled(fixedRate = 7 * 24 * 60 * 60 * 1000)
    public void taskStatistics() {
        List<Task> allTasks = taskRepository.findAll();
        log.info("Found {} tasks in total", allTasks.size());

        Map<User, List<Task>> tasksByUser = allTasks.stream()
                                                    .filter(task -> task.getUser() != null && task.getUser().getEmail() != null)
                                                    .collect(Collectors.groupingBy(Task::getUser));
        log.info("Grouped tasks into {} users", tasksByUser.size());

        for (Map.Entry<User, List<Task>> entry : tasksByUser.entrySet()) {
            User user = entry.getKey();
            List<Task> tasks = entry.getValue();
            log.info("User: {}, Email: {}, Number of tasks: {}",
                     user.getUsername(), user.getEmail(), tasks.size());

            tasks.forEach(task -> log.info("Task ID: {}, Title: {}, User: {}",
                                           task.getId(), task.getTitle(), user.getEmail()));

            long total = tasks.size();
            long completed = tasks.stream().filter(t -> t.getStatus() == TaskStatus.COMPLETED).count();
            long pending = tasks.stream().filter(t -> t.getStatus() == TaskStatus.PENDING).count();
            List<Task> pendingTasks = tasks.stream().filter(t -> t.getStatus() == TaskStatus.PENDING).toList();
            emailService.sendTemplateHtmlEmail(
                    EmailDetails.builder()
                                .subject("📊 Your Weekly Task Summary")
                                .to(user.getEmail())
                                .templateName("task-statistics")
                                .variables(Map.of(
                                        "total", total,
                                        "completed", completed,
                                        "pending", pending,
                                        "username", user.getUsername(),
                                        "pendingTasks", pendingTasks
                                                 ))
                                .build());
            log.info("Sent weekly task summary to user: {}", user.getEmail());
        }
    }
}
