package org.applicationn.simtrilhas.domain;

import java.io.Serializable;
import java.math.BigDecimal;
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

@Entity(name="TbCONHECIMENTOSESPCARGOS")
@Table(name="\"TBCONHECIMENTOSESPCARGOS\"")
public class TbCONHECIMENTOSESPCARGOSEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne(optional=true)
    @JoinColumn(name = "IDCARGOS_ID", referencedColumnName = "ID")
    private TbCARGOSEntity idCARGOS;

    @ManyToOne(optional=true)
    @JoinColumn(name = "IDCONHECESP_ID", referencedColumnName = "ID")
    private TbCONHECIMENTOSESPECIFICOSEntity idCONHECESP;

    @Column(name="MASCARA")
    private String mascara;
    
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
  

    public String getMascara() {
		return mascara;
	}

	public void setMascara(String mascara) {
		this.mascara = mascara;
	}
    
    
    @Column(name="PONTUACAO_CON_ESP")
    private Double poNTUACAOCONESP;

    public TbCARGOSEntity getIdCARGOS() {
        return this.idCARGOS;
    }

    public void setIdCARGOS(TbCARGOSEntity tbCARGOS) {
        this.idCARGOS = tbCARGOS;
    }

    public TbCONHECIMENTOSESPECIFICOSEntity getIdCONHECESP() {
        return this.idCONHECESP;
    }

    public void setIdCONHECESP(TbCONHECIMENTOSESPECIFICOSEntity tbCONHECIMENTOSESPECIFICOS) {
        this.idCONHECESP = tbCONHECIMENTOSESPECIFICOS;
    }

    public Double getPoNTUACAOCONESP() {
        return this.poNTUACAOCONESP;
    }

    public void setPoNTUACAOCONESP(Double poNTUACAOCONESP) {
        this.poNTUACAOCONESP = poNTUACAOCONESP;
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
