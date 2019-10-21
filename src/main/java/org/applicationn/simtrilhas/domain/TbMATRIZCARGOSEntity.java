package org.applicationn.simtrilhas.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;

@Entity(name="TbMATRIZCARGOS")
@Table(name="TB_MATRIZ_CARGOS")
public class TbMATRIZCARGOSEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name="ID_CARGODE")
    @Digits(integer = 4, fraction = 0)
    private Long idCARGODE;

    @Column(name="ID_CARGOPARA")
    @Digits(integer = 4, fraction = 0)
    private Long idCARGOPARA;

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
    
    public Long getIdCARGODE() {
        return this.idCARGODE;
    }

    public void setIdCARGODE(Long long1) {
        this.idCARGODE = long1;
    }

    public Long getIdCARGOPARA() {
        return this.idCARGOPARA;
    }

    public void setIdCARGOPARA(Long idCARGOPARA) {
        this.idCARGOPARA = idCARGOPARA;
    }

    public double getAdERENCIAFINAL() {
        return this.adERENCIAFINAL;
    }

    public void setAdERENCIAFINAL(double aderenciafinal2) {
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

	public String getEstaganado() {
		return estaganado;
	}

	public void setEstaganado(String estaganado) {
		this.estaganado = estaganado;
	}
    
}
