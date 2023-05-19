package org.applicationn.pesquisa.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.applicationn.pesquisa.service.TbCurvaService;
import org.applicationn.pesquisa.vo.CurvaVO;


@Named("tbCurvaBean")
@ViewScoped
public class TbCurvaBean  implements Serializable  {
	private static final long serialVersionUID = 1L;
	
	@Inject
	private TbCurvaService tbCurvaService;

	
	public void createRegressaoTeste() {
		List<CurvaVO> lista = new ArrayList<CurvaVO>();
		lista = tbCurvaService.findCurvaMercados("Mercado AptaXR");
		
	}


	public TbCurvaService getTbCurvaService() {
		return tbCurvaService;
	}


	public void setTbCurvaService(TbCurvaService tbCurvaService) {
		this.tbCurvaService = tbCurvaService;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
