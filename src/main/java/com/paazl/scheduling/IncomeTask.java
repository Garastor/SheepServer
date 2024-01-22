package com.paazl.scheduling;

import com.paazl.data.CurrentBalance;
import com.paazl.data.Sheep;
import com.paazl.data.State;
import com.paazl.data.repositories.CurrentBalanceRepository;
import com.paazl.data.repositories.SheepRepository;
import com.paazl.util.CurrentBalanceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Component
public class IncomeTask {
    private final CurrentBalanceRepository currentBalanceRepository;

    private final SheepRepository sheepRepository;

    private final BigInteger yieldPerSheep;

    @Autowired
    public IncomeTask(
            CurrentBalanceRepository currentBalanceRepository,
            SheepRepository sheepRepository,
            @Value("${sheep.yield}") BigInteger yieldPerSheep) {
        this.currentBalanceRepository = currentBalanceRepository;
        this.sheepRepository = sheepRepository;
        this.yieldPerSheep = yieldPerSheep;
    }

    @Scheduled(cron = "${scheduling.income}")
    @Transactional
    public void updateCurrentBalance() {
        CurrentBalance currentBalance;
        if ((currentBalance = currentBalanceRepository.findFirstByOrderByTimestampDesc()) == null) {
            return;
        }

        List<Sheep> sheep = sheepRepository.findAllByStateNot(State.DEAD);

        currentBalanceRepository.save(
                CurrentBalanceUtils.addBalance(
                        currentBalance,
                        yieldPerSheep.multiply(BigInteger.valueOf(sheep.size()))));
    }
}
