package tech.buildruin.agregadorInv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.buildruin.agregadorInv.entity.Stock;
import tech.buildruin.agregadorInv.entity.User;

import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository <Stock, String> {

}
