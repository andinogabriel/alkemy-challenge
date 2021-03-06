package alkemy.challenge.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import alkemy.challenge.SpringApplicationContext;
import alkemy.challenge.dtos.UserDto;
import alkemy.challenge.models.requests.UserLoginRequest;
import alkemy.challenge.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter  {

    private final AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            //De esta manera copiamos todas las propiedades que vienen de la request a la clase UserLoginRequestModel, lo convertimos a un objeto de java
            UserLoginRequest userModel = new ObjectMapper()
                    .readValue(request.getInputStream(), UserLoginRequest.class);

            //Trata loguear
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userModel.getEmail(),
                            userModel.getPassword(),
                            new ArrayList<>()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Si el logueo es correcto se llama a este metodo y se crea el token y se puede usar el token en los otros endpoints
    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException
    {
        //Casteamos para acceder al metodo, en el username va a estar nuestro email
        String username = ((User) authentication.getPrincipal()).getUsername();

        //Le sumamos los milisegundos del dia actual nuestro expiration date, creamos el token que vamos a estar usando como autenticacion del cliente
        String token = Jwts.builder().setSubject(username).setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_DATE)).signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret()).compact();

        //Agregamos el id publi co al header. Le implementamos a la clase UserService pero en el metodo la primer letra siempre va con minuscula
        UserService userService = (UserService) SpringApplicationContext.getBean("userService");
        UserDto userDto = userService.getUser(username); //Con esto tenemos acceso a los datos del usuario

        //A parte de los headers por defecto se mandan estos tambien
        response.addHeader("Access-Control-Expose-headers", "Authorization, UserId, FirstName, LastName");
        //Agregamos al header el id publico
        response.addHeader("FirstName", userDto.getFirstName());
        response.addHeader("LastName", userDto.getLastName());

        //Agregamos un header a la response
        response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
    }


}
