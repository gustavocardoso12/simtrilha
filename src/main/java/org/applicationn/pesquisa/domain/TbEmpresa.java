package org.applicationn.pesquisa.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.applicationn.simtrilhas.domain.BaseEntity;

@Entity(name="TbEmpresa")
@Table(name="TB_EMPRESA")
public class TbEmpresa extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    @Column(name="dsEmpresa")
    @NotNull
    private String descEmpresa;

    @Column(name = "createdAt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    public Date getCreatedAt() {
        return createdAt;
    }

    public String getDescEmpresa() {
		return descEmpresa;
	}

	public void setDescEmpresa(String descMercado) {
		this.descEmpresa = descMercado;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public void updateAuditInformation(String username) {
        if (this.getId() != null) {
        } else {
            createdAt = new Date();
        }
    }
    
}
