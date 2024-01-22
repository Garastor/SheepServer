package com.paazl.data.repositories;

import com.paazl.data.CurrentBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrentBalanceRepository extends JpaRepository<CurrentBalance, Long> {
    CurrentBalance findFirstByOrderByTimestampDesc();


}
