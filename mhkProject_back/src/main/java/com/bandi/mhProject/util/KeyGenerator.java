package com.bandi.mhProject.util;

import com.bandi.mhProject.constants.SystemConfigs;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class KeyGenerator implements SystemConfigs {
    public static String generateKey(){
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0,6);
    }

    public static boolean validateKey(String inputKey, String storedKey){
        return inputKey.equals(storedKey);
    }

    public static boolean isInTimeAuthenticated(LocalDateTime keyGenTime, LocalDateTime authTime){
        long betweenMin = Duration.between(keyGenTime, authTime).toMinutes();
        return AUTH_DURATION - betweenMin > 0;
    }


}
