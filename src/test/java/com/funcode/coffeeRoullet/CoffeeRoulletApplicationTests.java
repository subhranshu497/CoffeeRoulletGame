package com.funcode.coffeeRoullet;

import com.funcode.coffeeRoullet.model.Match;
import com.funcode.coffeeRoullet.model.Pairing;
import com.funcode.coffeeRoullet.model.Triad;
import com.funcode.coffeeRoullet.model.User;
import com.funcode.coffeeRoullet.service.RouletteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CoffeeRoulletApplicationTests {

	@Test
	void contextLoads() {
	}
	@Autowired
	private RouletteService service;


	@Test
	@DisplayName("Should handle 'Department Overload' by creating triads")
	void testDepartmentOverload() {
		// Static Load: 5 users (4 IT, 1 HR)
		List<User> users = List.of(
				new User("1", "HR_Alice", "HR"),
				new User("2", "IT_Bob", "IT"),
				new User("3", "IT_Charlie", "IT"),
				new User("4", "IT_David", "IT"),
				new User("5", "IT_Eve", "IT")
		);

		// Act
		List<Pairing> results = service.generateScalablePairs(users);

		// Assert
		assertThat(results)
				.withFailMessage("Results should not be empty for 5 users")
				.isNotEmpty();

		// Verify all 5 users are included in the results
		long totalParticipants = results.stream()
				.mapToLong(p -> (p instanceof Triad) ? 3 : 2)
				.sum();

		assertThat(totalParticipants)
				.withFailMessage("Expected 5 total participants across all matches/triads")
				.isEqualTo(5L);

		// Verify at least one Triad exists (since 5 is odd and/or dept is skewed)
		boolean hasTriad = results.stream().anyMatch(p -> p instanceof Triad);
		assertThat(hasTriad)
				.withFailMessage("Should have formed at least one Triad for the leftover users")
				.isTrue();
	}

	@Test
	@DisplayName("Should create exactly one Triad for 3 diverse users")
	void testTriadStrategy() {
		List<User> users = List.of(
				new User("1", "Alice", "IT"),
				new User("2", "Bob", "HR"),
				new User("3", "Charlie", "Finance")
		);

		List<Pairing> results = service.generateScalablePairs(users);

		// For 3 people, the only valid outcome is 1 Triad
		assertThat(results).hasSize(1);
		assertThat(results.get(0)).isInstanceOf(Triad.class);
	}

}
