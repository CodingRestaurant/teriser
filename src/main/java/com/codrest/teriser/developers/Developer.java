/*
 * Author : 나상혁 : Kasania, 박찬형
 * Filename : Developer
 * Desc :
 */
package com.codrest.teriser.developers;

import com.codrest.teriser.projects.Project;
import com.codrest.teriser.security.Jwt;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "developers")
@EntityListeners(AuditingEntityListener.class)
public class Developer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @Column(name = "name")
    private String name;

    @Column(name = "email", unique = true)
    @Convert(converter = EmailConverter.class)
    private Email email;

    @Column(name = "login_token")
    private String loginToken;

    @CreatedDate
    @Column(name = "create_at")
    private LocalDateTime createAt;

    @CreatedDate
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "activate")
    private boolean activate;

    @OneToMany(mappedBy = "owner")
    private List<Project> projects;

    public String newJwt(Jwt jwt, String[] roles) {
        Jwt.Claims claims = Jwt.Claims.of(seq, name, roles);
        return jwt.create(claims);
    }

    public void login(PasswordEncoder passwordEncoder, String credentials) {
        if (!passwordEncoder.matches(credentials, loginToken)) {
            throw new IllegalArgumentException("Bad credential");
        }
    }
}
