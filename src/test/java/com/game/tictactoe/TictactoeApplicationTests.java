package com.game.tictactoe;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TictactoeApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void mainMethodTest() {
		try (var mocked = Mockito.mockStatic(SpringApplication.class)) {
			mocked.when(
					() -> SpringApplication.run(TictactoeApplication.class, new String[]{}))
					.thenReturn(null);
			TictactoeApplication.main(new String[]{});
			mocked.verify(
					() -> SpringApplication.run(TictactoeApplication.class, new String[]{}));
		}
	}

}
