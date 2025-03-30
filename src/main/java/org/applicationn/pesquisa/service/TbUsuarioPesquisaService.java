package org.applicationn.pesquisa.service;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.applicationn.pesquisa.domain.TbEmpresa;
import org.applicationn.pesquisa.domain.TbMercado;
import org.applicationn.pesquisa.domain.TbUsuarioMercado;
import org.applicationn.simtrilhas.domain.TbGRADEEntity;
import org.applicationn.simtrilhas.domain.security.UserEntity;
import org.applicationn.simtrilhas.service.BaseService;
import org.applicationn.simtrilhas.service.security.SecurityWrapper;

@Named
public class TbUsuarioPesquisaService extends BaseService<TbUsuarioMercado> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public TbUsuarioPesquisaService() {
        super(TbUsuarioMercado.class);
    }
    
    @Transactional
    public List<TbUsuarioMercado> findByUsuario(UserEntity usuario) {
        return getEntityManagerMatriz()
                .createQuery("SELECT o FROM TbUsuarioMercado o WHERE o.empresa = :usuario", TbUsuarioMercado.class)
                .setParameter("usuario", usuario)
                .getResultList();
    }
    
    @Transactional
    public void deleteByUsuario(UserEntity usuario) {
        getEntityManagerMatriz()
                .createQuery("DELETE FROM TbUsuarioMercado o WHERE o.empresa = :usuario")
                .setParameter("usuario", usuario)
                .executeUpdate();
    }
    
    @Override
    @Transactional
    public TbUsuarioMercado save(TbUsuarioMercado usuario) {
        return super.save(usuario);
    }
    
    @Override
    @Transactional
    public TbUsuarioMercado update(TbUsuarioMercado tbGRADE) {
        return super.update(tbGRADE);
    }
    
    @Transactional
    public String getMercadoByUsuario(UserEntity usuario) {
        List<TbUsuarioMercado> mercados = findByUsuario(usuario);
        if (!mercados.isEmpty()) {
            return mercados.get(0).getMercado().getDescMercado();
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
	@Transactional
	public List<String> findMercadosDisponiveis(Long idUser) {
	    List<Object> resultados = getEntityManagerMatriz()
	        .createNativeQuery(
	            "SELECT tm.dsMercado " +
	            "FROM tb_usuario_mercado tum " +
	            "INNER JOIN users u ON u.id = tum.cd_usuario " +
	            "INNER JOIN tb_mercado tm ON tm.id = tum.cd_mercado " +
	            "WHERE u.id = :idUser")
	        .setParameter("idUser", idUser)
	        .getResultList();

	    return resultados.stream()
	                     .map(Object::toString)
	                     .collect(Collectors.toList());
	}
}