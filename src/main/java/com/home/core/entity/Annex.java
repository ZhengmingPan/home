package com.home.core.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.home.common.entity.UuIdEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 附件
 * 
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "core_annex")
public class Annex extends UuIdEntity {

	public static final String PROP_NAME = "name";
	public static final String PROP_PATH = "path";

	public static final String TEMP_PICTURE_PATH = "/temp";
	public static final String TEMP_PICTURE_TYPE = "temp";

	private String ObjectId;
	private String ObjectType;
	private String path;
	private String name;
	private String type;
	private Date createTime;
	private Long creator;
	private String fastdfsUrl;

}
