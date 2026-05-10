package tech.rayanetoko.ebanking.repositories;

import tech.rayanetoko.ebanking.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
}
