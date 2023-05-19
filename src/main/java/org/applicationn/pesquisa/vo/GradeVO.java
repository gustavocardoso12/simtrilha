package org.applicationn.pesquisa.vo;
import java.io.Serializable;


import org.applicationn.simtrilhas.domain.BaseEntity;

public class GradeVO extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    private int gradeMinimoEmpresa;
    private int gradeMaximoEmpresa;
    
    
	public int getGradeMinimoEmpresa() {
		return gradeMinimoEmpresa;
	}
	public void setGradeMinimoEmpresa(int gradeMinimoEmpresa) {
		this.gradeMinimoEmpresa = gradeMinimoEmpresa;
	}
	public int getGradeMaximoEmpresa() {
		return gradeMaximoEmpresa;
	}
	public void setGradeMaximoEmpresa(int gradeMaximoEmpresa) {
		this.gradeMaximoEmpresa = gradeMaximoEmpresa;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

    
    
}
