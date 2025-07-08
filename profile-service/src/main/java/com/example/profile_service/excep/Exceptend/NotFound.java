package com.example.profile_service.excep.Exceptend;

import com.example.profile_service.excep.BaseException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotFound extends BaseException {
    public NotFound(String message) {
        super(404, message + " not found ");
    }
}
