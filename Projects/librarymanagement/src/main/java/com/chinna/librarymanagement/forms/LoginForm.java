package com.chinna.librarymanagement.forms;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class LoginForm {
    @NotNull
    @Min(1)
    private Long customerId;

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
}
