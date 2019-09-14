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

@Entity(name="TbMOTIVADORESCARGOS")
@Table(name="TB_MOTIVADORES_CARGOS")
public class TbMOTIVADORESCARGOSEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne(optional=true)
    @JoinColumn(name = "IDCARGOS_ID", referencedColumnName = "ID")
    private TbCARGOSEntity idCARGOS;

    @ManyToOne(optional=true)
    @JoinColumn(name = "IDMOTIVADORES_ID", referencedColumnName = "ID")
    private TbMOTIVADORESEntity idMOTIVADORES;

    @Digits(integer = 5,  fraction = 2)
    @Column(precision = 7, scale = 2, name="PONTUACAO_MOTIVADORES")
    @NotNull
    private BigDecimal poNTUACAOMOTIVADORES;

    public TbCARGOSEntity getIdCARGOS() {
        return this.idCARGOS;
    }

    public void setIdCARGOS(TbCARGOSEntity tbCARGOS) {
        this.idCARGOS = tbCARGOS;
    }

    public TbMOTIVADORESEntity getIdMOTIVADORES() {
        return this.idMOTIVADORES;
    }

    public void setIdMOTIVADORES(TbMOTIVADORESEntity tbMOTIVADORES) {
        this.idMOTIVADORES = tbMOTIVADORES;
    }

    public BigDecimal getPoNTUACAOMOTIVADORES() {
        return this.poNTUACAOMOTIVADORES;
    }

    public void setPoNTUACAOMOTIVADORES(BigDecimal poNTUACAOMOTIVADORES) {
        this.poNTUACAOMOTIVADORES = poNTUACAOMOTIVADORES;
    }

}
