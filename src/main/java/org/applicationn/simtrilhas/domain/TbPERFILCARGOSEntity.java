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

@Entity(name="TbPERFILCARGOS")
@Table(name="TB_PERFIL_CARGOS")
public class TbPERFILCARGOSEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne(optional=true)
    @JoinColumn(name = "IDCARGOS_ID", referencedColumnName = "ID")
    private TbCARGOSEntity idCARGOS;

    @ManyToOne(optional=true)
    @JoinColumn(name = "IDPERFIL_ID", referencedColumnName = "ID")
    private TbPERFILEntity idPERFIL;

    @Digits(integer = 5,  fraction = 2)
    @Column(precision = 7, scale = 2, name="PONTUACAO_PERFIL")
    @NotNull
    private BigDecimal poNTUACAOPERFIL;

    public TbCARGOSEntity getIdCARGOS() {
        return this.idCARGOS;
    }

    public void setIdCARGOS(TbCARGOSEntity tbCARGOS) {
        this.idCARGOS = tbCARGOS;
    }

    public TbPERFILEntity getIdPERFIL() {
        return this.idPERFIL;
    }

    public void setIdPERFIL(TbPERFILEntity tbPERFIL) {
        this.idPERFIL = tbPERFIL;
    }

    public BigDecimal getPoNTUACAOPERFIL() {
        return this.poNTUACAOPERFIL;
    }

    public void setPoNTUACAOPERFIL(BigDecimal poNTUACAOPERFIL) {
        this.poNTUACAOPERFIL = poNTUACAOPERFIL;
    }

}
