package io.github.ghacupha.prepay.repository;

import io.github.ghacupha.prepay.domain.Prepayment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Prepayment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrepaymentRepository extends JpaRepository<Prepayment, Long>, JpaSpecificationExecutor<Prepayment> {

}
