package med.voll.api.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Indica aos Spring que está classe é genérica, não tem um objetivo específico
public class SecurityFilter extends OncePerRequestFilter { // Garante que o filtro será executado uma vez por requisição

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Lógica para recuperar o token
        var tokenJWT = recuperarToken(request); // O token está no heador ("cabecalho"), e as informações do header estão na variável request

        //validação do Token e recuperar subject, que contém o login
        if(tokenJWT != null) {
            var subject = tokenService.getSubject(tokenJWT);
            var usuario = repository.findByLogin(subject);

            var  authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // A ideia é: quando um filtro termina, ele precisa chamar o próximo filtro
        // É dessa forma que chama-se o próximo filtro
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization"); // Se não existir o cabeçalho é devolvido null

        if(authorizationHeader != null){
            return authorizationHeader.replace("Bearer ", "");
        }

        return null;
    }
}
