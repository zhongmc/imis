package com.ynet.imis.domain.org;

import javax.persistence.Column;
//import javax.validation.constraints.NotBlank;
import javax.persistence.Entity;

import com.ynet.imis.domain.TreeEntity;

import org.hibernate.validator.constraints.Length;

@Entity
public class Custom extends TreeEntity<Custom>{

    
	private static final long serialVersionUID = 8848238179585679119L;
	
	@Column(length = 100)
	@Length(min = 2, max = 100, message = "用户名长度为4-100")
	private String name;// 名称
//	@NotBlank(message = "登录名不能为空")


	public Custom(){
		
	}

    public Custom(Long id, long pid, String name )
    {
        super.setId(id);
        super.setParentId( pid);
        
        this.name = name;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format("Custom, id:%s, name:%s", id, name);
	}

}