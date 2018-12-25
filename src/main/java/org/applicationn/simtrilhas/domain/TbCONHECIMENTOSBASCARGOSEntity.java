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

@Entity(name="TbCONHECIMENTOSBASCARGOS")
@Table(name="\"TBCONHECIMENTOSBASCARGOS\"")
public class TbCONHECIMENTOSBASCARGOSEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne(optional=true)
    @JoinColumn(name = "IDCARGOS_ID", referencedColumnName = "ID")
    private TbCARGOSEntity idCARGOS;

    @ManyToOne(optional=true)
    @JoinColumn(name = "IDCONHECBAS_ID", referencedColumnName = "ID")
    private TbCONHECIMENTOSBASICOSEntity idCONHECBAS;

    @Digits(integer = 5,  fraction = 2)
    @Column(precision = 7, scale = 2, name="\"poNTUACAOCONBAS\"")
    @NotNull
    private BigDecimal poNTUACAOCONBAS;

    public TbCARGOSEntity getIdCARGOS() {
        return this.idCARGOS;
    }

    public void setIdCARGOS(TbCARGOSEntity tbCARGOS) {
        this.idCARGOS = tbCARGOS;
    }

    public TbCONHECIMENTOSBASICOSEntity getIdCONHECBAS() {
        return this.idCONHECBAS;
    }

    public void setIdCONHECBAS(TbCONHECIMENTOSBASICOSEntity tbCONHECIMENTOSBASICOS) {
        this.idCONHECBAS = tbCONHECIMENTOSBASICOS;
    }

    public BigDecimal getPoNTUACAOCONBAS() {
        return this.poNTUACAOCONBAS;
    }

    public void setPoNTUACAOCONBAS(BigDecimal poNTUACAOCONBAS) {
        this.poNTUACAOCONBAS = poNTUACAOCONBAS;
    }

}
