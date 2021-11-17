/*
 * Author : Hyeokwoo Kwon
 * Filename : ProjectToken.java
 * Desc :
 */

package com.codrest.teriser.projects.tokens;

import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@TableGenerator(name = "PROJECT_TOKEN_GENERATOR", table = "project_token_seq", allocationSize = 1000)
@Table(name = "project_tokens")
public class ProjectToken {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PROJECT_TOKEN_GENERATOR")
    private Long seq;

    private String token;

    private LocalDateTime createdDate;
    private LocalDateTime issuedDate;
    private LocalDateTime expiredDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (Objects.isNull(o) || getClass() != o.getClass()) return false;
        ProjectToken projectToken = (ProjectToken) o;
        return seq.equals(projectToken.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("seq", seq)
                .append("token", token)
                .append("createdDate", createdDate)
                .append("issuedDate", issuedDate)
                .append("expiredDate", expiredDate)
                .toString();
    }
}
