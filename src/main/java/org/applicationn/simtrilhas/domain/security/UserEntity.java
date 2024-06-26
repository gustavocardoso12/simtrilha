package org.applicationn.simtrilhas.domain.security;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.applicationn.pesquisa.domain.TbEmpresa;
import org.applicationn.pesquisa.domain.TbPesquisa;
import org.applicationn.simtrilhas.domain.BaseEntity;

@Entity(name="User")
@Table(name="\"USERS\"")
public class UserEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 50)
    @NotNull
    private String username;

    @Size(max = 255)
    @NotNull
    private String password;

    @Size(max = 255)
    @NotNull
    private String salt;
    
    @Size(max = 100)
    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    @NotNull
    private String email;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;

    // unique hash for account activation link
    @Size(max = 255)
    private String emailConfirmationKey;
    
    // unique hash for reset password link
    @Size(max = 255)
    private String emailResetPasswordKey;

    // date of generating reset password link
    @Temporal(TemporalType.TIMESTAMP)
    private Date passwordResetDate;
    
    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = { @JoinColumn(name = "USER_ID") })
    @Column(name = "user_role")
    private List<UserRole> roles;
    

    @ManyToOne(optional=true)
    @JoinColumn(name = "id_empresa", referencedColumnName = "id")
    private TbEmpresa idEmpresa;

    
    @Enumerated(EnumType.STRING)
    @NotNull
    private UserStatus status;
    
    
    
    
    @Size(max = 50)
    @NotNull
    private String theme;
    
    @Size(max = 50)
    private String banco_dados;
    
    @Size(max = 50)
    private String flag_pessoa;
    
    @Size(max=50)
    private String flag_grade;
    
    @Size(max=50)
    private String ativo;
    
    @Size(max=50)
    private String sistema;
    
    @Size(max=50)
    private String mercado;
    
    private String privilegio_acesso;
    
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getEmailConfirmationKey() {
        return emailConfirmationKey;
    }

    public void setEmailConfirmationKey(String emailConfirmationKey) {
        this.emailConfirmationKey = emailConfirmationKey;
    }

    public String getEmailResetPasswordKey() {
        return emailResetPasswordKey;
    }

    public void setEmailResetPasswordKey(String emailResetPasswordKey) {
        this.emailResetPasswordKey = emailResetPasswordKey;
    }

    public Date getPasswordResetDate() {
        return passwordResetDate;
    }

    public void setPasswordResetDate() {
        this.passwordResetDate = new Date();
    }
    
    public List<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRole> roles) {
        this.roles = roles;
    }

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getBanco_dados() {
		return banco_dados;
	}

	public void setBanco_dados(String banco_dados) {
		this.banco_dados = banco_dados;
	}

	public String getFlag_pessoa() {
		return flag_pessoa;
	}

	public void setFlag_pessoa(String flag_pessoa) {
		this.flag_pessoa = flag_pessoa;
	}

	public String getFlag_grade() {
		return flag_grade;
	}

	public void setFlag_grade(String flag_grade) {
		this.flag_grade = flag_grade;
	}

	public String getAtivo() {
		return ativo;
	}

	public String getPrivilegio_acesso() {
		return privilegio_acesso;
	}

	public void setPrivilegio_acesso(String privilegio_acesso) {
		this.privilegio_acesso = privilegio_acesso;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setPasswordResetDate(Date passwordResetDate) {
		this.passwordResetDate = passwordResetDate;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

	public String getSistema() {
		return sistema;
	}

	public void setSistema(String sistema) {
		this.sistema = sistema;
	}

	public String getMercado() {
		return mercado;
	}

	public void setMercado(String mercado) {
		this.mercado = mercado;
	}

	public TbEmpresa getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(TbEmpresa idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

}
