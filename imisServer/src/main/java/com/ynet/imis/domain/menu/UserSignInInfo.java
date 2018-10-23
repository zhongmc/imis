/**
* UserSignInInfo.java
* @author ZHONGMC
* @description 
* @created Wed Sep 12 2018 18:44:07 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Wed Sep 12 2018 18:45:32 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.domain.menu;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ynet.imis.domain.AbstractEntity;


@Entity
@Table(name="USER_SIGN_INFO")
public class UserSignInInfo extends AbstractEntity {
	
   	private static final long serialVersionUID = -4924247541772029857L;

	@ManyToOne
   @JoinColumn(name="USERID", insertable = false, updatable = false)
	private User user;
	

   @Column(name="USERID")
   	private Long userId;
   
	private int failedCount = 0;//失败登录次数
	
	private int successCount = 0;//成功登录次数
	
	private Date lastSignInTime = new Date();//上次成功登录时间
	
	private Date signInTime = new Date();//当前成功登录时间
	
	private Date loginTime = new Date();//当前登录时间，成功登录后再修改该时间
	
	@Column(length=50)
	private String lastIP;
	
	@Column(length=50)
	private String currentIP;

	public UserSignInInfo()
	{
		
	}
	
	public UserSignInInfo(Long id, Long userId, int successCount, int failedCount, String lastIP, String currentIP, Date signInTime, Date lastSignInTime, Date loginTime)
	{
		this.setId( id );
		this.userId = userId;
		this.successCount = successCount;
		this.failedCount = failedCount;
		this.lastIP = lastIP;
		this.currentIP = currentIP;
		this.signInTime = signInTime;
		this.lastSignInTime = lastSignInTime;
		this.loginTime = loginTime;//依赖该时间处理锁定用户
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public int getFailedCount() {
		return failedCount;
	}

	public void setFailedCount(int failedCount) {
		this.failedCount = failedCount;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}

	public Date getLastSignInTime() {
		return lastSignInTime;
	}

	public void setLastSignInTime(Date lastSignInTime) {
		this.lastSignInTime = lastSignInTime;
	}

	public String getLastIP() {
		return lastIP;
	}

	public void setLastIP(String lastIP) {
		this.lastIP = lastIP;
	}

	public String getCurrentIP() {
		return currentIP;
	}

	public void setCurrentIP(String currentIP) {
		this.currentIP = currentIP;
	}

	public Date getSignInTime() {
		return signInTime;
	}

	public void setSignInTime(Date signInTime) {
		this.signInTime = signInTime;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
	
	
}
