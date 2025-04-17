package javachess;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class BiMap<T1, T2>{
    private final HashMap<T1, T2> map1;
    private final HashMap<T2, T1> map2;

    public BiMap() {
        map1 = new HashMap<>();
        map2 = new HashMap<>();
    }

    public BiMap(HashMap<T1, T2> map){
        this.map1 = map;
        this.map2 = new HashMap<>();
        try {
            for (T1 key : map.keySet()) {
                map2.put(map.get(key), key);
            }
        } catch (Exception e) {
            System.out.println("Error while creating BiMap: " + e.getMessage());
        }
    }

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

    public T2 get(T1 key) {
        try {
            return map1.get(key);
        } catch (Exception e) {
            System.out.println("Error while getting from BiMap: " + e.getMessage());
            return null;
        }
    }

    public T1 getReverse(T2 key) {
        try {
            return map2.get(key);
        } catch (Exception e) {
            System.out.println("Error while getting from BiMap: " + e.getMessage());
            return null;
        }
    }

    public boolean contains(T1 key) {
        try {
            return map1.containsKey(key);
        } catch (Exception e) {
            System.out.println("Error while checking key in BiMap: " + e.getMessage());
            return false;
        }
    }

    public boolean containsReverse(T2 value) {
        try {
            return map1.containsValue(value);
        } catch (Exception e) {
            System.out.println("Error while checking value in BiMap: " + e.getMessage());
            return false;
        }
    }

    public Set<T1> keySet() {
        try {
            return map1.keySet();
        } catch (Exception e) {
            System.out.println("Error while getting keys from BiMap: " + e.getMessage());
            return null;
        }
    }

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
