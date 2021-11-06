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
import javax.validation.constraints.NotNull;

@Entity(name="TbCOMPETENCIASCARGOS")
@Table(name="\"TBCOMPETENCIASCARGOS\"")
public class TbCOMPETENCIASCARGOSEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne(optional=true)
    @JoinColumn(name = "IDCOMPETENCIAS_ID", referencedColumnName = "ID")
    private TbCOMPETENCIASEntity idCOMPETENCIAS;

    @ManyToOne(optional=true)
    @JoinColumn(name = "IDCARGOS_ID", referencedColumnName = "ID")
    private TbCARGOSEntity idCARGOS;


    @Column(name="PONTUACAO_COMPETENCIA")
    private Double poNTUACAOCOMPETENCIA;
    
    
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
    

    public TbCOMPETENCIASEntity getIdCOMPETENCIAS() {
        return this.idCOMPETENCIAS;
    }

    public void setIdCOMPETENCIAS(TbCOMPETENCIASEntity tbCOMPETENCIAS) {
        this.idCOMPETENCIAS = tbCOMPETENCIAS;
    }

    public TbCARGOSEntity getIdCARGOS() {
        return this.idCARGOS;
    }

    public void setIdCARGOS(TbCARGOSEntity tbCARGOS) {
        this.idCARGOS = tbCARGOS;
    }

    public Double getPoNTUACAOCOMPETENCIA() {
        return this.poNTUACAOCOMPETENCIA;
    }

    public void setPoNTUACAOCOMPETENCIA(Double poNTUACAOCOMPETENCIA) {
        this.poNTUACAOCOMPETENCIA = poNTUACAOCOMPETENCIA;
    }

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
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
