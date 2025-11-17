package com.project.corp.service;

import com.project.corp.model.Task;
import com.project.corp.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public List<Task> findByCompleted(boolean completed) {
        return taskRepository.findAll().stream()
                .filter(t -> t.isCompleted() == completed)
                .toList();
    }

    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    @Transactional
    public Task create(Task task) {
        validateTask(task);
        log.info("Creating task: {}", task.getTitle());
        return taskRepository.save(task);
    }

    @Transactional
    public Optional<Task> update(Long id, Task updated) {
        return taskRepository.findById(id).map(existing -> {
            validateTask(updated);
            existing.setTitle(updated.getTitle());
            existing.setDescription(updated.getDescription());
            existing.setCompleted(updated.isCompleted());
            log.info("Updating task id: {}", id);
            return taskRepository.save(existing);
        });
    }

    @Transactional
    public boolean delete(Long id) {
        return taskRepository.findById(id).map(t -> {
            taskRepository.delete(t);
            log.info("Deleting task id: {}", id);
            return true;
        }).orElse(false);
    }

    @Transactional
    public Optional<Task> markCompleted(Long id) {
        return taskRepository.findById(id).map(task -> {
            task.setCompleted(true);
            log.info("Task {} marked as completed", id);
            return taskRepository.save(task);
        });
    }

    @Transactional
    public Optional<Task> markPending(Long id) {
        return taskRepository.findById(id).map(task -> {
            task.setCompleted(false);
            log.info("Task {} marked as pending", id);
            return taskRepository.save(task);
        });
    }

    private void validateTask(Task task) {
        if (task.getTitle() == null || task.getTitle().isBlank()) {
            throw new IllegalArgumentException("Task title cannot be empty");
        }
        if (task.getTitle().length() > 255) {
            throw new IllegalArgumentException("Task title must not exceed 255 characters");
        }
    }
}
