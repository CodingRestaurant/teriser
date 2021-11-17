/*
 * Author: Seokjin Yoon, Hyeokwoo Kwon
 * Filename: ProjectService.java
 * Desc:
 */

package com.codrest.teriser.projects;

import com.codrest.teriser.connections.ConnectionServerInfo;
import com.codrest.teriser.developers.Developer;
import com.codrest.teriser.errors.NotFoundException;
import com.codrest.teriser.errors.UnauthorizedException;
import com.codrest.teriser.projects.tokens.ProjectTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectTokenService projectTokenService;
    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public Optional<Project> findById(Long projectId) {
        checkNotNull(projectId, "projectId must be provided.");

        return projectRepository.findById(projectId);
    }

    @Transactional(readOnly = true)
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Project> findByName(String projectName) {
        checkNotNull(projectName, "projectName must be provided.");

        return projectRepository.findByName(projectName);
    }

    @Transactional
    public Project addProject(Developer developer, String title) {
        checkNotNull(developer, "developer must be provided.");
        checkNotNull(title, "title must be provided.");

        Project project = new Project();
        project.setOwner(developer);
        project.setTitle(title);
        project.setName(project.generateName());
        project.setPassword(null); //TODO: password 외부에서 받아 오기
        project.setClientToken(projectTokenService.issue());
        project.setUnpaidCallCount(0L);

        return projectRepository.save(project);
    }

    @Transactional
    public Optional<Project> editProject(Long projectId, Developer developer, String title) {
        Optional<Project> optionalProject = findById(projectId);
        if (optionalProject.isPresent()) {
            return editProject(optionalProject.get(), developer, title);
        }
        return optionalProject;
    }

    @Transactional
    public Optional<Project> editProject(Project project, Developer developer, String title) {
        checkNotNull(project, "project must be provided.");
        checkNotNull(developer, "developer must be provided.");
        checkNotNull(title, "title must be provided.");

        if (!project.getOwner().equals(developer)) {
            return Optional.empty();
        }

        project.setTitle(title);
        return Optional.of(projectRepository.save(project));
    }

    @Transactional
    public void removeProject(Long projectId, Developer developer) {
        findById(projectId).ifPresent(project -> removeProject(project, developer));
    }

    @Transactional
    public void removeProject(String projectName, Developer developer) {
        findByName(projectName).ifPresent(project -> removeProject(project, developer));
    }

    @Transactional
    public void removeProject(Project project, Developer developer) {
        checkNotNull(project, "project must be provided.");
        checkNotNull(developer, "developer must be provided.");

        if (!project.getOwner().equals(developer)) {
            return;
        }

        projectTokenService.expireBy(project.getClientToken());
        projectRepository.delete(project);
    }

    @Transactional
    public void update(Project project) {
        checkNotNull(project, "project must be provided.");
        projectRepository.save(project);
    }

    @Transactional(readOnly = true)
    public Project findByToken(String token) {
        return projectRepository.findByClientToken(token)
                .orElseThrow(() -> new UnauthorizedException("Invalid token."));
    }

    @Transactional
    public void updateToken(Project project) {
        String oldToken = project.getClientToken();
        String newToken = projectTokenService.issue();
        projectTokenService.expireBy(oldToken);
        project.setClientToken(newToken);
        projectRepository.save(project);
    }

    //TODO : Docs, 명시적으로 project name을 리턴한다고 선언할것.
    @Transactional
    public String setConnectionServer(String clientToken, String serverToken) {
        ConnectionServerInfo serverInfo = ConnectionServerInfo.instance;
        String csName = serverInfo.getServerNameByToken(serverToken);
        Project project = findByToken(clientToken);

        serverInfo.increaseConnection(serverToken);
        project.setConnectionServerName(csName);
        project.setConnectionServerConnected(true);
        update(project);

        return project.getName();
    }

    @Transactional
    public String removeConnectionServer(String projectName, String serverToken) {
        Project project = findByName(projectName).orElseThrow(() -> new NotFoundException("Wrong project name: " + projectName));
        project.setConnectionServerConnected(false);
        update(project);
        ConnectionServerInfo.instance.decreaseConnection(serverToken);
        return project.getName();
    }
}
