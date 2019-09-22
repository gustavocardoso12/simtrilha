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

@Entity(name="TbGRADE")
@Table(name="TB_GRADE")
public class TbGRADEEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 200)
    @Column(length = 200, name="DESC_GRADE")
    @NotNull
    private String deSCGRADE;

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
    
    @Column(name="PENALIDADE_GRADE")
    private Double penalidadeConhecGrade;
    
    @Column(name="BLOQUEIA_MOV_GRADE")
    private String bloqueiaMovConhecGrade;
    
    @Column(name="GRADE_CUSTOM")
    private String conhecGradeCustom;
    
    public String getDeSCGRADE() {
        return this.deSCGRADE;
    }

    public Double getPenalidadeConhecGrade() {
		return penalidadeConhecGrade;
	}

	public void setPenalidadeConhecGrade(Double penalidadeConhecGrade) {
		this.penalidadeConhecGrade = penalidadeConhecGrade;
	}

	public String getBloqueiaMovConhecGrade() {
		return bloqueiaMovConhecGrade;
	}

	public void setBloqueiaMovConhecGrade(String bloqueiaMovConhecGrade) {
		this.bloqueiaMovConhecGrade = bloqueiaMovConhecGrade;
	}

	public String getConhecGradeCustom() {
		return conhecGradeCustom;
	}

	public void setConhecGradeCustom(String conhecGradeCustom) {
		this.conhecGradeCustom = conhecGradeCustom;
	}

	public void setDeSCGRADE(String deSCGRADE) {
        this.deSCGRADE = deSCGRADE;
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
