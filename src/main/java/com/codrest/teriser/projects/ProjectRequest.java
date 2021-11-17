/*
 * Author: Seokjin Yoon, Hyeokwoo Kwon
 * Filename: ProjectRequest.java
 * Desc:
 */

package com.codrest.teriser.projects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ProjectRequest {
    @NotBlank(message = "title must be provided.")
    private String title;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("title", title)
                .toString();
    }
}
