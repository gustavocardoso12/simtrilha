package org.applicationn.simtrilhas.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity(name="TbCOMPETENCIAS")
@Table(name="\"TBCOMPETENCIAS\"")
public class TbCOMPETENCIASEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    @Column(name="DESC_COMPETENCIA")
    @NotNull
    private String deSCCOMPETENCIA;

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
    
    @Column(name="PENALIDADE_COMPETENCIAS")
    private Double penalidadeCompetencias;
    
    @Column(name="BLOQUEIA_MOV_COMPETENCIAS")
    private String bloqueiaMovCompetencias;
    
    @Column(name="COMPETENCIAS_CUSTOM")
    private String competenciasCustom;
    
    public String getDeSCCOMPETENCIA() {
        return this.deSCCOMPETENCIA;
    }

    public void setDeSCCOMPETENCIA(String deSCCOMPETENCIA) {
        this.deSCCOMPETENCIA = deSCCOMPETENCIA;
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

	public Double getPenalidadeCompetencias() {
		return penalidadeCompetencias;
	}

	public void setPenalidadeCompetencias(Double penalidadeCompetencias) {
		this.penalidadeCompetencias = penalidadeCompetencias;
	}

	public String getBloqueiaMovCompetencias() {
		return bloqueiaMovCompetencias;
	}

	public void setBloqueiaMovCompetencias(String bloqueiaMovCompetencias) {
		this.bloqueiaMovCompetencias = bloqueiaMovCompetencias;
	}

	public String getCompetenciasCustom() {
		return competenciasCustom;
	}

	public void setCompetenciasCustom(String competenciasCustom) {
		this.competenciasCustom = competenciasCustom;
	}
    
}
