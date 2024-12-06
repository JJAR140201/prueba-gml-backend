package login.prueba.security.controller;

import login.prueba.security.dto.JwtDto;
import login.prueba.security.dto.LoginUsuario;
import login.prueba.security.dto.NuevoUsuario;
import login.prueba.security.entity.Usuario;
import login.prueba.security.entity.Rol;
import login.prueba.security.enums.RolNombre;
import login.prueba.security.service.UsuarioService;
import login.prueba.security.service.RolService;
import login.prueba.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolService rolService;

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@RequestBody LoginUsuario loginUsuario) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUsuario.getNombreUsuario(),
                        loginUsuario.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);

        JwtDto jwtDto = JwtDto.builder()
                .token(jwt)
                .nombreUsuario(loginUsuario.getNombreUsuario())
                .authorities(authentication.getAuthorities())
                .build();

        return new ResponseEntity<>(jwtDto, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody NuevoUsuario nuevoUsuario) {
        if (usuarioService.existsByNombreUsuario(nuevoUsuario.getNombreUsuario())) {
            return new ResponseEntity<>("El nombre de usuario ya existe", HttpStatus.BAD_REQUEST);
        }
        if (usuarioService.existsByEmail(nuevoUsuario.getEmails())) {
            return new ResponseEntity<>("El email ya está registrado", HttpStatus.BAD_REQUEST);
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(nuevoUsuario.getNombre());
        usuario.setNombreUsuario(nuevoUsuario.getNombreUsuario());
        usuario.setEmail(nuevoUsuario.getEmails());
        usuario.setPassword(new BCryptPasswordEncoder().encode(nuevoUsuario.getPassword()));

        Set<Rol> roles = new HashSet<>();
        nuevoUsuario.getRoles().forEach(rol -> {
            Rol nuevoRol = rolService.findByRolNombre(RolNombre.valueOf(rol))
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rol));
            roles.add(nuevoRol);
        });
        usuario.setRoles(roles);

        usuarioService.save(usuario);

        return new ResponseEntity<>("Usuario registrado con éxito", HttpStatus.CREATED);
    }
}
