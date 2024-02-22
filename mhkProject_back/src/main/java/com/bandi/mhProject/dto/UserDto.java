package com.bandi.mhProject.dto;

import com.bandi.mhProject.entity.Info;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class UserDto {
    private String id;
    private String role;
    private List<Info> infos = new ArrayList<>();
    private boolean authorized = false;
    @Builder
    public UserDto(String id, String role, List<Info> infos, boolean authorized) {
        this.id = id;
        this.role = role;
        this.infos = infos;
        this.authorized = authorized;
    }
}
