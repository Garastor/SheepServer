package com.paazl.scheduling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.paazl.data.State;
import com.paazl.data.repositories.SheepRepository;

@Component
public class FlockTask {
    private final SheepRepository sheepRepository;

    private final double perishRate;

    @Autowired
    public FlockTask(
            SheepRepository sheepRepository,
            @Value("${sheep.perishRate}") double perishRate) {
        this.sheepRepository = sheepRepository;
        this.perishRate = perishRate;
    }

    @Scheduled(cron = "${scheduling.flock}")
    @Transactional
    public void updateSheepStates() {
        sheepRepository.findAllByState(State.HEALTHY).stream()
            .filter(s -> Math.random() < perishRate)
            .forEach(s -> {
                s.setState(State.DEAD);
                sheepRepository.save(s);
            });
    }
}