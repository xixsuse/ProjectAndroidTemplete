package com.badlogic.gdx.utils;

import com.badlogic.gdx.math.MathUtils;
import com.google.android.gms.drive.events.CompletionEvent;
import com.google.example.games.basegameutils.GameHelper;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ObjectSet<T> implements Iterable<T> {
    private static final int PRIME1 = -1105259343;
    private static final int PRIME2 = -1262997959;
    private static final int PRIME3 = -825114047;
    int capacity;
    private int hashShift;
    private SetIterator iterator1;
    private SetIterator iterator2;
    T[] keyTable;
    private float loadFactor;
    private int mask;
    private int pushIterations;
    public int size;
    private int stashCapacity;
    int stashSize;
    private int threshold;

    public static class SetIterator<K> implements Iterable<K>, Iterator<K> {
        int currentIndex;
        public boolean hasNext;
        int nextIndex;
        final ObjectSet<K> set;
        boolean valid;

        public SetIterator(ObjectSet<K> set) {
            this.valid = true;
            this.set = set;
            reset();
        }

        public void reset() {
            this.currentIndex = -1;
            this.nextIndex = -1;
            findNextIndex();
        }

        void findNextIndex() {
            this.hasNext = false;
            K[] keyTable = this.set.keyTable;
            int n = this.set.capacity + this.set.stashSize;
            do {
                int i = this.nextIndex + 1;
                this.nextIndex = i;
                if (i >= n) {
                    return;
                }
            } while (keyTable[this.nextIndex] == null);
            this.hasNext = true;
        }

        public void remove() {
            if (this.currentIndex < 0) {
                throw new IllegalStateException("next must be called before remove.");
            }
            if (this.currentIndex >= this.set.capacity) {
                this.set.removeStashIndex(this.currentIndex);
            } else {
                this.set.keyTable[this.currentIndex] = null;
            }
            this.currentIndex = -1;
            ObjectSet objectSet = this.set;
            objectSet.size--;
        }

        public boolean hasNext() {
            return this.hasNext;
        }

        public K next() {
            if (!this.hasNext) {
                throw new NoSuchElementException();
            } else if (this.valid) {
                K key = this.set.keyTable[this.nextIndex];
                this.currentIndex = this.nextIndex;
                findNextIndex();
                return key;
            } else {
                throw new GdxRuntimeException("#iterator() cannot be used nested.");
            }
        }

        public Iterator<K> iterator() {
            return this;
        }

        public Array<K> toArray() {
            Array array = new Array(true, this.set.size);
            while (this.hasNext) {
                array.add(next());
            }
            return array;
        }
    }

    public ObjectSet() {
        this(32, 0.8f);
    }

    public ObjectSet(int initialCapacity) {
        this(initialCapacity, 0.8f);
    }

    public ObjectSet(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("initialCapacity must be >= 0: " + initialCapacity);
        } else if (initialCapacity > 1073741824) {
            throw new IllegalArgumentException("initialCapacity is too large: " + initialCapacity);
        } else {
            this.capacity = MathUtils.nextPowerOfTwo(initialCapacity);
            if (loadFactor <= 0.0f) {
                throw new IllegalArgumentException("loadFactor must be > 0: " + loadFactor);
            }
            this.loadFactor = loadFactor;
            this.threshold = (int) (((float) this.capacity) * loadFactor);
            this.mask = this.capacity - 1;
            this.hashShift = 31 - Integer.numberOfTrailingZeros(this.capacity);
            this.stashCapacity = Math.max(3, ((int) Math.ceil(Math.log((double) this.capacity))) * 2);
            this.pushIterations = Math.max(Math.min(this.capacity, 8), ((int) Math.sqrt((double) this.capacity)) / 8);
            this.keyTable = new Object[(this.capacity + this.stashCapacity)];
        }
    }

    public boolean add(T key) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null.");
        }
        T[] keyTable = this.keyTable;
        int hashCode = key.hashCode();
        int index1 = hashCode & this.mask;
        T key1 = keyTable[index1];
        if (key.equals(key1)) {
            return false;
        }
        int index2 = hash2(hashCode);
        T key2 = keyTable[index2];
        if (key.equals(key2)) {
            return false;
        }
        int index3 = hash3(hashCode);
        T key3 = keyTable[index3];
        if (key.equals(key3)) {
            return false;
        }
        int i = this.capacity;
        int n = i + this.stashSize;
        while (i < n) {
            if (key.equals(keyTable[i])) {
                return false;
            }
            i++;
        }
        int i2;
        if (key1 == null) {
            keyTable[index1] = key;
            i2 = this.size;
            this.size = i2 + 1;
            if (i2 >= this.threshold) {
                resize(this.capacity << 1);
            }
            return true;
        } else if (key2 == null) {
            keyTable[index2] = key;
            i2 = this.size;
            this.size = i2 + 1;
            if (i2 >= this.threshold) {
                resize(this.capacity << 1);
            }
            return true;
        } else if (key3 == null) {
            keyTable[index3] = key;
            i2 = this.size;
            this.size = i2 + 1;
            if (i2 >= this.threshold) {
                resize(this.capacity << 1);
            }
            return true;
        } else {
            push(key, index1, key1, index2, key2, index3, key3);
            return true;
        }
    }

    public void addAll(Array<T> array) {
        ensureCapacity(array.size);
        int n = array.size;
        for (int i = 0; i < n; i++) {
            add(array.get(i));
        }
    }

    public void addAll(ObjectSet<T> set) {
        ensureCapacity(set.size);
        Iterator i$ = set.iterator();
        while (i$.hasNext()) {
            add(i$.next());
        }
    }

    private void addResize(T key) {
        int hashCode = key.hashCode();
        int index1 = hashCode & this.mask;
        T key1 = this.keyTable[index1];
        int i;
        if (key1 == null) {
            this.keyTable[index1] = key;
            i = this.size;
            this.size = i + 1;
            if (i >= this.threshold) {
                resize(this.capacity << 1);
                return;
            }
            return;
        }
        int index2 = hash2(hashCode);
        T key2 = this.keyTable[index2];
        if (key2 == null) {
            this.keyTable[index2] = key;
            i = this.size;
            this.size = i + 1;
            if (i >= this.threshold) {
                resize(this.capacity << 1);
                return;
            }
            return;
        }
        int index3 = hash3(hashCode);
        T key3 = this.keyTable[index3];
        if (key3 == null) {
            this.keyTable[index3] = key;
            i = this.size;
            this.size = i + 1;
            if (i >= this.threshold) {
                resize(this.capacity << 1);
                return;
            }
            return;
        }
        push(key, index1, key1, index2, key2, index3, key3);
    }

    private void push(T insertKey, int index1, T key1, int index2, T key2, int index3, T key3) {
        T[] keyTable = this.keyTable;
        int mask = this.mask;
        int i = 0;
        int pushIterations = this.pushIterations;
        while (true) {
            T evictedKey;
            switch (MathUtils.random(2)) {
                case GameHelper.CLIENT_NONE /*0*/:
                    evictedKey = key1;
                    keyTable[index1] = insertKey;
                    break;
                case CompletionEvent.STATUS_FAILURE /*1*/:
                    evictedKey = key2;
                    keyTable[index2] = insertKey;
                    break;
                default:
                    evictedKey = key3;
                    keyTable[index3] = insertKey;
                    break;
            }
            int hashCode = evictedKey.hashCode();
            index1 = hashCode & mask;
            key1 = keyTable[index1];
            int i2;
            if (key1 == null) {
                keyTable[index1] = evictedKey;
                i2 = this.size;
                this.size = i2 + 1;
                if (i2 >= this.threshold) {
                    resize(this.capacity << 1);
                    return;
                }
                return;
            }
            index2 = hash2(hashCode);
            key2 = keyTable[index2];
            if (key2 == null) {
                keyTable[index2] = evictedKey;
                i2 = this.size;
                this.size = i2 + 1;
                if (i2 >= this.threshold) {
                    resize(this.capacity << 1);
                    return;
                }
                return;
            }
            index3 = hash3(hashCode);
            key3 = keyTable[index3];
            if (key3 == null) {
                keyTable[index3] = evictedKey;
                i2 = this.size;
                this.size = i2 + 1;
                if (i2 >= this.threshold) {
                    resize(this.capacity << 1);
                    return;
                }
                return;
            }
            i++;
            if (i == pushIterations) {
                addStash(evictedKey);
                return;
            }
            insertKey = evictedKey;
        }
    }

    private void addStash(T key) {
        if (this.stashSize == this.stashCapacity) {
            resize(this.capacity << 1);
            add(key);
            return;
        }
        this.keyTable[this.capacity + this.stashSize] = key;
        this.stashSize++;
        this.size++;
    }

    public boolean remove(T key) {
        int hashCode = key.hashCode();
        int index = hashCode & this.mask;
        if (key.equals(this.keyTable[index])) {
            this.keyTable[index] = null;
            this.size--;
            return true;
        }
        index = hash2(hashCode);
        if (key.equals(this.keyTable[index])) {
            this.keyTable[index] = null;
            this.size--;
            return true;
        }
        index = hash3(hashCode);
        if (!key.equals(this.keyTable[index])) {
            return removeStash(key);
        }
        this.keyTable[index] = null;
        this.size--;
        return true;
    }

    boolean removeStash(T key) {
        T[] keyTable = this.keyTable;
        int i = this.capacity;
        int n = i + this.stashSize;
        while (i < n) {
            if (key.equals(keyTable[i])) {
                removeStashIndex(i);
                this.size--;
                return true;
            }
            i++;
        }
        return false;
    }

    void removeStashIndex(int index) {
        this.stashSize--;
        int lastIndex = this.capacity + this.stashSize;
        if (index < lastIndex) {
            this.keyTable[index] = this.keyTable[lastIndex];
        }
    }

    public void shrink(int maximumCapacity) {
        if (maximumCapacity < 0) {
            throw new IllegalArgumentException("maximumCapacity must be >= 0: " + maximumCapacity);
        }
        if (this.size > maximumCapacity) {
            maximumCapacity = this.size;
        }
        if (this.capacity > maximumCapacity) {
            resize(MathUtils.nextPowerOfTwo(maximumCapacity));
        }
    }

    public void clear(int maximumCapacity) {
        if (this.capacity <= maximumCapacity) {
            clear();
            return;
        }
        this.size = 0;
        resize(maximumCapacity);
    }

    public void clear() {
        T[] keyTable = this.keyTable;
        int i = this.capacity + this.stashSize;
        while (true) {
            int i2 = i - 1;
            if (i > 0) {
                keyTable[i2] = null;
                i = i2;
            } else {
                this.size = 0;
                this.stashSize = 0;
                return;
            }
        }
    }

    public boolean contains(T key) {
        int hashCode = key.hashCode();
        if (!key.equals(this.keyTable[hashCode & this.mask])) {
            if (!key.equals(this.keyTable[hash2(hashCode)])) {
                if (!key.equals(this.keyTable[hash3(hashCode)])) {
                    return containsKeyStash(key);
                }
            }
        }
        return true;
    }

    private boolean containsKeyStash(T key) {
        T[] keyTable = this.keyTable;
        int i = this.capacity;
        int n = i + this.stashSize;
        while (i < n) {
            if (key.equals(keyTable[i])) {
                return true;
            }
            i++;
        }
        return false;
    }

    public void ensureCapacity(int additionalCapacity) {
        int sizeNeeded = this.size + additionalCapacity;
        if (sizeNeeded >= this.threshold) {
            resize(MathUtils.nextPowerOfTwo((int) (((float) sizeNeeded) / this.loadFactor)));
        }
    }

    private void resize(int newSize) {
        int oldEndIndex = this.capacity + this.stashSize;
        this.capacity = newSize;
        this.threshold = (int) (((float) newSize) * this.loadFactor);
        this.mask = newSize - 1;
        this.hashShift = 31 - Integer.numberOfTrailingZeros(newSize);
        this.stashCapacity = Math.max(3, ((int) Math.ceil(Math.log((double) newSize))) * 2);
        this.pushIterations = Math.max(Math.min(newSize, 8), ((int) Math.sqrt((double) newSize)) / 8);
        T[] oldKeyTable = this.keyTable;
        this.keyTable = new Object[(this.stashCapacity + newSize)];
        int oldSize = this.size;
        this.size = 0;
        this.stashSize = 0;
        if (oldSize > 0) {
            for (int i = 0; i < oldEndIndex; i++) {
                T key = oldKeyTable[i];
                if (key != null) {
                    addResize(key);
                }
            }
        }
    }

    private int hash2(int h) {
        h *= PRIME2;
        return ((h >>> this.hashShift) ^ h) & this.mask;
    }

    private int hash3(int h) {
        h *= PRIME3;
        return ((h >>> this.hashShift) ^ h) & this.mask;
    }

    public String toString() {
        return '{' + toString(", ") + '}';
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String toString(java.lang.String r7) {
        /*
        r6 = this;
        r5 = r6.size;
        if (r5 != 0) goto L_0x0007;
    L_0x0004:
        r5 = "";
    L_0x0006:
        return r5;
    L_0x0007:
        r0 = new com.badlogic.gdx.utils.StringBuilder;
        r5 = 32;
        r0.<init>(r5);
        r4 = r6.keyTable;
        r1 = r4.length;
        r2 = r1;
    L_0x0012:
        r1 = r2 + -1;
        if (r2 <= 0) goto L_0x0037;
    L_0x0016:
        r3 = r4[r1];
        if (r3 != 0) goto L_0x001c;
    L_0x001a:
        r2 = r1;
        goto L_0x0012;
    L_0x001c:
        r0.append(r3);
        r2 = r1;
    L_0x0020:
        r1 = r2 + -1;
        if (r2 <= 0) goto L_0x0032;
    L_0x0024:
        r3 = r4[r1];
        if (r3 != 0) goto L_0x002a;
    L_0x0028:
        r2 = r1;
        goto L_0x0020;
    L_0x002a:
        r0.append(r7);
        r0.append(r3);
        r2 = r1;
        goto L_0x0020;
    L_0x0032:
        r5 = r0.toString();
        goto L_0x0006;
    L_0x0037:
        r2 = r1;
        goto L_0x0020;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.badlogic.gdx.utils.ObjectSet.toString(java.lang.String):java.lang.String");
    }

    public SetIterator<T> iterator() {
        if (this.iterator1 == null) {
            this.iterator1 = new SetIterator(this);
            this.iterator2 = new SetIterator(this);
        }
        if (this.iterator1.valid) {
            this.iterator2.reset();
            this.iterator2.valid = true;
            this.iterator1.valid = false;
            return this.iterator2;
        }
        this.iterator1.reset();
        this.iterator1.valid = true;
        this.iterator2.valid = false;
        return this.iterator1;
    }
}
