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

@Entity(name="TbPesquisaCurva")
@Table(name="TB_PESQUISA_CURVA")
public class TbPesquisaCurva extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    @Column(name="nome_empresa")
    private String descEmpresa;

    @Column(name="grade")
    private int Grade;
    
    @Column(name="quantidade_cargos")
    private int quantidadeCargos;
    
    public String getDescEmpresa() {
		return descEmpresa;
	}

	public void setDescEmpresa(String descMercado) {
		this.descEmpresa = descMercado;
	}

	public int getGrade() {
		return Grade;
	}

	public void setGrade(int grade) {
		Grade = grade;
	}

	public int getQuantidadeCargos() {
		return quantidadeCargos;
	}

	public void setQuantidadeCargos(int quantidadeCargos) {
		this.quantidadeCargos = quantidadeCargos;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

    
}
