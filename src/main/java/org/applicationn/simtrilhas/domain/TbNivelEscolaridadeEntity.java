package org.applicationn.simtrilhas.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity(name="tbNIVELESCOLARIDADE")
@Table(name="TB_NIVEL_ESCOLARIDADE")
public class TbNivelEscolaridadeEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

  
    @Column(name="DESC_FORMACAO")
    @NotNull
    private String descFormacao;
    
    @Column(name = "ID_FORMACAO")
    private Integer idFormacao;

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

    @Column(name = "GRUPO")
    private Integer grupo;
	
	
  

	public Integer getGrupo() {
		return grupo;
	}

	public void setGrupo(Integer grupo) {
		this.grupo = grupo;
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
    
    public Integer getIdFormacao() {
		return idFormacao;
	}

	public void setIdFormacao(Integer idFormacao) {
		this.idFormacao = idFormacao;
	}
	
	public String getDescFormacao() {
		return descFormacao;
	}

	public void setDescFormacao(String descFormacao) {
		this.descFormacao = descFormacao;
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
