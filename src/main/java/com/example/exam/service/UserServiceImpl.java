package com.example.exam.service;

import com.example.exam.domain.entities.User;
import com.example.exam.domain.entities.UserRole;
import com.example.exam.domain.models.service.UserServiceModel;
import com.example.exam.errors.UserRegisterFailureException;
import com.example.exam.repository.RoleRepository;
import com.example.exam.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.exam.common.Constants.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
    }


    private void seedRolesInDb() {
        if (roleRepository.count() == 0) {
            roleRepository.save(new UserRole("ROLE_ROOT"));
            roleRepository.save(new UserRole("ROLE_USER"));
        }
    }

    private void setUserRole(User userEntity) {
        if (userRepository.count() == 0) {
            userEntity.setAuthorities(new HashSet<>(roleRepository.findAll()));
        } else {
            UserRole roleUser = roleRepository.findByAuthority("ROLE_USER").orElse(null);

            if (roleUser == null) {
                throw new IllegalArgumentException(NON_EXISTENT_ROLE);
            }

            userEntity.setAuthorities(new HashSet<>());
            userEntity.getAuthorities().add(roleUser);
        }
    }

    private Set<Long> getVisitedQuestionsAsSet(String questions) {
        if (questions == null || questions.isEmpty()) {
            return new HashSet<>();
        }

        return Arrays.stream(questions.split(", "))
                .map(Long::parseLong)
                .collect(Collectors.toSet());
    }

    private String getVisitedQuestionsAsString(Set<Long> questions, String visitedQuestions) {
        if (questions.isEmpty()) {
            return visitedQuestions;
        }

        String newQuestions = questions.stream()
                .map(question -> Long.toString(question))
                .collect(Collectors.joining(", "));

        return String.format("%s, %s", visitedQuestions, newQuestions);
    }

    @Override
    public UserDetails loadUserByUsername(String user) throws UsernameNotFoundException {
        UserDetails userDetails = userRepository.findByUsername(user).orElse(null);

        if (userDetails == null) {
            throw new UsernameNotFoundException(WRONG_NON_EXISTENT_USER);
        }

        return userDetails;
    }

    @Override
    public void registerUser(UserServiceModel userServiceModel) {
        seedRolesInDb();

        User user = modelMapper.map(userServiceModel, User.class);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        setUserRole(user);

        try {
            userRepository.save(user);
        } catch (Exception ex) {
            throw new UserRegisterFailureException(USER_REGISTER_EXCEPTION);
        }
    }

    @Override
    public UserServiceModel getUserByName(String name) {
        User userEntity = userRepository.findByUsername(name).orElse(null);

        if (userEntity == null) {
            throw new UsernameNotFoundException(WRONG_NON_EXISTENT_USER);
        }

        UserServiceModel userService = modelMapper.map(userEntity, UserServiceModel.class);
        userService.setVisitedQuestions(getVisitedQuestionsAsSet(userEntity.getVisitedQuestions()));

        return userService;
    }

    @Override
    public void updateVisitedQuestions(UserServiceModel userServiceModel, String visitedQuestions) {
        User user = modelMapper.map(userServiceModel, User.class);

        if (user == null) {
            throw new UsernameNotFoundException(WRONG_NON_EXISTENT_USER);
        }

        String visited = getVisitedQuestionsAsString(userServiceModel.getVisitedQuestions(), visitedQuestions);
        user.setVisitedQuestions(visited);

        userRepository.save(user);
    }

    @Override
    public void updateUser(String username) {
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            throw new UsernameNotFoundException(WRONG_NON_EXISTENT_USER);
        }

        user.setVisitedQuestions("");

        userRepository.save(user);
    }
}
