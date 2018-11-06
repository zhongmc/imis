package com.ynet.imis.service.utils;

import org.springframework.web.multipart.MultipartFile;

public interface InitService {

    public boolean isSystemInitialized();

    public int doInitialize(String userName, String password, MultipartFile file);

}
