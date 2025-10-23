package com.eshop.basketservice.Service.Impl;

import com.eshop.basketservice.Service.IIdentityService;
import org.springframework.stereotype.Service;

@Service
public class IdentityService implements IIdentityService {

    private static final String USER_ID = "user123";


    @Override
    public String getUserIdentity(){
        return  USER_ID;
    }
}
