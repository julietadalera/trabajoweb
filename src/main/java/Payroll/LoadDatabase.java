package Payroll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    private static final Logger log =
            LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(ItemRepository repository) {

        return args -> {

            log.info(
                "Preloading " +
                repository.save(
                    new Item(
                        "O Hobbit",
                        "Livro",
                        "hobbit.jpg"
                    )
                )
            );

            log.info(
                "Preloading " +
                repository.save(
                    new Item(
                        "Interestelar",
                        "Filme",
                        "interestelar.jpg"
                    )
                )
            );
        };
    }
}