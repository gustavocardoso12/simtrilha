package org.applicationn.simtrilhas.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity(name="TbCARGOS")
@Table(name="\"TBCARGOS\"")
public class TbCARGOSEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 100)
    @Column(length = 100, name="\"deSCCARGO\"")
    @NotNull
    private String deSCCARGO;

    @Size(max = 300)
    @Column(length = 300, name="\"miSSAO\"")
    @NotNull
    private String miSSAO;

    @Size(max = 500)
    @Column(length = 500, name="\"reSPONSABILIDADES\"")
    @NotNull
    private String reSPONSABILIDADES;

    @ManyToOne(optional=true)
    @JoinColumn(name = "IDEST_ID", referencedColumnName = "ID")
    private TbESTATUARIOEntity idEST;

    @ManyToOne(optional=true)
    @JoinColumn(name = "IDCURSOS_ID", referencedColumnName = "ID")
    private TbCURSOSEntity idCURSOS;

    public String getDeSCCARGO() {
        return this.deSCCARGO;
    }

    public void setDeSCCARGO(String deSCCARGO) {
        this.deSCCARGO = deSCCARGO;
    }

    public String getMiSSAO() {
        return this.miSSAO;
    }

    public void setMiSSAO(String miSSAO) {
        this.miSSAO = miSSAO;
    }

    public String getReSPONSABILIDADES() {
        return this.reSPONSABILIDADES;
    }

    public void setReSPONSABILIDADES(String reSPONSABILIDADES) {
        this.reSPONSABILIDADES = reSPONSABILIDADES;
    }

    public TbESTATUARIOEntity getIdEST() {
        return this.idEST;
    }

    public void setIdEST(TbESTATUARIOEntity tbESTATUARIO) {
        this.idEST = tbESTATUARIO;
    }

    public TbCURSOSEntity getIdCURSOS() {
        return this.idCURSOS;
    }

    public void setIdCURSOS(TbCURSOSEntity tbCURSOS) {
        this.idCURSOS = tbCURSOS;
    }

}
