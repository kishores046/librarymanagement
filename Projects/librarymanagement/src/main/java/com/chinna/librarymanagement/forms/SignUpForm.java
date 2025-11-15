package com.chinna.librarymanagement.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignUpForm {
    @NotBlank @Size(min=2, max=100)
    private String name;

    @Email
    private String email;

    @Size(min=10, max=20)
    private String phone;

    @NotBlank
    private String membershipType; // REGULAR/ELITE/PREMIUM/OCCASIONAL

    // getters / setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getMembershipType() { return membershipType; }
    public void setMembershipType(String membershipType) { this.membershipType = membershipType; }
}
