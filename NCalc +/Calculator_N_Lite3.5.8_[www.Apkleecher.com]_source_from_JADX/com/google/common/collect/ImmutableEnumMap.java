package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import org.apache.commons.math4.random.ValueServer;

@GwtCompatible(emulated = true, serializable = true)
final class ImmutableEnumMap<K extends Enum<K>, V> extends ImmutableMap<K, V> {
    private final transient EnumMap<K, V> delegate;

    class 1 extends ImmutableSet<K> {
        1() {
        }

        public boolean contains(Object object) {
            return ImmutableEnumMap.this.delegate.containsKey(object);
        }

        public int size() {
            return ImmutableEnumMap.this.size();
        }

        public UnmodifiableIterator<K> iterator() {
            return Iterators.unmodifiableIterator(ImmutableEnumMap.this.delegate.keySet().iterator());
        }

        boolean isPartialView() {
            return true;
        }
    }

    class 2 extends ImmutableMapEntrySet<K, V> {

        class 1 extends UnmodifiableIterator<Entry<K, V>> {
            private final Iterator<Entry<K, V>> backingIterator;

            1() {
                this.backingIterator = ImmutableEnumMap.this.delegate.entrySet().iterator();
            }

            public boolean hasNext() {
                return this.backingIterator.hasNext();
            }

            public Entry<K, V> next() {
                Entry<K, V> entry = (Entry) this.backingIterator.next();
                return Maps.immutableEntry(entry.getKey(), entry.getValue());
            }
        }

        2() {
        }

        ImmutableMap<K, V> map() {
            return ImmutableEnumMap.this;
        }

        public UnmodifiableIterator<Entry<K, V>> iterator() {
            return new 1();
        }
    }

    private static class EnumSerializedForm<K extends Enum<K>, V> implements Serializable {
        private static final long serialVersionUID = 0;
        final EnumMap<K, V> delegate;

        EnumSerializedForm(EnumMap<K, V> delegate) {
            this.delegate = delegate;
        }

        Object readResolve() {
            return new ImmutableEnumMap(null);
        }
    }

    static <K extends Enum<K>, V> ImmutableMap<K, V> asImmutable(EnumMap<K, V> map) {
        switch (map.size()) {
            case ValueServer.DIGEST_MODE /*0*/:
                return ImmutableMap.of();
            case ValueServer.REPLAY_MODE /*1*/:
                Entry<K, V> entry = (Entry) Iterables.getOnlyElement(map.entrySet());
                return ImmutableMap.of(entry.getKey(), entry.getValue());
            default:
                return new ImmutableEnumMap(map);
        }
    }

    private ImmutableEnumMap(EnumMap<K, V> delegate) {
        this.delegate = delegate;
        Preconditions.checkArgument(!delegate.isEmpty());
    }

    ImmutableSet<K> createKeySet() {
        return new 1();
    }

    public int size() {
        return this.delegate.size();
    }

    public boolean containsKey(@Nullable Object key) {
        return this.delegate.containsKey(key);
    }

    public V get(Object key) {
        return this.delegate.get(key);
    }

    ImmutableSet<Entry<K, V>> createEntrySet() {
        return new 2();
    }

    boolean isPartialView() {
        return false;
    }

    Object writeReplace() {
        return new EnumSerializedForm(this.delegate);
    }
}
