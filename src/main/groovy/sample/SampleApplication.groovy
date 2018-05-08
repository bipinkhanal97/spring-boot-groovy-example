package sample

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@EnableAutoConfiguration
@ComponentScan
class SampleApplication {

    static void main(String[] args) throws Exception {
        SpringApplication.run(SampleApplication.class, args)
    }

}
