package com.paazl.app_init;

import com.paazl.data.CurrentBalance;
import com.paazl.data.Sheep;
import com.paazl.data.repositories.CurrentBalanceRepository;
import com.paazl.data.repositories.SheepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class InitialDataInjection implements ApplicationListener<ContextRefreshedEvent> {
    private final CurrentBalanceRepository currentBalanceRepository;

    private final SheepRepository sheepRepository;

    private final BigInteger initialBalance;

    private final Integer initialSheep;

    @Autowired
    public InitialDataInjection(
            CurrentBalanceRepository currentBalanceRepository,
            SheepRepository sheepRepository,
            @Value("${initial.balance}") BigInteger initialBalance,
            @Value("${initial.sheep}") Integer initialSheep) {
        this.currentBalanceRepository = currentBalanceRepository;
        this.sheepRepository = sheepRepository;
        this.initialBalance = initialBalance;
        this.initialSheep = initialSheep;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        setInitBalance();
        setInitSheep();
    }

    private void setInitSheep() {
        sheepRepository.deleteAll();
        sheepRepository.saveAll(
                Stream.generate(Sheep::new)
                    .limit(initialSheep)
                    .collect(Collectors.toList()));
    }

    private void setInitBalance() {
        currentBalanceRepository.deleteAll();
        currentBalanceRepository.save(new CurrentBalance(initialBalance));
    }
}