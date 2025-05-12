package javachess;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * A BiMap is a bidirectional map that allows for two-way lookups.
 * It maintains two HashMaps, one for each direction of the mapping.
 * It has to be used on bijective data, meaning that each key can only have one value and vice versa.
 * @param <T1> The type of the first key
 * @param <T2> The type of the second key
 */
public class BiMap<T1, T2>{
    private final HashMap<T1, T2> map1;
    private final HashMap<T2, T1> map2;

    public BiMap() {
        map1 = new HashMap<>();
        map2 = new HashMap<>();
    }

    /**
     * Puts a key-value pair in the BiMap. If the key or value already exists, it removes the old pair.
     * @param key The key to put in the map
     * @param value The value to put in the map
     */
    public void put(T1 key, T2 value) {
        if(map1.containsKey(key)) {
            T2 temp = map1.get(key);
            map1.remove(key);
            map2.remove(temp);
        }
        if(map2.containsKey(value)) {
            T1 temp = map2.get(value);
            map2.remove(value);
            map1.remove(temp);
        }
        try {
            map1.put(key, value);
            map2.put(value, key);
        } catch (Exception e) {
            System.out.println("Error while putting in BiMap: " + e.getMessage());
        }
    }

    /**
     * Removes a key-value pair from the BiMap.
     * @param key The key to remove
     */
    public void remove(T1 key) {
        try {
            T2 value = map1.remove(key);
            if (value != null) {
                map2.remove(value);
            }
        } catch (Exception e) {
            System.out.println("Error while removing from BiMap: " + e.getMessage());
        }
    }

    /**
     * Removes a key-value pair from the BiMap using the reverse mapping.
     * @param key The value to remove
     */
    public void removeReverse(T2 key) {
        try {
            T1 value = map2.remove(key);
            if (value != null) {
                map1.remove(value);
            }
        } catch (Exception e) {
            System.out.println("Error while removing from BiMap: " + e.getMessage());
        }
    }

    /**
     * Gets the value associated with the key.
     * @param key The key to get the value for
     * @return The value associated with the key
     */
    public T2 get(T1 key) {
        try {
            return map1.get(key);
        } catch (Exception e) {
            System.out.println("Error while getting from BiMap: " + e.getMessage());
            return null;
        }
    }

    /**
     * Gets the key associated with the value.
     * @param key The value to get the key for
     * @return The key associated with the value
     */
    public T1 getReverse(T2 key) {
        try {
            return map2.get(key);
        } catch (Exception e) {
            System.out.println("Error while getting from BiMap: " + e.getMessage());
            return null;
        }
    }

    /**
     * Gets whether the BiMap contains a key or not.
     * @param key The key to check for
     * @return True if the key is in the BiMap, false otherwise
     */
    public boolean contains(T1 key) {
        try {
            return map1.containsKey(key);
        } catch (Exception e) {
            System.out.println("Error while checking key in BiMap: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets whether the BiMap contains a value or not. (reverse mapping)
     * @param value The value to check for
     * @return True if the value is in the BiMap, false otherwise
     */
    public boolean containsReverse(T2 value) {
        try {
            return map1.containsValue(value);
        } catch (Exception e) {
            System.out.println("Error while checking value in BiMap: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets the key set of the BiMap.
     * @return The key set of the BiMap
     */
    public Set<T1> keySet() {
        try {
            return map1.keySet();
        } catch (Exception e) {
            System.out.println("Error while getting keys from BiMap: " + e.getMessage());
            return null;
        }
    }

    /**
     * Gets the value set of the BiMap.
     * @return The value set of the BiMap
     */
    public Set<T2> reverseKeySet() {
        try {
            return map2.keySet();
        } catch (Exception e) {
            System.out.println("Error while getting values from BiMap: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BiMap{");
        ArrayList<T1> keys = new ArrayList<>(map1.keySet());
        for (T1 key : keys) {
            sb.append(key).append("=").append(map1.get(key)).append(", \n");
        }
        sb.append("}");
        return sb.toString();
    }
}
