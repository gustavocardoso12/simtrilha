package org.applicationn.simtrilhas.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;

@Entity(name="TbPONTCARGOS")
@Table(name="TB_PONT_CARGOS")
public class TbPONTCARGOSEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 20)
    @Column(length = 20, name="DESC_PONT")
    private String deSCPONT;

    @Column(name="PONTUACAO_ORIGINAL")
    @Digits(integer = 4, fraction = 0)
    private Integer poNTUACAOORIGINAL;
    
    @Column(name="DESCRICAO_PONT")
    private String descricaoCompleta;
    
    @Column(name="PESO")
    private Integer peso;
    
    public String getDeSCPONT() {
        return this.deSCPONT;
    }

    public void setDeSCPONT(String deSCPONT) {
        this.deSCPONT = deSCPONT;
    }

    public Integer getPoNTUACAOORIGINAL() {
        return this.poNTUACAOORIGINAL;
    }

    public void setPoNTUACAOORIGINAL(Integer poNTUACAOORIGINAL) {
        this.poNTUACAOORIGINAL = poNTUACAOORIGINAL;
    }

	public String getDescricaoCompleta() {
		return descricaoCompleta;
	}

	public void setDescricaoCompleta(String descricaoCompleta) {
		this.descricaoCompleta = descricaoCompleta;
	}

	public Integer getPeso() {
		return peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}

}
