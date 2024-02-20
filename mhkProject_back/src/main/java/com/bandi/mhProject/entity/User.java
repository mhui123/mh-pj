package com.bandi.mhProject.entity;

import com.bandi.mhProject.repository.BaseEntity;
import com.bandi.mhProject.repository.BaseTimeEntity;
import com.bandi.mhProject.repository.TimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@ToString(of={"id", "pw"})
public class User extends TimeEntity {
    @Id @Column(name="user_id")
    private String id;
    @Column(name="user_pw")
    private String pw;
    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'ROLE_USER'")
    private String role;

    @OneToMany(mappedBy = "user")
    private List<Info> infos = new ArrayList<>();

    public User(String id, String pw) {
        this.id = id;
        this.pw = pw;
    }
    @Builder
    public User(String id, String pw, String role) {
        this.id = id;
        this.pw = pw;
        this.role = role;
    }
}
