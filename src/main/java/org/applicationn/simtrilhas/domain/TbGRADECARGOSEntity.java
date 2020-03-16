package org.applicationn.simtrilhas.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;

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
    @Column(name="PONTUACAO_GRADE")
    private Integer poNTUACAOGRADE;

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

    public Integer getPoNTUACAOGRADE() {
        return this.poNTUACAOGRADE;
    }

    public void setPoNTUACAOGRADE(Integer poNTUACAOGRADE) {
        this.poNTUACAOGRADE = poNTUACAOGRADE;
    }

}
