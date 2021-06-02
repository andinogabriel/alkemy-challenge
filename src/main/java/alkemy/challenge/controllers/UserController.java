package alkemy.challenge.controllers;

import alkemy.challenge.dtos.UserDto;
import alkemy.challenge.entities.ConfirmationTokenEntity;
import alkemy.challenge.entities.UserEntity;
import alkemy.challenge.models.requests.RegisterUserRequest;
import alkemy.challenge.models.responses.UserResponse;
import alkemy.challenge.services.ConfirmationTokenService;
import alkemy.challenge.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ConfirmationTokenService confirmationTokenService;

    @Autowired
    ModelMapper mapper;


    @PostMapping(path = "/register")
    public UserResponse createUser (@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        UserDto userDto = userService.createUser(mapper.map(registerUserRequest, UserDto.class));
        return mapper.map(userDto, UserResponse.class);
    }

    @GetMapping(path = "/activation")
    public String confirmation(@RequestParam("token") String token, Model model) {
        ConfirmationTokenEntity confirmationTokenEntity = confirmationTokenService.findByToken(token);
        if(token == null) {
            model.addAttribute("message", "Your confirmation token is invalid.");
        } else {
            UserEntity userEntity = confirmationTokenEntity.getUser();
            //if the user account isn't activated
            if(!userEntity.isEnabled()) {
                Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
                //check if the token is expired
                if(confirmationTokenEntity.getExpiryDate().before(currentTimestamp)) {
                    model.addAttribute("message", "Your confirmation token has expired.");
                } else {
                    //token is valid so activate the user account
                    userEntity.setEnabled(true);
                    userService.save(userEntity);
                    model.addAttribute("message", "Your account is successfully activated.");
                }
            } else {
                //User account is already activated
                model.addAttribute("message", "Your account is already activated.");
            }
        }

        return "Confirmation email: " + model.getAttribute("message");
    }


}
