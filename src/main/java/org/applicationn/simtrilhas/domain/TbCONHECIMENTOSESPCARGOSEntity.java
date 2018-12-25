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

@Entity(name="TbCONHECIMENTOSESPCARGOS")
@Table(name="\"TBCONHECIMENTOSESPCARGOS\"")
public class TbCONHECIMENTOSESPCARGOSEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne(optional=true)
    @JoinColumn(name = "IDCARGOS_ID", referencedColumnName = "ID")
    private TbCARGOSEntity idCARGOS;

    @ManyToOne(optional=true)
    @JoinColumn(name = "IDCONHECESP_ID", referencedColumnName = "ID")
    private TbCONHECIMENTOSESPECIFICOSEntity idCONHECESP;

    @Digits(integer = 5,  fraction = 2)
    @Column(precision = 7, scale = 2, name="\"poNTUACAOCONESP\"")
    @NotNull
    private BigDecimal poNTUACAOCONESP;

    public TbCARGOSEntity getIdCARGOS() {
        return this.idCARGOS;
    }

    public void setIdCARGOS(TbCARGOSEntity tbCARGOS) {
        this.idCARGOS = tbCARGOS;
    }

    public TbCONHECIMENTOSESPECIFICOSEntity getIdCONHECESP() {
        return this.idCONHECESP;
    }

    public void setIdCONHECESP(TbCONHECIMENTOSESPECIFICOSEntity tbCONHECIMENTOSESPECIFICOS) {
        this.idCONHECESP = tbCONHECIMENTOSESPECIFICOS;
    }

    public BigDecimal getPoNTUACAOCONESP() {
        return this.poNTUACAOCONESP;
    }

    public void setPoNTUACAOCONESP(BigDecimal poNTUACAOCONESP) {
        this.poNTUACAOCONESP = poNTUACAOCONESP;
    }

}
