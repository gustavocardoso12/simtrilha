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

@Entity(name="TbGRADECARGOS")
@Table(name="TB_GRADES_CARGOS")
public class TbGRADECARGOSEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne(optional=true)
    @JoinColumn(name = "IDCARGOS_ID", referencedColumnName = "ID")
    private TbCARGOSEntity idCARGOS;

    @ManyToOne(optional=true)
    @JoinColumn(name = "IDGRADE_ID", referencedColumnName = "ID")
    private TbGRADEEntity idGRADE;

    @Digits(integer = 5,  fraction = 2)
    @Column(name="PONTUACAO_GRADE")
    private Double poNTUACAOGRADE;
    
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

    public TbCARGOSEntity getIdCARGOS() {
        return this.idCARGOS;
    }

    public void setIdCARGOS(TbCARGOSEntity tbCARGOS) {
        this.idCARGOS = tbCARGOS;
    }

    public TbGRADEEntity getIdGRADE() {
        return this.idGRADE;
    }

    public void setIdGRADE(TbGRADEEntity tbGRADE) {
        this.idGRADE = tbGRADE;
    }

    public Double getPoNTUACAOGRADE() {
        return this.poNTUACAOGRADE;
    }

    public void setPoNTUACAOGRADE(Double gapdePARA) {
        this.poNTUACAOGRADE = gapdePARA;
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
