package com.shaikhabdulgani.ConnectHub.config;

import com.shaikhabdulgani.ConnectHub.filter.LastSeenFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up the LastSeenFilter in the application.
 * The LastSeenFilter is responsible for updating the last seen timestamp of users
 * when they interact with any endpoints.
 */
@Configuration
public class LastSeenConfig {

    /**
     * Configures and registers the LastSeenFilter as a filter bean in the application.
     * The LastSeenFilter updates the last seen timestamp of users when they interact
     * with API endpoints under the "/api" path.
     *
     * @return A FilterRegistrationBean containing the LastSeenFilter configuration.
     */
    @Bean
    public FilterRegistrationBean<LastSeenFilter> lastSeenFilter(){
        FilterRegistrationBean<LastSeenFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LastSeenFilter());
        registrationBean.addUrlPatterns("/api/**");
        return registrationBean;
    }

}
