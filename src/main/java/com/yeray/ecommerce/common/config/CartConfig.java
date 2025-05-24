package com.yeray.ecommerce.common.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class CartConfig {

    @Value("${cart.inactive.timeout.minutes}")
    private int inactiveTimeoutMinutes;

}
