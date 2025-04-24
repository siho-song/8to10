package com.eighttoten.common;

import com.eighttoten.exception.BusinessException;
import com.eighttoten.exception.ExceptionCode;
import jakarta.annotation.PostConstruct;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AppConstant {
    public static final LocalTime WORK_START_TIME = LocalTime.of(8, 0);
    public static final LocalTime WORK_END_TIME = LocalTime.of(22, 0);

    @PostConstruct
    void init(){
        if(AppConstant.WORK_START_TIME.isAfter(AppConstant.WORK_END_TIME)){
            throw new BusinessException(ExceptionCode.INTERNAL_SERVER_ERROR);
        }
        if(AppConstant.WORK_END_TIME.equals(AppConstant.WORK_START_TIME)){
            throw new BusinessException(ExceptionCode.INTERNAL_SERVER_ERROR);
        }
    }
}