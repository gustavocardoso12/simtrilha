package org.applicationn.simtrilhas.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Entity(name="TbCOMPETENCIASCARGOS")
@Table(name="\"TBCOMPETENCIASCARGOS\"")
public class TbCOMPETENCIASCARGOSEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne(optional=true)
    @JoinColumn(name = "IDCOMPETENCIAS_ID", referencedColumnName = "ID")
    private TbCOMPETENCIASEntity idCOMPETENCIAS;

    @ManyToOne(optional=true)
    @JoinColumn(name = "IDCARGOS_ID", referencedColumnName = "ID")
    private TbCARGOSEntity idCARGOS;


    @Column(name="PONTUACAO_COMPETENCIA")
    @NotNull
    private int poNTUACAOCOMPETENCIA;

    public TbCOMPETENCIASEntity getIdCOMPETENCIAS() {
        return this.idCOMPETENCIAS;
    }

    public void setIdCOMPETENCIAS(TbCOMPETENCIASEntity tbCOMPETENCIAS) {
        this.idCOMPETENCIAS = tbCOMPETENCIAS;
    }

    public TbCARGOSEntity getIdCARGOS() {
        return this.idCARGOS;
    }

    public void setIdCARGOS(TbCARGOSEntity tbCARGOS) {
        this.idCARGOS = tbCARGOS;
    }

    public int getPoNTUACAOCOMPETENCIA() {
        return this.poNTUACAOCOMPETENCIA;
    }

    public void setPoNTUACAOCOMPETENCIA(int poNTUACAOCOMPETENCIA) {
        this.poNTUACAOCOMPETENCIA = poNTUACAOCOMPETENCIA;
    }

}
