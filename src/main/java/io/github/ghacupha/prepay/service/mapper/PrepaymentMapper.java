package io.github.ghacupha.prepay.service.mapper;

import io.github.ghacupha.prepay.domain.*;
import io.github.ghacupha.prepay.service.dto.PrepaymentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Prepayment and its DTO PrepaymentDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PrepaymentMapper extends EntityMapper<PrepaymentDTO, Prepayment> {



    default Prepayment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Prepayment prepayment = new Prepayment();
        prepayment.setId(id);
        return prepayment;
    }
}
