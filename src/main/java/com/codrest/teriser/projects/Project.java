/*
 * Author : 나상혁 : Kasania, Seokjin Yoon, Hyeokwoo Kwon
 * Filename : Project
 * Desc :
 */

package com.codrest.teriser.projects;

import com.codrest.teriser.developers.Developer;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "projects", indexes = @Index(name = "idx_name", columnList = "name"))
public class Project {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long seq;

    @JoinColumn(name = "owner_id")
    @ManyToOne
    private Developer owner;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)
    private String name;
    private String password;

    private Long unpaidCallCount;

    private String clientToken;

    private String connectionServerName;
    private Boolean connectionServerConnected;

    public String generateName() {
        return Objects.nonNull(title) ? title.toLowerCase().replaceAll("-", " ") + "-" + Objects.hash(seq) : name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (Objects.isNull(o) || getClass() != o.getClass())
            return false;

        Project project = (Project) o;
        return Objects.equals(seq, project.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("seq", seq)
                .append("owner", owner)
                .append("title", title)
                .append("name", name)
                .append("unpaidCallCount", unpaidCallCount)
                .append("clientToken", clientToken)
                .append("connectionServerName", connectionServerName)
                .append("connectionServerConnected", connectionServerConnected)
                .toString();
    }
}
