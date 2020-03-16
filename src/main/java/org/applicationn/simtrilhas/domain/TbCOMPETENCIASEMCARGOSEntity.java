package org.applicationn.simtrilhas.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Entity(name="TbCOMPETENCIASEMCARGOS")
@Table(name="\"TBCOMPETENCIASEMCARGOS\"")
public class TbCOMPETENCIASEMCARGOSEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    //@ManyToOne(optional=true)
    //@JoinColumn(name = "IDCARGOS_ID", referencedColumnName = "ID")
    private TbCARGOSEntity idCARGOS;

    //@ManyToOne(optional=true)
    //@JoinColumn(name = "IDCOMPEM_ID", referencedColumnName = "ID")
    private TbCOMPETENCIASEMOCIONAISEntity idCOMPEM;

    @Digits(integer = 5,  fraction = 2)
    @Column(precision = 7, scale = 2, name="\"poNTUACAOCOMPEM\"")
    @NotNull
    private BigDecimal poNTUACAOCOMPEM;

    public TbCARGOSEntity getIdCARGOS() {
        return this.idCARGOS;
    }

    public void setIdCARGOS(TbCARGOSEntity tbCARGOS) {
        this.idCARGOS = tbCARGOS;
    }

    public TbCOMPETENCIASEMOCIONAISEntity getIdCOMPEM() {
        return this.idCOMPEM;
    }

    public void setIdCOMPEM(TbCOMPETENCIASEMOCIONAISEntity tbCOMPETENCIASEMOCIONAIS) {
        this.idCOMPEM = tbCOMPETENCIASEMOCIONAIS;
    }

    public BigDecimal getPoNTUACAOCOMPEM() {
        return this.poNTUACAOCOMPEM;
    }

    public void setPoNTUACAOCOMPEM(BigDecimal poNTUACAOCOMPEM) {
        this.poNTUACAOCOMPEM = poNTUACAOCOMPEM;
    }

}
