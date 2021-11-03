package com.resttest.domain.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "REST_TEST_MASTER")
@IdClass(REST_TEST_PK.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class REST_TEST implements Serializable{
	
	@Id
	@Column(name = "ID")
	private String id;
	
	@Id
	@Column(name = "NAME")
	private String name;

}
