package com.project.corp.service;

import com.project.corp.model.Project;
import com.project.corp.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private static final Logger log = LoggerFactory.getLogger(ProjectService.class);

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }

    @Transactional
    public Project create(Project project) {
        validateProject(project);
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());
        log.info("Creating project: {}", project.getName());
        return projectRepository.save(project);
    }

    @Transactional
    public Optional<Project> update(Long id, Project updated) {
        return projectRepository.findById(id).map(existing -> {
            validateProject(updated);
            existing.setName(updated.getName());
            existing.setDescription(updated.getDescription());
            existing.setUpdatedAt(LocalDateTime.now());
            log.info("Updating project id: {}", id);
            return projectRepository.save(existing);
        });
    }

    @Transactional
    public boolean delete(Long id) {
        return projectRepository.findById(id).map(p -> {
            projectRepository.delete(p);
            log.info("Deleting project id: {}", id);
            return true;
        }).orElse(false);
    }

    private void validateProject(Project project) {
        if (project.getName() == null || project.getName().isBlank()) {
            throw new IllegalArgumentException("Project name cannot be empty");
        }
        if (project.getName().length() > 100) {
            throw new IllegalArgumentException("Project name must not exceed 100 characters");
        }
    }
}
