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

@Entity(name="TbCONHECIMENTOSBASICOS")
@Table(name="\"TBCONHECIMENTOSBASICOS\"")
public class TbCONHECIMENTOSBASICOSEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name="DESC_CONHECIMENTOS_BASICOS")
    @NotNull
    private String deSCCONHECIMENTOSBASICOS;

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
    
    @Column(name="PENALIDADE_CONHECBAS")
    private int penalidadeConhecBas;
    
    @Column(name="BLOQUEIA_MOV_CONHECBAS")
    private String bloqueiaMovConhecBas;
    
    @Column(name="CONHECBAS_CUSTOM")
    private String conhecBasCustom;
    
    public String getDeSCCONHECIMENTOSBASICOS() {
        return this.deSCCONHECIMENTOSBASICOS;
    }

    public void setDeSCCONHECIMENTOSBASICOS(String deSCCONHECIMENTOSBASICOS) {
        this.deSCCONHECIMENTOSBASICOS = deSCCONHECIMENTOSBASICOS;
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

	public int getPenalidadeConhecBas() {
		return penalidadeConhecBas;
	}

	public void setPenalidadeConhecBas(int penalidadeConhecBas) {
		this.penalidadeConhecBas = penalidadeConhecBas;
	}

	public String getBloqueiaMovConhecBas() {
		return bloqueiaMovConhecBas;
	}

	public void setBloqueiaMovConhecBas(String bloqueiaMovConhecBas) {
		this.bloqueiaMovConhecBas = bloqueiaMovConhecBas;
	}

	public String getConhecBasCustom() {
		return conhecBasCustom;
	}

	public void setConhecBasCustom(String conhecBasCustom) {
		this.conhecBasCustom = conhecBasCustom;
	}
    
}
