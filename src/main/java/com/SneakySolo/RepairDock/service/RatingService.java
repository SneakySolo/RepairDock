package com.SneakySolo.RepairDock.service;

import com.SneakySolo.RepairDock.domain.bid.Bid;
import com.SneakySolo.RepairDock.domain.bid.BidStatus;
import com.SneakySolo.RepairDock.domain.rating.Rating;
import com.SneakySolo.RepairDock.domain.repairshop.RepairShop;
import com.SneakySolo.RepairDock.domain.request.Request;
import com.SneakySolo.RepairDock.domain.request.RequestStatus;
import com.SneakySolo.RepairDock.domain.user.User;
import com.SneakySolo.RepairDock.repository.*;
import org.springframework.stereotype.Service;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final RepairShopRepository repairShopRepository;
    private final BidRepository  bidRepository;


    public RatingService(RatingRepository ratingRepository, RequestRepository requestRepository, UserRepository userRepository, RepairShopRepository repairShopRepository, BidRepository bidRepository) {
        this.ratingRepository = ratingRepository;
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.repairShopRepository = repairShopRepository;
        this.bidRepository = bidRepository;
    }

    public Rating createRating (Long requestId,
                                Integer stars,
                                String comment,
                                Long customerId) {

        Request req = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request Not Found"));

        if (!req.getRequestStatus().equals(RequestStatus.CLOSED)) {
            throw new RuntimeException("Request Status Not Closed so you cannot rate");
        }

        User user = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Request Not Found"));

        if (!user.getId().equals(req.getCustomer().getId())) {
            throw new RuntimeException("You did not created the request so you cannot rate");
        }

        if (ratingRepository.existsByRequestId(requestId)) {
            throw new RuntimeException("Rating already exists for this request");
        }

        if (stars == null || stars < 1 || stars > 5) {
            throw new RuntimeException("Stars must be between 1 and 5");
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
}
