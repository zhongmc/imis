package com.ynet.imis.service.utils;

import org.springframework.web.multipart.MultipartFile;

public interface InitService {

    public int doInitialize(String userName, String password, MultipartFile file);

}
