/**
* User.java
* @author ZHONGMC
* @description 
* @created Wed Sep 12 2018 18:07:12 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Thu Oct 11 2018 16:24:01 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.domain.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ynet.imis.domain.AbstractEntity;
import com.ynet.imis.domain.org.Department;

@XmlRootElement(name = "User")
@Entity
public class User extends AbstractEntity implements Serializable, UserDetails {
	private static final long serialVersionUID = -5384967512982720774L;

	public enum UserStatus {
		ACTIVE, // 正常
		FREEZE, // 冻结
		DELETE, // 删除
		ALL, // 全部
		LOCK// 临时锁定
	}

	@Column(length = 50, nullable = false)
	private String userName;// 用户名,用于登陆

	private String number;// 操作员编号

	@Column(length = 50)
	private String nickName;// 昵称，用于显示

	@Column(name = "YC_DESC")
	private String desc;// 描述

	private Long avadaImage; // the avada upload file id

	@Column(name = "password", length = 150)
	private String password;// 密码

	@Column(length = 50)
	private String email;// 邮件

	@Column(length = 11)
	private String telNumber;// 手机号

	@Column(length = 20)
	private String docNumber;// 证件号

	// 0：没有；1：重置
	private int isPasswordChange;// 是否重置密码

	private boolean supervisor = false;// 是否为系统管理员

	@Enumerated(EnumType.STRING)
	private UserStatus status;// 状态

	@Transient
	private String roleName;// 角色名称

	@ManyToOne(fetch=FetchType.LAZY)
	@JsonIgnore // update shenhc
	@JoinColumn(name = "DEP_ID", insertable = false, updatable = false)
	private Department department;

	@Column(name = "DEP_ID")
	private Long depId;// 用户所属的机构id

		
	
	
	//初次密码存放处
	@Column(length = 100,nullable = true)
	private String firstPassword;

	/**
	 * @OneToMany(cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY,
	 *                    mappedBy="user")
	 */
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "USER_ROLE", //
	joinColumns = { @JoinColumn(name = "USER_ID", referencedColumnName = "ID") }, //
	inverseJoinColumns = { @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID") })
	private List<Role> roles = new java.util.ArrayList<Role>();// 用户的角色列表

	public User() {
	}


	public User(String name, String encodePass)
	{
		this.userName = name;
		this.password = encodePass;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void addRole(Role role) {
		roles.add(role);
	}

	@XmlElement
	public String getUserName() {
		return userName;
	}

	public String getUsername() {
		return userName;
	}

	public void setUserName(String userId) {
		this.userName = userId;
	}

	@XmlElement
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	@XmlElement
	public Long getAvadaImage() {
		return avadaImage;
	}

	public void setAvadaImage(Long avadaImage) {
		this.avadaImage = avadaImage;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@XmlElement
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelNumber() {
		return telNumber;
	}

	public void setTelNumber(String telNumber) {
		this.telNumber = telNumber;
	}

	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

	/**
	 * Returns the authorities granted to the user. Cannot return
	 * <code>null</code>.
	 *
	 * @return the authorities, sorted by natural key (never <code>null</code>)
	 */
	// public Collection<GrantedAuthority> getAuthorities() {
	// 	List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	// 	// authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));//
	// 	// GrantedAuthorityImpl("ROLE_ADMIN"));
	// 	authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
	// 	// authorities.add(new SimpleGrantedAuthority("ROLE_INCOMER"));
	// 	// authorities.add(new SimpleGrantedAuthority("ROLE_PRINTER"));
	// 	return authorities;

    // }
	
	

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }
    

	/**
	 * Indicates whether the user's account has expired. An expired account
	 * cannot be authenticated.
	 *
	 * @return <code>true</code> if the user's account is valid (ie
	 *         non-expired), <code>false</code> if no longer valid (ie expired)
	 */
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * Indicates whether the user is locked or unlocked. A locked user cannot be
	 * authenticated.
	 *
	 * @return <code>true</code> if the user is not locked, <code>false</code>
	 *         otherwise
	 */
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * Indicates whether the user's credentials (password) has expired. Expired
	 * credentials prevent authentication.
	 *
	 * @return <code>true</code> if the user's credentials are valid (ie
	 *         non-expired), <code>false</code> if no longer valid (ie expired)
	 */
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * Indicates whether the user is enabled or disabled. A disabled user cannot
	 * be authenticated.
	 *
	 * @return <code>true</code> if the user is enabled, <code>false</code>
	 *         otherwise
	 */
	@JsonIgnore
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String toString() {
		return "[User: ID=" + getId() + ";userName=" + userName + ";nickName=" + nickName + ";password=" + password
				+ "]";
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
		if( department != null)
			this.depId = department.getId();
		else
			depId = null;
	}

	public Long getDepId() {
		return depId;
	}

	public void setDepId(Long depId) {
		this.depId = depId;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public boolean isSupervisor() {
		return supervisor;
	}

	public void setSupervisor(boolean supervisor) {
		this.supervisor = supervisor;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public int getIsPasswordChange() {
		return isPasswordChange;
	}

	public void setIsPasswordChange(int isPasswordChange) {
		this.isPasswordChange = isPasswordChange;
	}

	public String getFirstPassword() {
		return firstPassword;
	}

	public void setFirstPassword(String firstPassword) {
		this.firstPassword = firstPassword;
	}

}
