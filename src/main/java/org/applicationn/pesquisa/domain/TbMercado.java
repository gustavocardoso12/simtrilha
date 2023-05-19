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

@Entity(name="TbMercado")
@Table(name="TB_MERCADO")
public class TbMercado extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    @Column(name="dsMercado")
    @NotNull
    private String descMercado;

    @Column(name = "createdAt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    public Date getCreatedAt() {
        return createdAt;
    }

    public String getDescMercado() {
		return descMercado;
	}

	public void setDescMercado(String descMercado) {
		this.descMercado = descMercado;
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
