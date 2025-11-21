package com.digis01.ISanchezProgramacionNCapasSeptiembre2025.ML;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.sql.Blob;
import java.util.Date;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

public class Usuario {

    private int IdUsuario;
    @NotNull(message = "Campo no puede ser nulo")
    @NotBlank(message = "Campo debe contener datos")
    @Size(min = 2, max = 17, message = "Entre 2 y 17")
    private String NombreUsuario;
    @NotNull(message = "El apellido paterno no puede ser nulo")
    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(min = 2, max = 20, message = "El apellido paterno debe tener entre 2 y 20 caracteres")
    private String ApellidoPatUsuario;

    @Size(max = 20, message = "El apellido materno no debe exceder 20 caracteres")
    private String ApellidoMatUsuario;

    @NotNull(message = "La contraseña no puede ser nula")
    @NotBlank(message = "La contraseña es obligatoria")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "La contraseña debe tener al menos una mayúscula, un número, un carácter especial, y mínimo 8 caracteres")
    private String PasswordUser;

    @NotNull(message = "La fecha de nacimiento no puede ser nula")
    @Past(message = "La fecha de nacimiento debe ser anterior a la fecha actual")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date FechaNacimiento;

    private String StatusUsuario;

    private Date FechaModificacion;

    private String FotoUsuario;

    @NotNull(message = "El nombre de usuario no puede ser nulo")
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]+$", message = "El nombre de usuario no es valido")
    private String UserName;

    @NotNull(message = "El correo no puede ser nulo")
    @NotBlank(message = "El correo es obligatorio")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message="el correo electrónico ingresado tiene un formato incorrecto")
    private String EmailUsuario;

    @NotNull(message = "El sexo no puede ser nulo")
    @NotBlank(message = "El sexo no puede estar vacio")
    @Size(max = 1, message = "El sexo no puede tener mas de un carácter")
    private String SexoUsuario;

    @NotNull(message = "El teléfono no puede ser nulo")
    @NotBlank(message = "El teléfono no puede estar vacio")
    @Size(max = 20, message = "El telefóno ha sobrepasado los caracteres")
    private String TelefonoUsuario;

    @Size(max = 20, message = "El celular ha sobrepasado el número caracteres")
    private String CelularUsuario;

    @Size(max = 50, message = "El celular ha sobrepasado el número caracteres")
    private String CurpUsuario;

    @NotNull(message = "El rol del usuario es obligatorio")
    @JsonProperty("RolJPA")
    public Rol Rol;
    
    @JsonProperty("DireccionesJPA")
    public List<Direccion> Direcciones;

    public Usuario() {

    }

    public Usuario(
            String NombreUsuario, String ApellidoPatUsuario, String ApellidoMatUsuario, String PasswordUser, Date FechaNacimiento,
            String StatusUsuario, Date FechaModificacion, String FotoUsuario, String UserName, String EmailUsuario, String SexoUsuario,
            String TelefonoUsuario, String CelularUsuario, String CurpUsuario
    ) {
        this.NombreUsuario = NombreUsuario;
        this.ApellidoPatUsuario = ApellidoPatUsuario;
        this.ApellidoMatUsuario = ApellidoMatUsuario;
        this.PasswordUser = PasswordUser;
        this.FechaNacimiento = FechaNacimiento;
        this.StatusUsuario = StatusUsuario;
        this.FechaModificacion = FechaModificacion;
        this.FotoUsuario = FotoUsuario;
        this.UserName = UserName;
        this.EmailUsuario = EmailUsuario;
        this.SexoUsuario = SexoUsuario;
        this.TelefonoUsuario = TelefonoUsuario;
        this.CelularUsuario = CelularUsuario;
        this.CurpUsuario = CurpUsuario;
    }

    public void setIdUsuario(int IdUsuario) {
        this.IdUsuario = IdUsuario;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setNombreUsuario(String NombreUsuario) {
        this.NombreUsuario = NombreUsuario;
    }

    public String getNombreUsuario() {
        return NombreUsuario;
    }

    public void setApellidoPatUsuario(String ApellidoPatUsuario) {
        this.ApellidoPatUsuario = ApellidoPatUsuario;
    }

    public String getApellidoPatUsuario() {
        return ApellidoPatUsuario;
    }

    public void setApellidoMatUsuario(String ApellidoMatUsuario) {
        this.ApellidoMatUsuario = ApellidoMatUsuario;
    }

    public String getApellidoMatUsuario() {
        return ApellidoMatUsuario;
    }

    public void setPasswordUser(String PasswordUser) {
        this.PasswordUser = PasswordUser;
    }

    public String getPasswordUser() {
        return PasswordUser;
    }

    public void setFechaNacimiento(Date FechaNacimiento) {
        this.FechaNacimiento = FechaNacimiento;
    }

    public Date getFechaNacimiento() {
        return FechaNacimiento;
    }

    public void setStatusUsuario(String StatusUsuario) {
        this.StatusUsuario = StatusUsuario;
    }

    public String getStatusUsuario() {
        return StatusUsuario;
    }

    public void setFechaModificacion(Date FechaModificacion) {
        this.FechaModificacion = FechaModificacion;
    }

    public Date getFechaModificacion() {
        return FechaModificacion;
    }

    public void setFotoUsuario(String FotoUsuario) {
        this.FotoUsuario = FotoUsuario;
    }

    public String getFotoUsuario() {
        return FotoUsuario;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setEmailUsuario(String EmailUsuario) {
        this.EmailUsuario = EmailUsuario;
    }

    public String getEmailUsuario() {
        return EmailUsuario;
    }

    public void setSexoUsuario(String SexoUsuario) {
        this.SexoUsuario = SexoUsuario;
    }

    public String getSexoUsuario() {
        return SexoUsuario;
    }

    public void setTelefonoUsuario(String TelefonoUsuario) {
        this.TelefonoUsuario = TelefonoUsuario;
    }

    public String getTelefonoUsuario() {
        return TelefonoUsuario;
    }

    public void setCelularUsuario(String CelularUsuario) {
        this.CelularUsuario = CelularUsuario;
    }

    public String getCelularUsuario() {
        return CelularUsuario;
    }

    public void setCurpUsuario(String CurpUsuario) {
        this.CurpUsuario = CurpUsuario;
    }

    public String getCurpUsuario() {
        return CurpUsuario;
    }

    public void setRol(Rol rol) {
        this.Rol = rol;
    }

    public Rol getRol() {
        return Rol;
    }

    public void setDirecciones(List<Direccion> Direcciones) {
        this.Direcciones = Direcciones;
    }

    public List<Direccion> getDirecciones() {
        return Direcciones;
    }
}
