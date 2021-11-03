package com.resttest.domain.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "REST_TEST_MASTER")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class REST_TEST_PK implements Serializable{
	
	@Column(name = "ID")
	private String id;

}
