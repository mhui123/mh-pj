package com.bandi.mhProject.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class InfoDto {
    private Long id;
    private String infokey;
    private String info_kr;
    private String description;
    private String link;
    private Long totalSearchCnt = 0L;
    private Long todaySearchCnt = 0L;
    private String creator;
    private String updator;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    @Builder
    public InfoDto(Long id, String infokey, String info_kr, String description, String link, Long totalSearchCnt, Long todaySearchCnt, String creator, String updator, LocalDateTime createDate, LocalDateTime updateDate) {
        this.id = id;
        this.infokey = infokey;
        this.info_kr = info_kr;
        this.description = description;
        this.link = link;
        this.totalSearchCnt = totalSearchCnt;
        this.todaySearchCnt = todaySearchCnt;
        this.creator = creator;
        this.updator = updator;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }
}
