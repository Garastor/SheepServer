package com.paazl.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.paazl.data.State;
import com.paazl.data.repositories.CurrentBalanceRepository;
import com.paazl.data.repositories.SheepRepository;

@Component
public class StatusTask {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final ConfigurableApplicationContext context;

    private final SheepRepository sheepRepository;

    private final CurrentBalanceRepository currentBalanceRepository;

    @Autowired
    public StatusTask(
            ConfigurableApplicationContext context,
            SheepRepository sheepRepository,
            CurrentBalanceRepository currentBalanceRepository) {
        this.context = context;
        this.sheepRepository = sheepRepository;
        this.currentBalanceRepository = currentBalanceRepository;
    }

    @Scheduled(cron = "${scheduling.status}")
    @Transactional
    public void checkSheepStates() {
        int aliveSheep = sheepRepository.countByState(State.HEALTHY);
        int deadSheep = sheepRepository.countByState(State.DEAD);

        if (checkSheepRemaining(aliveSheep)) {
            return;
        }

        log.info("Balance: {}, number of sheep healthy and dead: [{}, {}]",
                currentBalanceRepository.findFirstByOrderByTimestampDesc().getBalance(),
                aliveSheep,
                deadSheep);
    }

    private boolean checkSheepRemaining(int aliveSheep) {
        if (aliveSheep == 0) {
            printExitStatement();
            context.close();
            return true;
        }

        return false;
    }

    private void printExitStatement() {
        log.info("");
        log.info("*********************************************************");
        log.info("* You lost! All sheep died, the shepherd goes bankrupt! *");
        log.info("*********************************************************");
        log.info("");
    }
}
