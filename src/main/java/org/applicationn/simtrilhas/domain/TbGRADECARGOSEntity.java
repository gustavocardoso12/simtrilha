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

@Entity(name="TbGRADECARGOS")
@Table(name="TB_GRADES_CARGOS")
public class TbGRADECARGOSEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne(optional=true)
    @JoinColumn(name = "IDCARGOS_ID", referencedColumnName = "ID")
    private TbCARGOSEntity idCARGOS;

    @ManyToOne(optional=true)
    @JoinColumn(name = "IDGRADE_ID", referencedColumnName = "ID")
    private TbGRADEEntity idGRADE;

    @Digits(integer = 5,  fraction = 2)
    @Column(precision = 7, scale = 2, name="PONTUACAO_GRADE")
    @NotNull
    private int poNTUACAOGRADE;

    public TbCARGOSEntity getIdCARGOS() {
        return this.idCARGOS;
    }

    public void setIdCARGOS(TbCARGOSEntity tbCARGOS) {
        this.idCARGOS = tbCARGOS;
    }

    public TbGRADEEntity getIdGRADE() {
        return this.idGRADE;
    }

    public void setIdGRADE(TbGRADEEntity tbGRADE) {
        this.idGRADE = tbGRADE;
    }

    public int getPoNTUACAOGRADE() {
        return this.poNTUACAOGRADE;
    }

    public void setPoNTUACAOGRADE(int poNTUACAOGRADE) {
        this.poNTUACAOGRADE = poNTUACAOGRADE;
    }

}
