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

@Entity(name="TbNO")
@Table(name="TB_NO")
public class TbNOEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

   
    @Column(name="DESC_NIVEL")
    @NotNull
    private String deSCNivel;
    
    @Column(name="ID_NIVEL")
    private Long idNivel;

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

	public String getDeSCNivel() {
		return deSCNivel;
	}

	public void setDeSCNivel(String deSCNivel) {
		this.deSCNivel = deSCNivel;
	}

	public Long getIdNivel() {
		return idNivel;
	}

	public void setIdNivel(Long idNivel) {
		this.idNivel = idNivel;
	}
    
}
