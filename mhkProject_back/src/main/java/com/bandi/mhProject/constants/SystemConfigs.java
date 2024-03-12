package com.bandi.mhProject.constants;

import lombok.Getter;

public interface SystemConfigs {
    //pagination
    Long PAGE_SIZE = 20L;
    Long AUTH_DURATION = 10L;
    String CODE_200 = "정상동작";
    String CODE_201 = "인증 성공";
    String CODE_202 = "비밀번호 변경완료";
    String CODE_203 = "로그인 성공";
    String CODE_204 = "회원가입 완료";
    String CODE_205 = "권한변경 완료";
    String CODE_206 = "사용여부변경 완료";
    String CODE_207 = "비밀번호 초기화 완료";
    String CODE_208 = "작성 완료";
    String CODE_209 = "삭제 완료";

    String CODE_900 = "에러발생";
    String CODE_901 = "유저를 찾을 수 없습니다.";
    String CODE_902 = "기존 비밀번호가 다릅니다.";
    String CODE_903 = "기존 아이디가 존재합니다.";
    String CODE_904 = "비밀번호 초기화 실패한 계정이 존재합니다.";
    String CODE_905 = "해당 게시글을 찾을 수 없습니다.";
    String CODE_906 = "사용중지된 계정입니다.";
    String CODE_907 = "비밀번호를 확인해주세요.";
    String CODE_908 = "아직 유효한 인증키가 존재합니다.";
    String CODE_909 = "파라미터가 올바르지 않습니다.";
    String CODE_910 = "인증키값이 일치하지 않습니다.";
    String CODE_911 = "인증키의 유효시간이 경과하였습니다.";

    String CODE_912 = "인증과정에 오류가 발생하였습니다. 처음부터 다시 진행해주세요.";
}
