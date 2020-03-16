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

@Entity(name="TbESTILOLIDERANCACARGOS")
@Table(name="\"TBESTILOLIDERANCACARGOS\"")
public class TbESTILOLIDERANCACARGOSEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    //@ManyToOne(optional=true)
    //@JoinColumn(name = "IDCARGOS_ID", referencedColumnName = "ID")
    private TbCARGOSEntity idCARGOS;

    //@ManyToOne(optional=true)
    //@JoinColumn(name = "IDESTLIDER_ID", referencedColumnName = "ID")
    private TbESTILOLIDERANCAEntity idESTLIDER;

    @Digits(integer = 5,  fraction = 2)
    @Column(precision = 7, scale = 2, name="\"poNTUACAOESTLIDER\"")
    @NotNull
    private BigDecimal poNTUACAOESTLIDER;

    public TbCARGOSEntity getIdCARGOS() {
        return this.idCARGOS;
    }

    public void setIdCARGOS(TbCARGOSEntity tbCARGOS) {
        this.idCARGOS = tbCARGOS;
    }

    public TbESTILOLIDERANCAEntity getIdESTLIDER() {
        return this.idESTLIDER;
    }

    public void setIdESTLIDER(TbESTILOLIDERANCAEntity tbESTILOLIDERANCA) {
        this.idESTLIDER = tbESTILOLIDERANCA;
    }

    public BigDecimal getPoNTUACAOESTLIDER() {
        return this.poNTUACAOESTLIDER;
    }

    public void setPoNTUACAOESTLIDER(BigDecimal poNTUACAOESTLIDER) {
        this.poNTUACAOESTLIDER = poNTUACAOESTLIDER;
    }

}
