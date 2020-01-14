package com.Tester.BuchLadenTester.Repository;

import com.Tester.BuchLadenTester.Model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegesRepository extends JpaRepository<Privilege, Integer> {
    //@Override
   // <S extends Privilege> S save(S s);
    Privilege findByName(String name);
}
