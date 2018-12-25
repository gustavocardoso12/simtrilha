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

@Entity(name="TbCOMPETENCIASCARGOS")
@Table(name="\"TBCOMPETENCIASCARGOS\"")
public class TbCOMPETENCIASCARGOSEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne(optional=true)
    @JoinColumn(name = "IDCOMPETENCIAS_ID", referencedColumnName = "ID")
    private TbCOMPETENCIASEntity idCOMPETENCIAS;

    @ManyToOne(optional=true)
    @JoinColumn(name = "IDCARGOS_ID", referencedColumnName = "ID")
    private TbCARGOSEntity idCARGOS;

    @Digits(integer = 5,  fraction = 2)
    @Column(precision = 7, scale = 2, name="\"poNTUACAOCOMPETENCIA\"")
    @NotNull
    private BigDecimal poNTUACAOCOMPETENCIA;

    public TbCOMPETENCIASEntity getIdCOMPETENCIAS() {
        return this.idCOMPETENCIAS;
    }

    public void setIdCOMPETENCIAS(TbCOMPETENCIASEntity tbCOMPETENCIAS) {
        this.idCOMPETENCIAS = tbCOMPETENCIAS;
    }

    public TbCARGOSEntity getIdCARGOS() {
        return this.idCARGOS;
    }

    public void setIdCARGOS(TbCARGOSEntity tbCARGOS) {
        this.idCARGOS = tbCARGOS;
    }

    public BigDecimal getPoNTUACAOCOMPETENCIA() {
        return this.poNTUACAOCOMPETENCIA;
    }

    public void setPoNTUACAOCOMPETENCIA(BigDecimal poNTUACAOCOMPETENCIA) {
        this.poNTUACAOCOMPETENCIA = poNTUACAOCOMPETENCIA;
    }

}
