/*
 * DeveloperDto.java
 * Author : 박찬형
 * Created Date : 2021-08-01
 */
package com.codrest.teriser.developers;

import com.codrest.teriser.projects.ProjectDto;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class DeveloperDto {
    private final String name;
    private final Email email;
    private final List<ProjectDto> projects;

    public DeveloperDto(Developer developer){
        name = developer.getName();
        email = developer.getEmail();
        projects = developer.getProjects().stream().map(ProjectDto::new).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("name", name)
                .append("email", email.toString())
                .append("projects", projects.stream()
                        .map(ProjectDto::toString)
                        .collect(Collectors.toList()))
                .toString();
    }
}
