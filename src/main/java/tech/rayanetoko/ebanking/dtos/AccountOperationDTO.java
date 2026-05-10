package tech.rayanetoko.ebanking.dtos;

import lombok.Data;
import tech.rayanetoko.ebanking.enums.OperationType;

import java.util.Date;

@Data
public class AccountOperationDTO {
    private Long id;
    private Date operationDate;
    private double amount;
    private OperationType type;
    private String description;
}

