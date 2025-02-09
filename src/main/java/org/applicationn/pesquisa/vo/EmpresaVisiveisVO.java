package org.applicationn.pesquisa.vo;

import java.io.Serializable;

public class EmpresaVisiveisVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private long visivel;
	private long idEmpresa;
	public long getVisivel() {
		return visivel;
	}
	public void setVisivel(long visivel) {
		this.visivel = visivel;
	}
	public long getIdEmpresa() {
		return idEmpresa;
	}
	public void setIdEmpresa(long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
