package org.applicationn.pesquisa.service;

import java.io.Serializable;

import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Named;
import javax.persistence.Query;

import org.applicationn.pesquisa.domain.TbPesquisa;
import org.applicationn.simtrilhas.service.BaseService;

@Named
@TransactionManagement(TransactionManagementType.BEAN)
public class TbPesquisaServiceNew extends BaseService<TbPesquisa> implements Serializable {

	private static final long serialVersionUID = 1L;

	public TbPesquisaServiceNew(){
		super(TbPesquisa.class);
	}
	
	
}
