package tech.buildruin.agregadorInv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.buildruin.agregadorInv.entity.Account;
import tech.buildruin.agregadorInv.entity.User;

import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository <Account, UUID> {

}
