/*
 * Author: Seokjin Yoon, Hyeokwoo Kwon
 * Filename: ProjectDto.java
 * Desc:
 */

package com.codrest.teriser.projects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static org.springframework.beans.BeanUtils.copyProperties;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class ProjectDto {
    private Long seq;
    private String title;
    private String name;
    private String clientToken;

    public ProjectDto(Project source) {
        copyProperties(source, this);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("seq", seq)
                .append("title", title)
                .append("name", name)
                .append("clientToken", clientToken)
                .toString();
    }
}
