package alkemy.challenge.services;

import alkemy.challenge.dtos.UserDto;
import alkemy.challenge.entities.UserEntity;
import alkemy.challenge.exceptions.EmailExistsException;
import alkemy.challenge.repositories.ConfirmationTokenRepository;
import alkemy.challenge.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    ConfirmationTokenService confirmationTokenService;

    @Autowired
    EmailService emailService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    ModelMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }
        // El tercer parametro es una coleccion donde se especifica una serie de
        // autoridades
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

    @Transactional
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }

    public UserDto createUser(UserDto user) {
        if (userRepository.findByEmail(user.getEmail()) != null) throw new EmailExistsException("Email is already registered.");

        UserEntity userEntity = new UserEntity();
        // Encriptamos la password.
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setEmail(user.getEmail());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setEnabled(false);

        Optional<UserEntity> userSaved = Optional.of(save(userEntity));

        userSaved.ifPresent(u -> {
            try {
                String token = UUID.randomUUID().toString();
                confirmationTokenService.save(userSaved.get(), token);
                //send confirmation email
                emailService.sendHtmlMail(u);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return mapper.map(userSaved.get(), UserDto.class);
    }

    public UserDto getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }

        UserDto userToReturn = new UserDto();
        return mapper.map(userEntity, UserDto.class);
    }
}
