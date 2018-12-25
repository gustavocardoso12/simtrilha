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

@Entity(name="TbESTATUARIO")
@Table(name="\"TBESTATUARIO\"")
public class TbESTATUARIOEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 50)
    @Column(length = 50, name="\"deSCESTATUARIO\"")
    @NotNull
    private String deSCESTATUARIO;

    @ManyToOne(optional=true)
    @JoinColumn(name = "IDDIRETORIA_ID", referencedColumnName = "ID")
    private TbDIRETORIAEntity idDIRETORIA;

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
    
    public String getDeSCESTATUARIO() {
        return this.deSCESTATUARIO;
    }

    public void setDeSCESTATUARIO(String deSCESTATUARIO) {
        this.deSCESTATUARIO = deSCESTATUARIO;
    }

    public TbDIRETORIAEntity getIdDIRETORIA() {
        return this.idDIRETORIA;
    }

    public void setIdDIRETORIA(TbDIRETORIAEntity tbDIRETORIA) {
        this.idDIRETORIA = tbDIRETORIA;
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
