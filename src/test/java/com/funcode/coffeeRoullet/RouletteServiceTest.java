//package com.funcode.coffeeRoullet;
//
//import com.funcode.coffeeRoullet.model.Match;
//import com.funcode.coffeeRoullet.model.Pairing;
//import com.funcode.coffeeRoullet.model.Triad;
//import com.funcode.coffeeRoullet.model.User;
//import com.funcode.coffeeRoullet.service.RouletteService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class RouletteServiceTest {
//
//    private RouletteService service;
//
//    @BeforeEach
//    void setUp() {
//        service = new RouletteService();
//    }
//
//    @Test
//    @DisplayName("Should create standard matches for even number of diverse users")
//    void testEvenDiverseMatching() {
//        User u1 = new User("1", "Alice", "IT");
//        User u2 = new User("2", "Bob", "HR");
//
//        List<Pairing> results = service.generateScalablePairs(List.of(u1, u2));
//
//        assertThat(results).hasSize(1);
//        assertThat(results.get(0)).isInstanceOf(Match.class);
//
//        Match m = (Match) results.get(0);
//        assertThat(m.user1().department()).isNotEqualTo(m.user2().department());
//    }
//
//    @Test
//    @DisplayName("Should create a Triad when there is an odd number of total users")
//    void testTriadStrategy() {
//        // 3 users from different departments
//        User u1 = new User("1", "Alice", "IT");
//        User u2 = new User("2", "Bob", "HR");
//        User u3 = new User("3", "Charlie", "Marketing");
//
//        List<Pairing> results = service.generateScalablePairs(List.of(u1, u2, u3));
//
//        // Logic: 3 users should result in 1 Triad, not 1 Match + 1 Leftover
//        assertThat(results).hasSize(1);
//        assertThat(results.get(0)).isInstanceOf(Triad.class);
//
//        Triad t = (Triad) results.get(0);
//        assertThat(t.user1()).isNotNull();
//        assertThat(t.user2()).isNotNull();
//        assertThat(t.user3()).isNotNull();
//    }
//
//    @Test
//    @DisplayName("Should handle 'Department Overload' by creating triads instead of skipping users")
//    void testDepartmentOverload() {
//        // Scenario: 4 IT people and 1 HR person.
//        // Standard pairing would make 1 Match (IT+HR) and leave 3 IT people.
//        // Our Triad logic should force them together.
//        List<User> users = List.of(
//                new User("1", "IT_1", "IT"),
//                new User("2", "IT_2", "IT"),
//                new User("3", "IT_3", "IT"),
//                new User("4", "IT_4", "IT"),
//                new User("5", "HR_1", "HR")
//        );
//
//        List<Pairing> results = service.generateScalablePairs(users);
//
//        // Verify we used all users and didn't leave anyone behind
//        long totalParticipants = results.stream()
//                .mapToLong(p -> p instanceof Triad ? 3 : 2)
//                .sum();
//
//        assertThat(totalParticipants).isEqualTo(5);
//        assertThat(results.stream().anyMatch(p -> p instanceof Triad)).isTrue();
//    }
//}
