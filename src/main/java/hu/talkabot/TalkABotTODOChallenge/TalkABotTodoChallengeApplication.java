package hu.talkabot.TalkABotTODOChallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.datatables.repository.DataTablesRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = DataTablesRepositoryFactoryBean.class)
public class TalkABotTodoChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TalkABotTodoChallengeApplication.class, args);
	}

}
