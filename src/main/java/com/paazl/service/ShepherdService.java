package com.paazl.service;

import com.paazl.data.CurrentBalance;
import com.paazl.data.Sheep;
import com.paazl.data.State;
import com.paazl.data.repositories.CurrentBalanceRepository;
import com.paazl.data.repositories.SheepRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ShepherdService {
    private final SheepRepository sheepRepository;

    private final CurrentBalanceRepository currentBalanceRepository;

    @SuppressWarnings("unused")
    private final Integer priceOfSheep;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public ShepherdService(
            SheepRepository sheepRepository,
            CurrentBalanceRepository currentBalanceRepository,
            @Value("${sheep.price}") Integer priceOfSheep) {
        this.sheepRepository = sheepRepository;
        this.currentBalanceRepository = currentBalanceRepository;
        this.priceOfSheep = priceOfSheep;
    }

    @Transactional
    public String orderNewSheep(int nofSheepDesired) {
        // TODO Implement sheep ordering feature
        // TODO Write unit tests

        if (nofSheepDesired <= 0) {
            return "Number of sheep must be 1 or more";
        }

        BigInteger totalPriceOfSheep = BigInteger.valueOf(priceOfSheep.longValue() * nofSheepDesired);
        CurrentBalance currentBalance = getBalance();

        int purchasePossibility = currentBalance
                .getBalance()
                .compareTo(totalPriceOfSheep);

        if (purchasePossibility >= 0) {
            currentBalance.setBalance(currentBalance.getBalance().subtract(totalPriceOfSheep));
            List<Sheep> newSheep = IntStream.range(0, nofSheepDesired)
                    .mapToObj(i -> new Sheep())
                    .collect(Collectors.toList());
            currentBalanceRepository.save(currentBalance);
            sheepRepository.saveAll(newSheep);
            log.info(String.format("%s sheep were ordered", nofSheepDesired));
            return String.format("In total %s sheep were ordered and added to your flock! %s", nofSheepDesired, getStatus());
        } else {
            return String.format("Insufficient balance to order sheep. %s", getStatus());
        }

    }

    private String getStatus() {
        BigInteger currentBalance = getBalance().getBalance();
        FlockState flockState = getFlockState();
        int healthySheepNum = flockState.getNumberOfHealthySheep();
        int deadSheepNum = flockState.getNumberOfDeadSheep();
        return String.format("\nBalance: %d, number of sheep healthy and dead: [%d, %d]",
                currentBalance,
                healthySheepNum,
                deadSheepNum);
    }

    private FlockState getFlockState() {
        List<Sheep> healthySheep = sheepRepository.findAllByState(State.HEALTHY);
        List<Sheep> deadSheep = sheepRepository.findAllByState(State.DEAD);

        return new FlockState(healthySheep.size(), deadSheep.size());
    }

    private CurrentBalance getBalance() {
        return currentBalanceRepository.findFirstByOrderByTimestampDesc();
    }

}
