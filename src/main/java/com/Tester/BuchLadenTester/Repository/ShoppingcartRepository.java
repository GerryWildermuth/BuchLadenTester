package com.Tester.BuchLadenTester.Repository;

import com.Tester.BuchLadenTester.Model.Shoppingcart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingcartRepository extends JpaRepository<Shoppingcart, Integer> {

}