package com.eshop.basketservice.Service.Impl;

import com.eshop.basketservice.Service.IIdentityService;
import org.springframework.stereotype.Service;

@Service
public class IdentityService implements IIdentityService {

    // TODO: Thay thế bằng logic xác thực thực tế (tạm thời để im)
    private static final String MOCK_USER_ID = "user123";

    @Override
    public String getUserIdentity() {
        return MOCK_USER_ID;
    }
}
