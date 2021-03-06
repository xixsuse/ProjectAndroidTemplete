package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
final class ImmutableMapKeySet<K, V> extends ImmutableSet<K> {
    private final ImmutableMap<K, V> map;

    class 1 extends ImmutableAsList<K> {
        final /* synthetic */ ImmutableList val$entryList;

        1(ImmutableList immutableList) {
            this.val$entryList = immutableList;
        }

        public K get(int index) {
            return ((Entry) this.val$entryList.get(index)).getKey();
        }

        ImmutableCollection<K> delegateCollection() {
            return ImmutableMapKeySet.this;
        }
    }

    @GwtIncompatible("serialization")
    private static class KeySetSerializedForm<K> implements Serializable {
        private static final long serialVersionUID = 0;
        final ImmutableMap<K, ?> map;

        KeySetSerializedForm(ImmutableMap<K, ?> map) {
            this.map = map;
        }

        Object readResolve() {
            return this.map.keySet();
        }
    }

    ImmutableMapKeySet(ImmutableMap<K, V> map) {
        this.map = map;
    }

    public int size() {
        return this.map.size();
    }

    public UnmodifiableIterator<K> iterator() {
        return asList().iterator();
    }

    public boolean contains(@Nullable Object object) {
        return this.map.containsKey(object);
    }

    ImmutableList<K> createAsList() {
        return new 1(this.map.entrySet().asList());
    }

    boolean isPartialView() {
        return true;
    }

    @GwtIncompatible("serialization")
    Object writeReplace() {
        return new KeySetSerializedForm(this.map);
    }
}
