package com.ynet.imis.service.security;

import java.util.Date;
import java.util.List;

import com.ynet.imis.domain.security.PersistentLogins;
import com.ynet.imis.repository.security.PersistentLoginsRepository;
import com.ynet.imis.utils.ImisUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class PersistentLoginsService implements PersistentTokenRepository {

    @Autowired
    private PersistentLoginsRepository loginsDao;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void createNewToken(PersistentRememberMeToken token) {

        PersistentLogins login = new PersistentLogins();
        login.setUsername(token.getUsername());
        login.setSeries(token.getSeries());
        login.setToken(token.getTokenValue());
        login.setLastUsed(token.getDate());

        logger.info("Add new persistent login: " + ImisUtils.objectJsonStr(login));
        login = loginsDao.save(login);

    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {

        PersistentLogins login = loginsDao.findBySeries(series);
        login.setToken(tokenValue);
        login.setLastUsed(lastUsed);
        loginsDao.save(login);
        logger.info("update persistent login: " + ImisUtils.objectJsonStr(login));
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        logger.info("get user persistent login:" + seriesId);

        PersistentLogins login = loginsDao.findBySeries(seriesId);
        PersistentRememberMeToken token = new PersistentRememberMeToken(login.getUsername(), login.getSeries(),
                login.getToken(), login.getLastUsed());
        logger.info(ImisUtils.objectJsonStr(login));

        return token;
    }

    @Override
    public void removeUserTokens(String username) {
        logger.info("remove user persitent logins:" + username);
        List<PersistentLogins> logins = loginsDao.foundAllByUsername(username);
        loginsDao.deleteAll(logins);
        // loginsDao.removeUserPersistents(username);
    }

}