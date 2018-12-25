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

@Entity(name="TbHABILIDADESCARGOS")
@Table(name="\"TBHABILIDADESCARGOS\"")
public class TbHABILIDADESCARGOSEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne(optional=true)
    @JoinColumn(name = "IDCARGOS_ID", referencedColumnName = "ID")
    private TbCARGOSEntity idCARGOS;

    @ManyToOne(optional=true)
    @JoinColumn(name = "IDHABCARGOS_ID", referencedColumnName = "ID")
    private TbHABILIDADESEntity idHABCARGOS;

    @Digits(integer = 5,  fraction = 2)
    @Column(precision = 7, scale = 2, name="\"poNTUACAOHABCARGOS\"")
    @NotNull
    private BigDecimal poNTUACAOHABCARGOS;

    public TbCARGOSEntity getIdCARGOS() {
        return this.idCARGOS;
    }

    public void setIdCARGOS(TbCARGOSEntity tbCARGOS) {
        this.idCARGOS = tbCARGOS;
    }

    public TbHABILIDADESEntity getIdHABCARGOS() {
        return this.idHABCARGOS;
    }

    public void setIdHABCARGOS(TbHABILIDADESEntity tbHABILIDADES) {
        this.idHABCARGOS = tbHABILIDADES;
    }

    public BigDecimal getPoNTUACAOHABCARGOS() {
        return this.poNTUACAOHABCARGOS;
    }

    public void setPoNTUACAOHABCARGOS(BigDecimal poNTUACAOHABCARGOS) {
        this.poNTUACAOHABCARGOS = poNTUACAOHABCARGOS;
    }

}
