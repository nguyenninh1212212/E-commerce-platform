package com.example.profile_service.excep.Exceptend;

import com.example.profile_service.excep.BaseException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlreadyExist extends BaseException {
    public AlreadyExist(String message) {
        super(409, message + " already exist!!");
    }
}
