package com.ynet.imis.service.org;

import java.util.List;

import javax.transaction.Transactional;

import com.ynet.imis.domain.org.Custom;
import com.ynet.imis.repository.org.CustomRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class CustomServiceImpl implements CustomService {

    @Autowired
    private CustomRepository customDao;


    @Override
    public Custom addCustom(Custom custom) {
        return customDao.save(custom);
    }

    @Override
    public Custom getCustomById(Long id) {
        return customDao.findById(id).get();
    }

    @Override
    public int deleteCustomById(Long id) {
        customDao.deleteById(id);
        return 1;
    }

    @Override
    public List<Custom> getCustomTree() {
		return customDao.getCustomTree();
	}

}