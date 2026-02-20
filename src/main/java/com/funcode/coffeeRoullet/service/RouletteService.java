package com.funcode.coffeeRoullet.service;

import com.funcode.coffeeRoullet.model.Match;
import com.funcode.coffeeRoullet.model.Pairing;
import com.funcode.coffeeRoullet.model.Triad;
import com.funcode.coffeeRoullet.model.User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class RouletteService {
    // In-memory store
    private final Map<String, User> users = new ConcurrentHashMap<>();

    public void addUser(User user) {
        users.put(user.id(), user);
    }

    public List<User> getRegisteredUsers() {
        return new ArrayList<>(users.values());
    }

    public List<Pairing> generateScalablePairs(List<User> allUsers) {
        if (allUsers == null || allUsers.isEmpty()) return new ArrayList<>();

        // 1. Group by department
        Map<String, Queue<User>> buckets = allUsers.stream()
                .collect(Collectors.groupingBy(
                        User::department,
                        Collectors.toCollection(LinkedList::new)
                ));

        List<Pairing> pairings = new ArrayList<>();
        List<String> deptNames = new ArrayList<>(buckets.keySet());

        // 2. Cross-department pairing loop
        while (buckets.size() > 1) {
            Collections.shuffle(deptNames);
            String deptA = deptNames.get(0);
            String deptB = deptNames.get(1);

            User u1 = buckets.get(deptA).poll();
            User u2 = buckets.get(deptB).poll();

            pairings.add(new Match(u1, u2, getRandomIcebreaker()));

            if (buckets.get(deptA).isEmpty()) buckets.remove(deptA);
            if (buckets.get(deptB).isEmpty()) buckets.remove(deptB);
            deptNames = new ArrayList<>(buckets.keySet());
        }

        // 3. Robust Triad/Leftover Strategy
        if (!buckets.isEmpty()) {
            Queue<User> leftOverQueue = buckets.values().iterator().next();

            while (!leftOverQueue.isEmpty()) {
                User oddPerson = leftOverQueue.poll();

                // Check if we can form a Triad with an existing Match
                Optional<Match> lastMatch = pairings.stream()
                        .filter(p -> p instanceof Match)
                        .map(p -> (Match) p)
                        .findFirst(); // Find the first available pair to turn into a triad

                if (lastMatch.isPresent()) {
                    pairings.remove(lastMatch.get());
                    pairings.add(new Triad(lastMatch.get().user1(), lastMatch.get().user2(), oddPerson, getRandomIcebreaker()));
                } else {
                    // If no pairs exist to form a triad, and we have 2+ people in the same dept
                    // we must pair them together anyway (Safety Pair)
                    if (!leftOverQueue.isEmpty()) {
                        User nextPerson = leftOverQueue.poll();
                        pairings.add(new Match(oddPerson, nextPerson, "Same-dept bonding!"));
                    } else {
                        // This is the absolute last person who can't be matched (Waitlist candidate)
                        // For now, let's log them or handle as needed.
                        System.out.println("One user left unmatched: " + oddPerson.name());
                    }
                }
            }
        }
        return pairings;
    }


// TODO SUBH - uncomment it for standalone check
//    public List<Match> generateWeeklyPairs() {
//        List<User> pool = new ArrayList<>(users.values());
//        // to achieve randomness and prevent the same pairs every week, we shuffle the pool before pairing
//        Collections.shuffle(pool);
//        List<Match> matches = new ArrayList<>();
//        // To avoid pairing the same user multiple times in a week
//        Set<String> pairedSet = new HashSet<>();
//
//        for (int i = 0; i < pool.size(); i++) {
//            User u1 = pool.get(i);
//            if (pairedSet.contains(u1.id())) continue;
//
//            for (int j = i + 1; j < pool.size(); j++) {
//                User u2 = pool.get(j);
//                // The "Anti-Echo Chamber" Logic
//                if (!pairedSet.contains(u2.id()) && !u1.department().equals(u2.department())) {
//                    matches.add(new Match(u1, u2, getRandomIcebreaker()));
//                    pairedSet.add(u1.id());
//                    pairedSet.add(u2.id());
//                    break;
//                }
//            }
//        }
//        return matches;
//    }

    //TODO - SUBH - will be improvized to fetch from emp social media profiles in future by using AI to generate personalized icebreakers based on their interests and hobbies

    private String getRandomIcebreaker() {
        var challenges = List.of(
                "Take a selfie with a plant",
                "Discuss your most funnist work moment",
                "Find 3 things you have in common outside of work"
        );
        return challenges.get(new Random().nextInt(challenges.size()));
    }
}
