package com.project.corp.service;

import com.project.corp.model.Team;
import com.project.corp.repository.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    private static final Logger log = LoggerFactory.getLogger(TeamService.class);

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    public Optional<Team> findById(Long id) {
        return teamRepository.findById(id);
    }

    @Transactional
    public Team create(Team team) {
        validateTeam(team);
        team.setCreatedAt(LocalDateTime.now());
        team.setUpdatedAt(LocalDateTime.now());
        log.info("Creating team: {}", team.getName());
        return teamRepository.save(team);
    }

    @Transactional
    public Optional<Team> update(Long id, Team updated) {
        return teamRepository.findById(id).map(existing -> {
            validateTeam(updated);
            existing.setName(updated.getName());
            existing.setDescription(updated.getDescription());
            existing.setUpdatedAt(LocalDateTime.now());
            log.info("Updating team id: {}", id);
            return teamRepository.save(existing);
        });
    }

    @Transactional
    public boolean delete(Long id) {
        return teamRepository.findById(id).map(t -> {
            teamRepository.delete(t);
            log.info("Deleting team id: {}", id);
            return true;
        }).orElse(false);
    }

    private void validateTeam(Team team) {
        if (team.getName() == null || team.getName().isBlank()) {
            throw new IllegalArgumentException("Team name cannot be empty");
        }
        if (team.getName().length() > 100) {
            throw new IllegalArgumentException("Team name must not exceed 100 characters");
        }
    }
}
