package org.applicationn.simtrilhas.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity(name="TbCARGOS")
@Table(name="\"TBCARGOS\"")
public class TbCARGOSEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 100)
    @Column(length = 100, name="DESC_CARGO")
    @NotNull
    private String deSCCARGO;

    @Size(max = 300)
    @Column(length = 300, name="MISSAO")
    @NotNull
    private String miSSAO;

    @Size(max = 500)
    @Column(length = 500, name="RESPONSABILIDADES")
    @NotNull
    private String reSPONSABILIDADES;

    @ManyToOne(optional=true)
    @JoinColumn(name = "IDDEPTO_ID", referencedColumnName = "ID")
    private TbDEPTOEntity idDEPTO;
    
    @ManyToOne(optional=true)
    @JoinColumn(name = "IDNO_ID", referencedColumnName = "ID")
    private TbNOEntity idNO;

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

}
