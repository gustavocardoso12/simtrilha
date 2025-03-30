package org.applicationn.pesquisa.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.applicationn.simtrilhas.domain.BaseEntity;
import org.applicationn.simtrilhas.domain.security.UserEntity;

@Entity(name="TbUsuarioMercado")
@Table(name="TB_USUARIO_MERCADO")
public class TbUsuarioMercado extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @ManyToOne
    @JoinColumn(name="cd_usuario", referencedColumnName="id")
    @NotNull
    private UserEntity empresa;

    @ManyToOne
    @JoinColumn(name="cd_mercado", referencedColumnName="id")
    @NotNull
    private TbMercado mercado;

    @Column(name = "createdAt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    public TbUsuarioMercado() {
        this.createdAt = new Date();
    }
    
    

    public TbMercado getMercado() {
        return mercado;
    }

    public void setMercado(TbMercado mercado) {
        this.mercado = mercado;
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

	public UserEntity getEmpresa() {
		return empresa;
	}



	public void setEmpresa(UserEntity empresa) {
		this.empresa = empresa;
	}
}