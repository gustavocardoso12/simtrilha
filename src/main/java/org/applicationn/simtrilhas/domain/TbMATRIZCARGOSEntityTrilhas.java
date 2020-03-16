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
import javax.validation.constraints.Digits;


@Entity(name="TbMATRIZCARGOSTrilhas")
@Table(name="TB_MATRIZ_CARGOS")
public class TbMATRIZCARGOSEntityTrilhas extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @ManyToOne(optional=true)
    @JoinColumn(name = "ID_CARGODE", referencedColumnName = "ID")
    private TbCARGOSEntity idCARGODE;

    @ManyToOne(optional=true)
    @JoinColumn(name = "ID_CARGOPARA", referencedColumnName = "ID")
    private TbCARGOSEntity idCARGOPARA;

    @Column(name="ADERENCIA_FINAL")
    @Digits(integer = 4, fraction = 2)
    private Double adERENCIAFINAL;

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
    
    @Column(name = "COR_ADERENCIA")
    private String corAderencia;
    
    @Column(name="ESTAGNADO")
    private String estaganado; 
    




    public Double getAdERENCIAFINAL() {
        return this.adERENCIAFINAL;
    }

    public void setAdERENCIAFINAL(Double aderenciafinal2) {
        this.adERENCIAFINAL = aderenciafinal2;
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

	public String getCorAderencia() {
		return corAderencia;
	}

	public void setCorAderencia(String corAderencia) {
		this.corAderencia = corAderencia;
	}

	public TbCARGOSEntity getIdCARGODE() {
		return idCARGODE;
	}

	public void setIdCARGODE(TbCARGOSEntity idCARGODE) {
		this.idCARGODE = idCARGODE;
	}

	public TbCARGOSEntity getIdCARGOPARA() {
		return idCARGOPARA;
	}

	public void setIdCARGOPARA(TbCARGOSEntity idCARGOPARA) {
		this.idCARGOPARA = idCARGOPARA;
	}

	public String getEstaganado() {
		return estaganado;
	}

	public void setEstaganado(String estaganado) {
		this.estaganado = estaganado;
	}
    
}
