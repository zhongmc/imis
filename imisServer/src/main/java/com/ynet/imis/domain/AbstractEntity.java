/**
* Domain.java
* @author ZHONGMC
* @description 
* @created Wed Sep 12 2018 14:38:55 GMT+0800 (中国标准时间)
* @copyright 
* @last-modified Wed Sep 12 2018 14:41:31 GMT+0800 (中国标准时间)
*/





package com.ynet.imis.domain;

import java.io.Serializable;


import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.GenerationType;


import com.fasterxml.jackson.annotation.JsonIgnore;




@MappedSuperclass
public abstract class AbstractEntity implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1196692249484848642L;
	
	
	/**
	 * 条目id,数据库主键，自动赋值，不需要程序员手工赋值

	@GeneratedValue(generator = "sequenceStyleGenerator")
	@GenericGenerator(name = "sequenceStyleGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
    @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "YC_ID_SEQUENCE"), 
    @Parameter(name = SequenceStyleGenerator.INITIAL_PARAM, value = "1000"),
	@Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = "1"), @Parameter(name = SequenceStyleGenerator.OPT_PARAM, value = "pooled") })

    @GeneratedValue(strategy=GenerationType.AUTO)
	 *
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected Long id;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.domain.Persistable#getId()
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id of the entity.
	 * 
	 * @param id the id to set
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.domain.Persistable#isNew()
	 */
	@JsonIgnore
	public boolean isNew() {
		return null == getId();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("Entity of type %s with id: %s", this.getClass().getName(), getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (null == obj) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		AbstractEntity that = (AbstractEntity) obj;

		return null == this.getId() ? false : this.getId().equals(that.getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		int hashCode = 17;

		hashCode += null == getId() ? 0 : getId().hashCode() * 31;

		return hashCode;
	}
	
	
}
