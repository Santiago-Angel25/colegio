package usco.edu.co.parcial2.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import usco.edu.co.parcial2.model.Rol;
import usco.edu.co.parcial2.model.Usuario;
import usco.edu.co.parcial2.repository.UsuarioRepository;

@Service
public class JpaUsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public JpaUsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        String[] roles = usuario.getRoles().stream()
                .map(Rol::getName)
                .map(nombre -> nombre.replace("ROLE_", ""))
                .toArray(String[]::new);

        return User.withUsername(usuario.getUsername())
                .password(usuario.getPassword())
                .disabled(!Boolean.TRUE.equals(usuario.getEnabled()))
                .roles(roles)
                .build();
    }
}
