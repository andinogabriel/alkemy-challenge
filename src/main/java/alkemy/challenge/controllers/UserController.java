package alkemy.challenge.controllers;

import alkemy.challenge.dtos.UserDto;
import alkemy.challenge.models.requests.RegisterUserRequest;
import alkemy.challenge.models.responses.UserResponse;
import alkemy.challenge.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/auth")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ModelMapper mapper;


    @PostMapping(path = "/register")
    public UserResponse createUser (@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        UserDto userDto = mapper.map(registerUserRequest, UserDto.class);
        UserDto userRegistered = userService.createUser(userDto);
        return mapper.map(userRegistered, UserResponse.class);
    }


}
