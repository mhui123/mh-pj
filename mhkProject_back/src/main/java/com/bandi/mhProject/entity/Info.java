package com.bandi.mhProject.entity;

import com.bandi.mhProject.repository.BaseEntity;
import com.bandi.mhProject.repository.TimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Info extends BaseEntity {
    @Id @GeneratedValue
    @Column(name="info_id")
    private Long id;
    private String infokey;
    private String info_kr;
    @Column(length = 2000)
    private String description;
//    private String catg;
    private String link;
    private Long totalSearchCnt = 0L;
    private Long todaySearchCnt = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Builder
    public Info(String infokey, String infoKr, String description, String link, User user, Long totalCnt, Long todayCnt, String creator, String editor) {
        this.infokey = infokey;
        this.info_kr = infoKr;
        this.description = description;
        this.user = user;
        this.totalSearchCnt = totalCnt == null ? 0L : totalCnt;
        this.todaySearchCnt = todayCnt == null ? 0L : todayCnt;
        this.link = link;
        this.creator = creator;
        this.updator = editor;
    }
}
