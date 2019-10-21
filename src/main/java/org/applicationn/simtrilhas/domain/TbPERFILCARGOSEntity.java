package org.applicationn.simtrilhas.domain;

import java.io.Serializable;

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


    @Column(name="PONTUACAO_PERFIL")
    @NotNull
    private int poNTUACAOPERFIL;

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

    public int getPoNTUACAOPERFIL() {
        return this.poNTUACAOPERFIL;
    }

    public void setPoNTUACAOPERFIL(int poNTUACAOPERFIL) {
        this.poNTUACAOPERFIL = poNTUACAOPERFIL;
    }

}
