package com.SneakySolo.RepairDock.service;

import com.SneakySolo.RepairDock.domain.bid.Bid;
import com.SneakySolo.RepairDock.domain.bid.BidStatus;
import com.SneakySolo.RepairDock.domain.rating.Rating;
import com.SneakySolo.RepairDock.domain.repairshop.RepairShop;
import com.SneakySolo.RepairDock.domain.request.Request;
import com.SneakySolo.RepairDock.domain.request.RequestStatus;
import com.SneakySolo.RepairDock.domain.user.Role;
import com.SneakySolo.RepairDock.domain.user.User;
import com.SneakySolo.RepairDock.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final RepairShopRepository repairShopRepository;
    private final BidRepository  bidRepository;
    private final SessionService sessionService;


    public RatingService(RatingRepository ratingRepository, RequestRepository requestRepository, UserRepository userRepository, RepairShopRepository repairShopRepository, BidRepository bidRepository, SessionService sessionService) {
        this.ratingRepository = ratingRepository;
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.repairShopRepository = repairShopRepository;
        this.bidRepository = bidRepository;
        this.sessionService = sessionService;
    }

    public Rating createRating (Long requestId,
                                Integer stars,
                                String comment,
                                HttpSession session) {

        sessionService.requiredRole(session, Role.CUSTOMER);
        Long customerId = sessionService.getCurrentUserId(session);

        Request req = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request Not Found"));

        if (!req.getRequestStatus().equals(RequestStatus.CLOSED)) {
            throw new RuntimeException("Request Status Not Closed so you cannot rate");
        }

        User user = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        if (!user.getId().equals(req.getCustomer().getId())) {
            throw new RuntimeException("You did not created the request so you cannot rate");
        }

        if (ratingRepository.existsByRequestId(requestId)) {
            throw new RuntimeException("Rating already exists for this request");
        }

        Bid acceptedBid = bidRepository
                .findByRequestIdAndStatus(requestId, BidStatus.ACCEPTED)
                .orElseThrow(() -> new RuntimeException("No accepted bid found"));

        RepairShop repairShop = acceptedBid.getRepairShop();

        Rating rating = new Rating();
        rating.setCustomer(user);
        rating.setComment(comment);
        rating.setStar(stars);
        rating.setRequest(req);
        rating.setRepairShop(repairShop);

        return  ratingRepository.save(rating);
    }

    public Double getAverageRating(Long shopId) {

        Double avg = ratingRepository.getAverageRatingByShopId(shopId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
    }
}
