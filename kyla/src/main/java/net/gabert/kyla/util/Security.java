package net.gabert.kyla.util;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

public final class Security {
    private static final SecureRandom RND_GENERATOR = new SecureRandom();
    
    public static UUID getUUID() {
        return UUID.randomUUID();
    }
    
    public static String uniqueToken() {
        return new BigInteger(255, RND_GENERATOR).toString(32);
    }

    public static class RandomStringGroup {
        private static final char[] alphaNum = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        private static final Random generator = new Random();
        private static final Map<String, RandomStringGroup> instances = 
                Collections.synchronizedMap(new HashMap<String, RandomStringGroup>());
        private final int groupSize;
        private final int numOfGroups;
        private final String delimiter;

        private RandomStringGroup(int groupSize, int numOfGroups, String delimiter) {
            this.groupSize = groupSize;
            this.numOfGroups = numOfGroups;
            this.delimiter = delimiter;
        }

        public String nextString() {
            StringBuilder sb = new StringBuilder();

            for(int i=0; i < numOfGroups; i++) {
                for (int j=0; j< groupSize; j++) {
                    sb.append(randomChar());
                }
                sb.append(delimiter);
            }

            sb.setLength(sb.length() - 1);

            return sb.toString();
        }

        private static char randomChar() {
            return alphaNum[generator.nextInt(alphaNum.length)];
        }

        public static RandomStringGroup getInstance(int groupSize, int numOfGroups, @SuppressWarnings("SameParameterValue") String delimiter) {
            String key = groupSize + numOfGroups + delimiter;

            if (instances.containsKey(key) == false)
                instances.put(key, new RandomStringGroup(groupSize, numOfGroups, delimiter));

            return instances.get(key);
        }
    }
}
