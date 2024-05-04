package com.example.hiveinform;

import com.example.hiveinform.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HiveInformApplicationTests {

	@Autowired
	private ArticleRepository articleRepository ;
	@Test
	void contextLoads() {
		System.out.println(articleRepository.findById("1").orElse(null));
	}

}
