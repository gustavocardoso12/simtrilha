package org.applicationn.pesquisa.vo;
import java.io.Serializable;
import java.sql.Date;

import org.applicationn.simtrilhas.domain.BaseEntity;

public class RelatorioUsuariosVO extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    private String username;
    private String sistema;
    private Date createdAt;
    
    
    
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSistema() {
		return sistema;
	}
	public void setSistema(String sistema) {
		this.sistema = sistema;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

    
    
}
