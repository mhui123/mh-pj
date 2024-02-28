package com.bandi.mhProject.constants;

import lombok.Getter;

public interface SystemConfigs {
    public final Long AUTH_DURATION = 1000 * 60 * 10L;
    public final String CODE_200 = "정상동작";

    public final String CODE_900 = "에러발생";
    public final String CODE_901 = "유저를 찾을 수 없습니다.";
    public final String CODE_902 = "기존 비밀번호가 다릅니다.";
    public final String CODE_903 = "기존 아이디가 존재합니다.";
    public final String CODE_904 = "비밀번호 초기화 실패한 계정이 존재합니다.";
    public final String CODE_905 = "해당 게시글을 찾을 수 없습니다.";
    public final String CODE_906 = "사용중지된 계정입니다.";
    public final String CODE_907 = "비밀번호를 확인해주세요.";
    public final String CODE_908 = "아직 유효한 인증키가 존재합니다.";
}
