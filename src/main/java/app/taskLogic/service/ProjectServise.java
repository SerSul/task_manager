package app.taskLogic.service;

import app.taskLogic.models.Project;
import app.taskLogic.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProjectServise {
    @Autowired
    private ProjectRepository projectRepository;
    public Project createProject(Project project) { return projectRepository.save(project); }


    public List<Project> getAllProjects() { return projectRepository.findAll(); }

    public Optional<Project> getProjectById(Long projectId) { return projectRepository.findById(projectId); }

    public void deleteProject(Long projectId) { projectRepository.deleteById(projectId); }
}
