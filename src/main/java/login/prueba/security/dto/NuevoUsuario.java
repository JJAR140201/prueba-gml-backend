package login.prueba.security.dto;

import java.util.HashSet;
import java.util.Set;

public class NuevoUsuario {

    private String nombre;

    private String nombreUsuario;

    private String emails;

    private String password;

    private Set<String> roles = new HashSet<>();

    @Override
    public String toString() {
        return "NuevoUsuario{" +
                "nombre='" + nombre + '\'' +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", emails='" + emails + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }

    public NuevoUsuario() {
    }

    public NuevoUsuario(String nombre, String nombreUsuario, String emails, String password, Set<String> roles) {
        this.nombre = nombre;
        this.nombreUsuario = nombreUsuario;
        this.emails = emails;
        this.password = password;
        this.roles = roles;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
