package com.SneakySolo.RepairDock.service;

import com.SneakySolo.RepairDock.domain.repairshop.RepairShop;
import com.SneakySolo.RepairDock.domain.user.User;
import com.SneakySolo.RepairDock.repository.RepairShopRepository;
import com.SneakySolo.RepairDock.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final RepairShopRepository repairShopRepository;

    public AdminService(UserRepository userRepository, RepairShopRepository repairShopRepository) {
        this.userRepository = userRepository;
        this.repairShopRepository = repairShopRepository;
    }

    public void diableUser (Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("user not found"));

        user.setEnabled(false);
        userRepository.save(user);
    }

    public void diableRepaieShop(Long id) {

        RepairShop shop = repairShopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("shop not found"));

        shop.setVerified(false);
        repairShopRepository.save(shop);
    }
}
