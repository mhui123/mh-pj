package com.bandi.mhProject.repository.custom;

import com.bandi.mhProject.dto.UserDto;

import java.util.List;

public interface UserCustomRepo {
    List<UserDto> findUserList();
}
