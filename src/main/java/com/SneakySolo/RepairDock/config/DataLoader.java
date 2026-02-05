package com.SneakySolo.RepairDock.config;

import com.SneakySolo.RepairDock.entity.Role;
import com.SneakySolo.RepairDock.entity.User;
import com.SneakySolo.RepairDock.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadUserTest (UserRepository userRepository) {
        return args -> {
            if (userRepository.findByEmail("alfred@rediffmail.com").isEmpty()) {
                User newAdminUser  = new User();
                    newAdminUser.setFullName("Alfred PennyWorth");
                    newAdminUser.setEmail("alfred@rediffmail.com");
                    newAdminUser.setPhoneNumber("7003598701");
                    newAdminUser.setPassword("Pennyworth");
                    newAdminUser.setRole(Role.ADMIN);

                userRepository.save(newAdminUser);
                System.out.println("Admin Added");
            }
        };
    }
}
