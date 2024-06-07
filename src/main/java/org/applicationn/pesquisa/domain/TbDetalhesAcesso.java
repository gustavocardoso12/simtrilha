package org.applicationn.pesquisa.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.applicationn.simtrilhas.domain.BaseEntity;

@Entity(name="TbDetalhesAcesso")
@Table(name="TB_DETALHE_ACESSOS")
public class TbDetalhesAcesso extends BaseEntity implements Serializable {


	private static final long serialVersionUID = 1L;


	@Column(name = "data_acesso")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAcesso;

	@Column(name="tipoDeAtividade")
	@NotNull
	private String tipoDeAtividade;

	@Column(name="id_user")
	@NotNull
	private Long id_user;
	
	@Column(name="mesAno")
	@NotNull
	private String mesAno;
	

	public Long getId_user() {
		return id_user;
	}

	public void setId_user(Long id_user) {
		this.id_user = id_user;
	}

	public Date getDataAcesso() {
		return dataAcesso;
	}

	public void setDataAcesso(Date dataAcesso) {
		this.dataAcesso = dataAcesso;
	}

	public String getTipoDeAtividade() {
		return tipoDeAtividade;
	}

	public void setTipoDeAtividade(String tipoDeAtividade) {
		this.tipoDeAtividade = tipoDeAtividade;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getMesAno() {
		return mesAno;
	}

	public void setMesAno(String mesAno) {
		this.mesAno = mesAno;
	}

}
