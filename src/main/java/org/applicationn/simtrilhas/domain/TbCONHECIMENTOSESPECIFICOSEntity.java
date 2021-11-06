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

@Entity(name="TbCONHECIMENTOSESPECIFICOS")
@Table(name="\"TBCONHECIMENTOSESPECIFICOS\"")
public class TbCONHECIMENTOSESPECIFICOSEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    @Column(name="DESC_CONHECIMENTOS_ESPECIFICOS")
    @NotNull
    private String deSCCONHECIMENTOSESPECIFICOS;

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
    
    @Column(name="PENALIDADE_CONHECESP")
    private Double penalidadeConhecBas;
    
    @Column(name="BLOQUEIA_MOV_CONHECESP")
    private String bloqueiaMovConhecEsp;
    
    @Column(name="CONHECESP_CUSTOM")
    private String conhecEspCustom;
    
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
    
    public String getDeSCCONHECIMENTOSESPECIFICOS() {
        return this.deSCCONHECIMENTOSESPECIFICOS;
    }

    public void setDeSCCONHECIMENTOSESPECIFICOS(String deSCCONHECIMENTOSESPECIFICOS) {
        this.deSCCONHECIMENTOSESPECIFICOS = deSCCONHECIMENTOSESPECIFICOS;
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

	public Double getPenalidadeConhecBas() {
		return penalidadeConhecBas;
	}

	public void setPenalidadeConhecBas(Double double1) {
		this.penalidadeConhecBas = double1;
	}

	public String getBloqueiaMovConhecEsp() {
		return bloqueiaMovConhecEsp;
	}

	public void setBloqueiaMovConhecEsp(String bloqueiaMovConhecEsp) {
		this.bloqueiaMovConhecEsp = bloqueiaMovConhecEsp;
	}

	public String getConhecEspCustom() {
		return conhecEspCustom;
	}

	public void setConhecEspCustom(String conhecEspCustom) {
		this.conhecEspCustom = conhecEspCustom;
	}
    
}
