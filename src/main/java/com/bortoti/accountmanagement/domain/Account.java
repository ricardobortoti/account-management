package com.bortoti.accountmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class Account {
    @Id
    @JsonIgnore
    private UUID id;
    @ApiModelProperty(value = "name", name = "name", dataType = "String", example = "Joao")
    private String name;
    @Column(unique=true)
    @ApiModelProperty(value = "accountNumber", name = "accountNumber", dataType = "Integer", example = "1")
    private Integer accountNumber;
    @ApiModelProperty(value = "accountBalance", name = "accountBalance", dataType = "Double", example = "250000.00")
    private Double accountBalance;
}
