package course.springframeworkguru.messagesapirestg.config;

import course.springframeworkguru.messagesapirestg.session.AdminSessionFilter;
import course.springframeworkguru.messagesapirestg.session.SessionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@org.springframework.context.annotation.Configuration
@PropertySource("application.properties")
@EnableScheduling
public class Configuration {

    private final SessionFilter sessionFilter;
    private final AdminSessionFilter adminSessionFilter;

    @Autowired
    public Configuration(SessionFilter sessionFilter, AdminSessionFilter adminSessionFilter) {
        this.sessionFilter = sessionFilter;
        this.adminSessionFilter = adminSessionFilter;
    }

    @Bean
    public FilterRegistrationBean userFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(sessionFilter);
        registration.addUrlPatterns("/api/*");
        return registration;
    }

    @Bean
    public FilterRegistrationBean adminFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(adminSessionFilter);
        registration.addUrlPatterns("/admin/*");
        return registration;
    }
}
