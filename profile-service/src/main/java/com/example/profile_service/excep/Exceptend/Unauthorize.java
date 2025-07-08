package com.example.profile_service.excep.Exceptend;

import org.springframework.http.HttpStatus;

import com.example.profile_service.excep.BaseException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Unauthorize extends BaseException {
    public Unauthorize() {
        super(HttpStatus.UNAUTHORIZED.value(), "You do not have parmission to do this");
    }
}
