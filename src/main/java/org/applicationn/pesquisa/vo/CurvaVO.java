package org.applicationn.pesquisa.vo;
import java.io.Serializable;


import org.applicationn.simtrilhas.domain.BaseEntity;

public class CurvaVO extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


   private String empresa;
   private int grade;
   private int quantidade_cargos;
   private double media_sbm;
   private double media_sba;
   private double media_td;
   private double media_tda;
   private double media_rd;
   private double media_rda;
public String getEmpresa() {
	return empresa;
}
public void setEmpresa(String empresa) {
	this.empresa = empresa;
}
public int getGrade() {
	return grade;
}
public void setGrade(int grade) {
	this.grade = grade;
}
public int getQuantidade_cargos() {
	return quantidade_cargos;
}
public void setQuantidade_cargos(int quantidade_cargos) {
	this.quantidade_cargos = quantidade_cargos;
}
public double getMedia_sbm() {
	return media_sbm;
}
public void setMedia_sbm(double media_sbm) {
	this.media_sbm = media_sbm;
}
public double getMedia_sba() {
	return media_sba;
}
public void setMedia_sba(double media_sba) {
	this.media_sba = media_sba;
}
public double getMedia_td() {
	return media_td;
}
public void setMedia_td(double media_td) {
	this.media_td = media_td;
}
public double getMedia_tda() {
	return media_tda;
}
public void setMedia_tda(double media_tda) {
	this.media_tda = media_tda;
}
public double getMedia_rd() {
	return media_rd;
}
public void setMedia_rd(double media_rd) {
	this.media_rd = media_rd;
}
public double getMedia_rda() {
	return media_rda;
}
public void setMedia_rda(double media_rda) {
	this.media_rda = media_rda;
}
public static long getSerialversionuid() {
	return serialVersionUID;
}
   
    
    
}
