package org.applicationn.simtrilhas.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity(name="TbPERFIL")
@Table(name="TB_PERFIL")
public class TbPERFILEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name="DESC_PERFIL")
    @NotNull
    private String deSCPERFIL;

    @Column(name = "ENTRY_CREATED_BY")
    private String createdBy;

    @Column(name = "ENTRY_CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "ENTRY_MODIFIED_BY")
    private String modifiedBy;

    @Column(name = "ENTRY_MODIFIED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;
    
    @Column(name="PENALIDADE_PERFIL")
    private Double penalidadeConhecPerfil;
    
    @Column(name="BLOQUEIA_MOV_PERFIL")
    private String bloqueiaMovConhecPerfil;
    
    @Column(name="PERFIL_CUSTOM")
    private String conhecPerfilCustom;
    
    @Column(name="GRUPO", insertable=false, updatable = false)
    private Integer grupo;
    
    @ManyToOne(optional=true)
    @JoinColumn(name = "GRUPO", referencedColumnName = "ID_GRUPO")
    private TbMASCARAEntity tbMascara;
    
	

	public TbMASCARAEntity getTbMascara() {
		return tbMascara;
	}

	public void setTbMascara(TbMASCARAEntity tbMascara) {
		this.tbMascara = tbMascara;
	}
	
	public Integer getGrupo() {
		return grupo;
	}

	public void setGrupo(Integer idGrupo) {
		this.grupo = idGrupo;
	}
    
    
    public Double getPenalidadeConhecPerfil() {
		return penalidadeConhecPerfil;
	}

	public void setPenalidadeConhecPerfil(Double double1) {
		this.penalidadeConhecPerfil = double1;
	}

	public String getBloqueiaMovConhecPerfil() {
		return bloqueiaMovConhecPerfil;
	}

	public void setBloqueiaMovConhecPerfil(String bloqueiaMovConhecPerfil) {
		this.bloqueiaMovConhecPerfil = bloqueiaMovConhecPerfil;
	}

	public String getConhecPerfilCustom() {
		return conhecPerfilCustom;
	}

	public void setConhecPerfilCustom(String conhecPerfilCustom) {
		this.conhecPerfilCustom = conhecPerfilCustom;
	}

	public String getDeSCPERFIL() {
        return this.deSCPERFIL;
    }

    public void setDeSCPERFIL(String deSCPERFIL) {
        this.deSCPERFIL = deSCPERFIL;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }
    
    public void updateAuditInformation(String username) {
        if (this.getId() != null) {
            modifiedAt = new Date();
            modifiedBy = username;
        } else {
            createdAt = new Date();
            modifiedAt = createdAt;
            createdBy = username;
            modifiedBy = createdBy;
        }
    }
    
}
