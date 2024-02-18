package com.bandi.mhProject.entity;

import com.bandi.mhProject.repository.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Info extends BaseEntity {
    @Id @GeneratedValue
    @Column(name="info_id")
    private Long id;
    private String infokey;
    private String description;
//    private String catg;
//    private String link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    public Info(String key, String description, User user) {
        this.infokey = key;
        this.description = description;
        this.user = user;
    }
}
