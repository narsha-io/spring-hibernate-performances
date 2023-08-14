package io.narsha.spring.jpa.troubleshooting.dto;

import lombok.Data;

@Data
public class UserInfoDTO {

    private String id;
    private String username;

    private String country;
    private String region;
    private String continent;
}
