package login.prueba.security.controller;

import login.prueba.security.entity.Usuario;
import login.prueba.security.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin("*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Consultar todos los usuarios
    @GetMapping
    @PreAuthorize("hasAuthority('CONSULTA_USUARIO')")
    public ResponseEntity<List<Usuario>> consultarUsuarios() {
        List<Usuario> usuarios = usuarioService.findByAll();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    // Crear usuario
    @PostMapping
    @PreAuthorize("hasAuthority('CREAR_USUARIO_BD')")
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.save(usuario);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    // Actualizar usuario
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ACTUALIZA_USUARIO')")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Integer id, @RequestBody Usuario usuario) {
        Usuario usuarioExistente = usuarioService.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuarioExistente.setNombre(usuario.getNombre());
        usuarioExistente.setEmail(usuario.getEmail());
        usuarioExistente.setNombreUsuario(usuario.getNombreUsuario());
        Usuario usuarioActualizado = usuarioService.save(usuarioExistente);
        return new ResponseEntity<>(usuarioActualizado, HttpStatus.OK);
    }

    // Eliminar usuario
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ELIMINA_USUARIO')")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Integer id) {
        usuarioService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Consultar usuario por LDAP (Simulado)
    @GetMapping("/ldap/{username}")
    @PreAuthorize("hasAuthority('CONSULTA_USUARIO_LDAP')")
    public ResponseEntity<String> consultarUsuarioLdap(@PathVariable String username) {
        // Aquí se integraría con el servicio LDAP real.
        String resultadoSimulado = "Usuario encontrado en LDAP: " + username;
        return new ResponseEntity<>(resultadoSimulado, HttpStatus.OK);
    }

}
