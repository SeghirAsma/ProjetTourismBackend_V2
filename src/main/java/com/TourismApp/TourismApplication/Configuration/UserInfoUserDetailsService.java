package com.TourismApp.TourismApplication.Configuration;

import com.TourismApp.TourismApplication.Models.UserEntity;
import com.TourismApp.TourismApplication.Repositories.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserInfoUserDetailsService implements UserDetailsService {
    @Autowired
    private UserEntityRepository userEntityRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> userE = userEntityRepository.findByEmail(email);
       // return organisateur.map(UserInfoUserDetails::new)
           //     .orElseThrow(() -> new UsernameNotFoundException("user not found " + email));
        return userE.map(user -> {
            if (!user.isApproved() && !user.getRole().equalsIgnoreCase("admin")) {
                throw new DisabledException("Le compte n'est pas encore approuvé.");
            }
            return new UserInfoUserDetails(user);
        }).orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec le nom d'utilisateur: " + email));

    }
}
