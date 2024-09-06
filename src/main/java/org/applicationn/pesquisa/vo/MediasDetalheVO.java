package org.applicationn.pesquisa.vo;
import java.io.Serializable;
import java.math.BigDecimal;

import org.applicationn.simtrilhas.domain.BaseEntity;

public class MediasDetalheVO extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    private String nomeCargoEmpresa;
    private String nomeEmpresa;
    private String codigoCargo;
    private BigDecimal mediaSb;
	public String getNomeCargoEmpresa() {
		return nomeCargoEmpresa;
	}
	public void setNomeCargoEmpresa(String nomeCargoEmpresa) {
		this.nomeCargoEmpresa = nomeCargoEmpresa;
	}
	public String getNomeEmpresa() {
		return nomeEmpresa;
	}
	public void setNomeEmpresa(String nomeEmpresa) {
		this.nomeEmpresa = nomeEmpresa;
	}
	public String getCodigoCargo() {
		return codigoCargo;
	}
	public void setCodigoCargo(String codigoCargo) {
		this.codigoCargo = codigoCargo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public BigDecimal getMediaSb() {
		return mediaSb;
	}
	public void setMediaSb(BigDecimal mediaSb) {
		this.mediaSb = mediaSb;
	}
  
    
}
