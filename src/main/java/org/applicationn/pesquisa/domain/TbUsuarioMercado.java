package org.applicationn.pesquisa.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.applicationn.simtrilhas.domain.BaseEntity;

@Entity(name="TbUsuarioMercado")
@Table(name="TB_USUARIO_MERCADO")
public class TbUsuarioMercado extends BaseEntity implements Serializable {
	 private static final long serialVersionUID = 1L;
	 
	 @ManyToOne(optional=true)
	    @JoinColumn(name = "cd_usuario", referencedColumnName = "id")
	    private TbEmpresa idEmpresa;

	    @ManyToOne(optional=true)
	    @JoinColumn(name = "cd_mercado", referencedColumnName = "id")
	    private TbMercado idMercado;

	    @Column(name = "createdAt")
	    @Temporal(TemporalType.TIMESTAMP)
	    private Date createdAt;

		public Date getCreatedAt() {
			return createdAt;
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

		public TbMercado getIdMercado() {
			return idMercado;
		}

		public void setIdMercado(TbMercado idMercado) {
			this.idMercado = idMercado;
		}

		public TbEmpresa getIdEmpresa() {
			return idEmpresa;
		}

		public void setIdEmpresa(TbEmpresa idEmpresa) {
			this.idEmpresa = idEmpresa;
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}
		
}
