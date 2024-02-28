package com.bandi.mhProject.entity;

import com.bandi.mhProject.repository.TimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class ManageKey extends TimeEntity {
    @Id
    private String authKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Column(columnDefinition = "VARCHAR(1) DEFAULT 'y'")
    private String useYn = "y";

    @Builder
    public ManageKey(String authKey, User user) {
        this.authKey = authKey;
        this.user = user;
    }
}
