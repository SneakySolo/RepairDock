package com.SneakySolo.RepairDock.service;

import com.SneakySolo.RepairDock.domain.bid.Bid;
import com.SneakySolo.RepairDock.domain.bid.BidStatus;
import com.SneakySolo.RepairDock.domain.repairshop.RepairShop;
import com.SneakySolo.RepairDock.domain.request.Request;
import com.SneakySolo.RepairDock.domain.request.RequestStatus;
import com.SneakySolo.RepairDock.domain.user.Role;
import com.SneakySolo.RepairDock.domain.user.User;
import com.SneakySolo.RepairDock.repository.BidRepository;
import com.SneakySolo.RepairDock.repository.RepairShopRepository;
import com.SneakySolo.RepairDock.repository.RequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BidService {

    private final BidRepository bidRepository;
    private final RequestRepository requestRepository;
    private final RepairShopRepository repairShopRepository;

    public BidService(BidRepository bidRepository, RequestRepository requestRepository, RepairShopRepository repairShopRepository) {
        this.bidRepository = bidRepository;
        this.requestRepository = requestRepository;
        this.repairShopRepository = repairShopRepository;
    }

    @Transactional
    public Bid placeBid(Long requestId,
                        Long repairShopId,
                        double price,
                        int estimatedHours,
                        String message,
                        User currentUser) {

        if (currentUser.getRole() != Role.REPAIR_PERSON) {
            throw new RuntimeException ("only repair person can bid");
        }

        Request request = requestRepository.findById(requestId)
                .orElseThrow(() ->  new RuntimeException ("request not found"));

        RepairShop repairShop = repairShopRepository.findById(repairShopId)
                .orElseThrow(() -> new RuntimeException ("repair shop not found"));

        if (!repairShop.getOwner().getId().equals(currentUser.getId())) {
            throw new RuntimeException("you do not own the shop");
        }

        if (request.getRequestStatus().equals(RequestStatus.CLOSED)) {
            throw new RuntimeException ("request is already closed");
        }

        boolean alreadyPresent = bidRepository.existsByRequestIdAndRepairShopId(requestId, repairShopId);
        if (alreadyPresent) {
            throw new RuntimeException("you cannot bid twice");
        }

        Bid bid = new Bid();
        bid.setPrice(price);
        bid.setEstimatedHours(estimatedHours);
        bid.setMessage(message);
        bid.setRequest(request);
        bid.setRepairShop(repairShop);
        bid.setStatus(BidStatus.PENDING);

        return bidRepository.save(bid);
    }

    @Transactional
    public void acceptBid (Long bidId, Long customerId) {
        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new RuntimeException("Bid not found"));

        Request request = bid.getRequest();

        if (!request.getCustomer().getId().equals(customerId)) {
            throw new RuntimeException("you are not the request owner");
        }

        if (!request.getRequestStatus().equals(RequestStatus.OPEN)) {
            throw new RuntimeException ("request is not open");
        }

        bid.setStatus(BidStatus.ACCEPTED);
        List<Bid> allBids = bidRepository.findByRequestId(request.getId());

        for (Bid otherBid : allBids) {
            if (!otherBid.getId().equals(bidId)) {
                otherBid.setStatus(BidStatus.REJECTED);
            }
        }
        request.setRequestStatus(RequestStatus.ACCEPTED);
    }
}
