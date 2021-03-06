package reghzy.utils.memory.multimap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * A HashSetMultiMap is a collection of keys, which key to a hashset, but with functions to make managing that easier
 *
 * @param <K> The Key type
 * @param <V> The Value type (this doesn't have to be a collection, but it will be a hashset in the background)
 */
public class HashSetMultiMap<K, V> implements MultiMap<K, V> {
    private final HashMap<K, HashSet<V>> map;

    public HashSetMultiMap() {
        this.map = new HashMap<K, HashSet<V>>(12);
    }

    public boolean put(K key, V value) {
        return getOrCreateValues(key).add(value);
    }

    public boolean putAll(K key, Collection<V> values) {
        return getOrCreateValues(key).addAll(values);
    }

    public HashSet<V> remove(K key) {
        return map.remove(key);
    }

    public boolean remove(K key, V value) {
        return getOrCreateValues(key).remove(value);
    }

    public Collection<K> getKeys() {
        return map.keySet();
    }

    public HashSet<V> getValues(K key) {
        return getOrCreateValues(key);
    }

    @Override
    public Collection<V> getValuesNoCreate(K key) {
        return map.get(key);
    }

    public boolean containsKey(K key) {
        return this.map.containsKey(key);
    }

    public boolean contains(K key, V value) {
        return getOrCreateValues(key).contains(value);
    }

    public boolean containsValue(V value) {
        for (K key : this.getKeys()) {
            if (getValues(key).contains(value))
                return true;
        }
        return false;
    }

    public int keysSize() {
        return this.map.size();
    }

    public int valuesSize(K key) {
        return getOrCreateValues(key).size();
    }

    @Override
    public int clear() {
        int cleared = this.map.size();
        this.map.clear();
        return cleared;
    }

    @Override
    public HashMap<K, Collection<V>> asMap() {
        HashMap<K, Collection<V>> map = new HashMap<K, Collection<V>>(keysSize());
        for(K key : getKeys()) {
            map.put(key, getValues(key));
        }
        return map;
    }

    public ArrayList<Collection<V>> getAllValues() {
        ArrayList<Collection<V>> valuesCollection = new ArrayList<Collection<V>>(this.map.size() * 4);
        for (K key : getKeys()) {
            valuesCollection.add(getValues(key));
        }
        return valuesCollection;
    }

    public Collection<MultiMapEntry<K, V>> getEntrySet() {
        Collection<MultiMapEntry<K, V>> entrySets = new ArrayList<MultiMapEntry<K, V>>(this.map.size());
        for (K key : this.getKeys()) {
            entrySets.add(new MultiMapEntry<K, V>(key, getOrCreateValues(key)));
        }
        return entrySets;
    }

    private HashSet<V> getOrCreateValues(K key) {
        HashSet<V> hashMap = map.get(key);
        if (hashMap == null) {
            hashMap = new HashSet<V>();
            map.put(key, hashMap);
        }

        return hashMap;
    }
}
