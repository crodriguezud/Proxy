package com.mercadolibre.proxy.core.entities;

import java.io.Serializable;
//import java.util.Calendar;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(value="request")
public class Request implements Serializable {

	
	private static final long serialVersionUID = -575411066499743649L;
	
	@Id
	private Long id;
	private String ipOrigin;
	private String path;
	private String method;
	//private Calendar createdDate;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getIpOrigin() {
		return ipOrigin;
	}
	public void setIpOrigin(String ipOrigin) {
		this.ipOrigin = ipOrigin;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	

}
