package org.applicationn.simtrilhas.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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

@Entity(name="TbCARGOS")
@Table(name="\"TBCARGOS\"")
public class TbCARGOSEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    @Column(name="DESC_CARGO")
    @NotNull
    private String deSCCARGO;

    @Column(name="MISSAO")
    private String miSSAO;


    @Column(name="RESPONSABILIDADES")
    private String reSPONSABILIDADES;

    @ManyToOne(optional=true)
    @JoinColumn(name = "IDDEPTO_ID", referencedColumnName = "ID")
    private TbDEPTOEntity idDEPTO;
    
    @ManyToOne(optional=true)
    @JoinColumn(name = "IDNO_ID", referencedColumnName = "ID")
    private TbNOEntity idNO;
    
    @Column(name="FLAG_PESSOA")
    private String flagPessoa;

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
    
    public Date getCreatedAt() {
        return createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getModifiedAt() {
    	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
    	String strDate= formatter.format(modifiedAt);
    	
        return strDate;
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

    public String getDeSCCARGO() {
        return this.deSCCARGO;
    }

    public void setDeSCCARGO(String deSCCARGO) {
        this.deSCCARGO = deSCCARGO;
    }

    public String getMiSSAO() {
        return this.miSSAO;
    }

    public void setMiSSAO(String miSSAO) {
        this.miSSAO = miSSAO;
    }

    public String getReSPONSABILIDADES() {
        return this.reSPONSABILIDADES;
    }

    public void setReSPONSABILIDADES(String reSPONSABILIDADES) {
        this.reSPONSABILIDADES = reSPONSABILIDADES;
    }

    public TbDEPTOEntity getIdDEPTO() {
        return this.idDEPTO;
    }

    public void setIdDEPTO(TbDEPTOEntity tbDEPTO) {
        this.idDEPTO = tbDEPTO;
    }

	public TbNOEntity getIdNO() {
		return idNO;
	}

	public void setIdNO(TbNOEntity idNO) {
		this.idNO = idNO;
	}

	public String getFlagPessoa() {
		return flagPessoa;
	}

	public void setFlagPessoa(String flagPessoa) {
		this.flagPessoa = flagPessoa;
	}

}
