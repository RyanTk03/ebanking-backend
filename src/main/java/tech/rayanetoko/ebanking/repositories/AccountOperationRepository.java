package tech.rayanetoko.ebanking.repositories;

import tech.rayanetoko.ebanking.entities.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepository extends JpaRepository<AccountOperation,Long> {
}