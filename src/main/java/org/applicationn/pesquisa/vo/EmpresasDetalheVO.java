package org.applicationn.pesquisa.vo;
import java.io.Serializable;
import java.math.BigDecimal;

import org.applicationn.simtrilhas.domain.BaseEntity;

public class EmpresasDetalheVO extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    private String nomeCargoEmpresa;
    private String nmSubFam;
    private String nmFamilia;
	public String getNomeCargoEmpresa() {
		return nomeCargoEmpresa;
	}
	public void setNomeCargoEmpresa(String nomeCargoEmpresa) {
		this.nomeCargoEmpresa = nomeCargoEmpresa;
	}
	public String getNmSubFam() {
		return nmSubFam;
	}
	public void setNmSubFam(String nmSubFam) {
		this.nmSubFam = nmSubFam;
	}
	public String getNmFamilia() {
		return nmFamilia;
	}
	public void setNmFamilia(String nmFamilia) {
		this.nmFamilia = nmFamilia;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
  
    
}
