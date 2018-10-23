package com.ynet.imis.service.org;

import java.util.List;

import com.ynet.imis.domain.org.Custom;

public interface CustomService {


    public Custom addCustom(Custom custom );
    public Custom getCustomById(Long id);
    public int deleteCustomById(Long id);
    
    public List<Custom> getCustomTree();

}