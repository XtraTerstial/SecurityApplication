package com.xtraCoder.SecurityApp.SecurityApplication.Service;

import com.xtraCoder.SecurityApp.SecurityApplication.dto.*;
import com.xtraCoder.SecurityApp.SecurityApplication.entities.User;
import com.xtraCoder.SecurityApp.SecurityApplication.exception.*;
import com.xtraCoder.SecurityApp.SecurityApplication.repository.*;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username)
                .orElseThrow(()-> new BadCredentialsException("User with email "+ username +" doesn't exist"));
    }

    public User getUserById(Long userId){
        return userRepo.findById(userId)
                .orElseThrow(() -> new ResourseNotFoundException("User with id "+ userId +" doesn't exist"));
    }

    public UserDto signUp(SignUpDto signUpDto) {
        Optional<User> user = userRepo.findByEmail(signUpDto.getEmail());
        if(user.isPresent()){
            throw new EmailAlreadyExistsException("User with email already exists " + signUpDto.getEmail());
        }
        User toBeCreatedUser = modelMapper.map(signUpDto, User.class);
        toBeCreatedUser.setPassword(passwordEncoder.encode(toBeCreatedUser.getPassword()));
        User savedUser = userRepo.save(toBeCreatedUser);
        return modelMapper.map(savedUser, UserDto.class);
    }

}
