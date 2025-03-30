package org.applicationn.pesquisa.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.pesquisa.domain.TbMercado;
import org.applicationn.simtrilhas.service.BaseService;

@Named
public class TbMercadoService extends BaseService<TbMercado> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public TbMercadoService() {
        super(TbMercado.class);
    }
    
    @Transactional
    public List<TbMercado> findAllTbMercado() {
        return getEntityManagerMatriz()
                .createQuery("SELECT o FROM TbMercado o ORDER BY o.descMercado", TbMercado.class)
                .getResultList();
    }
    
    @Transactional
    public TbMercado findByDescricao(String descricao) {
        try {
            return getEntityManagerMatriz()
                    .createQuery("SELECT o FROM TbMercado o WHERE o.descMercado = :descricao", TbMercado.class)
                    .setParameter("descricao", descricao)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}