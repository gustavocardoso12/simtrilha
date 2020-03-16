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

@Entity(name="TbHABILIDADESCULTCARGOS")
@Table(name="\"TBHABILIDADESCULTCARGOS\"")
public class TbHABILIDADESCULTCARGOSEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

   // @ManyToOne(optional=true)
   // @JoinColumn(name = "IDCARGOS_ID", referencedColumnName = "ID")
    private TbCARGOSEntity idCARGOS;

    //@ManyToOne(optional=true)
    //@JoinColumn(name = "IDHABCULTCAR_ID", referencedColumnName = "ID")
    private TbHABILIDADESCULTURAISEntity idHABCULTCAR;

    @Digits(integer = 5,  fraction = 2)
    @Column(precision = 7, scale = 2, name="\"poNTUACAOHABCULT\"")
    private BigDecimal poNTUACAOHABCULT;

    public TbCARGOSEntity getIdCARGOS() {
        return this.idCARGOS;
    }

    public void setIdCARGOS(TbCARGOSEntity tbCARGOS) {
        this.idCARGOS = tbCARGOS;
    }

    public TbHABILIDADESCULTURAISEntity getIdHABCULTCAR() {
        return this.idHABCULTCAR;
    }

    public void setIdHABCULTCAR(TbHABILIDADESCULTURAISEntity tbHABILIDADESCULTURAIS) {
        this.idHABCULTCAR = tbHABILIDADESCULTURAIS;
    }

    public BigDecimal getPoNTUACAOHABCULT() {
        return this.poNTUACAOHABCULT;
    }

    public void setPoNTUACAOHABCULT(BigDecimal poNTUACAOHABCULT) {
        this.poNTUACAOHABCULT = poNTUACAOHABCULT;
    }

}
