package com.example;

import java.util.*;
import java.util.stream.Collectors;
import java.util.Arrays;

public class DroneExplorer {
    public static void main(String[] args) {
        Location[] locations = {
                new Location("A", LocationType.FRIENDLY),
                new Location("B", LocationType.ENEMY),
                new Location("C", LocationType.NEUTRAL),
                new Location("D", LocationType.FRIENDLY),
                new Location("E", LocationType.ENEMY),
                new Location("F", LocationType.NEUTRAL)
        };

        TreeSet<Location> friendlyLocations = Arrays.stream(locations)
                .filter(location -> location.getType() == LocationType.FRIENDLY)
                .collect(Collectors.toCollection(TreeSet::new));

        System.out.println("Friendly locations:");
        friendlyLocations.forEach(System.out::println);

        Comparator<Location> typeThenNameComparator = Comparator
                .comparing(Location::getType)
                .thenComparing(Location::getName);

        LinkedList<Location> enemyLocations = Arrays.stream(locations)
                .filter(location -> location.getType() == LocationType.ENEMY)
                .sorted(typeThenNameComparator)
                .collect(Collectors.toCollection(LinkedList::new));

        System.out.println("\nEnemy locations:");
        enemyLocations.forEach(System.out::println);
    }
}
