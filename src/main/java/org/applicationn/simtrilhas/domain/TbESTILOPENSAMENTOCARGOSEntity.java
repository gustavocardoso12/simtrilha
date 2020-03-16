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

@Entity(name="TbESTILOPENSAMENTOCARGOS")
@Table(name="\"TBESTILOPENSAMENTOCARGOS\"")
public class TbESTILOPENSAMENTOCARGOSEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

   // @ManyToOne(optional=true)
   // @JoinColumn(name = "IDCARGOS_ID", referencedColumnName = "ID")
    private TbCARGOSEntity idCARGOS;

    //@ManyToOne(optional=true)
   // @JoinColumn(name = "IDESTPENSAMENTO_ID", referencedColumnName = "ID")
    private TbESTILOPENSAMENTOEntity idESTPENSAMENTO;

    @Digits(integer = 5,  fraction = 2)
    @Column(precision = 7, scale = 2, name="\"poNTUACAOESTPENSAMENTO\"")
    @NotNull
    private BigDecimal poNTUACAOESTPENSAMENTO;

    public TbCARGOSEntity getIdCARGOS() {
        return this.idCARGOS;
    }

    public void setIdCARGOS(TbCARGOSEntity tbCARGOS) {
        this.idCARGOS = tbCARGOS;
    }

    public TbESTILOPENSAMENTOEntity getIdESTPENSAMENTO() {
        return this.idESTPENSAMENTO;
    }

    public void setIdESTPENSAMENTO(TbESTILOPENSAMENTOEntity tbESTILOPENSAMENTO) {
        this.idESTPENSAMENTO = tbESTILOPENSAMENTO;
    }

    public BigDecimal getPoNTUACAOESTPENSAMENTO() {
        return this.poNTUACAOESTPENSAMENTO;
    }

    public void setPoNTUACAOESTPENSAMENTO(BigDecimal poNTUACAOESTPENSAMENTO) {
        this.poNTUACAOESTPENSAMENTO = poNTUACAOESTPENSAMENTO;
    }

}
