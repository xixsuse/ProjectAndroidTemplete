package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet.Builder;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public final class Sets {

    static abstract class ImprovedAbstractSet<E> extends AbstractSet<E> {
        ImprovedAbstractSet() {
        }

        public boolean removeAll(Collection<?> c) {
            return Sets.removeAllImpl((Set) this, (Collection) c);
        }

        public boolean retainAll(Collection<?> c) {
            return super.retainAll((Collection) Preconditions.checkNotNull(c));
        }
    }

    @GwtIncompatible("NavigableSet")
    static class DescendingSet<E> extends ForwardingNavigableSet<E> {
        private final NavigableSet<E> forward;

        DescendingSet(NavigableSet<E> forward) {
            this.forward = forward;
        }

        protected NavigableSet<E> delegate() {
            return this.forward;
        }

        public E lower(E e) {
            return this.forward.higher(e);
        }

        public E floor(E e) {
            return this.forward.ceiling(e);
        }

        public E ceiling(E e) {
            return this.forward.floor(e);
        }

        public E higher(E e) {
            return this.forward.lower(e);
        }

        public E pollFirst() {
            return this.forward.pollLast();
        }

        public E pollLast() {
            return this.forward.pollFirst();
        }

        public NavigableSet<E> descendingSet() {
            return this.forward;
        }

        public Iterator<E> descendingIterator() {
            return this.forward.iterator();
        }

        public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
            return this.forward.subSet(toElement, toInclusive, fromElement, fromInclusive).descendingSet();
        }

        public NavigableSet<E> headSet(E toElement, boolean inclusive) {
            return this.forward.tailSet(toElement, inclusive).descendingSet();
        }

        public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
            return this.forward.headSet(fromElement, inclusive).descendingSet();
        }

        public Comparator<? super E> comparator() {
            Comparator<? super E> forwardComparator = this.forward.comparator();
            if (forwardComparator == null) {
                return Ordering.natural().reverse();
            }
            return reverse(forwardComparator);
        }

        private static <T> Ordering<T> reverse(Comparator<T> forward) {
            return Ordering.from((Comparator) forward).reverse();
        }

        public E first() {
            return this.forward.last();
        }

        public SortedSet<E> headSet(E toElement) {
            return standardHeadSet(toElement);
        }

        public E last() {
            return this.forward.first();
        }

        public SortedSet<E> subSet(E fromElement, E toElement) {
            return standardSubSet(fromElement, toElement);
        }

        public SortedSet<E> tailSet(E fromElement) {
            return standardTailSet(fromElement);
        }

        public Iterator<E> iterator() {
            return this.forward.descendingIterator();
        }

        public Object[] toArray() {
            return standardToArray();
        }

        public <T> T[] toArray(T[] array) {
            return standardToArray(array);
        }

        public String toString() {
            return standardToString();
        }
    }

    public static abstract class SetView<E> extends AbstractSet<E> {
        private SetView() {
        }

        public ImmutableSet<E> immutableCopy() {
            return ImmutableSet.copyOf((Collection) this);
        }

        public <S extends Set<E>> S copyInto(S set) {
            set.addAll(this);
            return set;
        }
    }

    static class 1 extends SetView<E> {
        final /* synthetic */ Set val$set1;
        final /* synthetic */ Set val$set2;
        final /* synthetic */ Set val$set2minus1;

        1(Set set, Set set2, Set set3) {
            this.val$set1 = set;
            this.val$set2minus1 = set2;
            this.val$set2 = set3;
            super();
        }

        public int size() {
            return this.val$set1.size() + this.val$set2minus1.size();
        }

        public boolean isEmpty() {
            return this.val$set1.isEmpty() && this.val$set2.isEmpty();
        }

        public Iterator<E> iterator() {
            return Iterators.unmodifiableIterator(Iterators.concat(this.val$set1.iterator(), this.val$set2minus1.iterator()));
        }

        public boolean contains(Object object) {
            return this.val$set1.contains(object) || this.val$set2.contains(object);
        }

        public <S extends Set<E>> S copyInto(S set) {
            set.addAll(this.val$set1);
            set.addAll(this.val$set2);
            return set;
        }

        public ImmutableSet<E> immutableCopy() {
            return new Builder().addAll(this.val$set1).addAll(this.val$set2).build();
        }
    }

    static class 2 extends SetView<E> {
        final /* synthetic */ Predicate val$inSet2;
        final /* synthetic */ Set val$set1;
        final /* synthetic */ Set val$set2;

        2(Set set, Predicate predicate, Set set2) {
            this.val$set1 = set;
            this.val$inSet2 = predicate;
            this.val$set2 = set2;
            super();
        }

        public Iterator<E> iterator() {
            return Iterators.filter(this.val$set1.iterator(), this.val$inSet2);
        }

        public int size() {
            return Iterators.size(iterator());
        }

        public boolean isEmpty() {
            return !iterator().hasNext();
        }

        public boolean contains(Object object) {
            return this.val$set1.contains(object) && this.val$set2.contains(object);
        }

        public boolean containsAll(Collection<?> collection) {
            return this.val$set1.containsAll(collection) && this.val$set2.containsAll(collection);
        }
    }

    static class 3 extends SetView<E> {
        final /* synthetic */ Predicate val$notInSet2;
        final /* synthetic */ Set val$set1;
        final /* synthetic */ Set val$set2;

        3(Set set, Predicate predicate, Set set2) {
            this.val$set1 = set;
            this.val$notInSet2 = predicate;
            this.val$set2 = set2;
            super();
        }

        public Iterator<E> iterator() {
            return Iterators.filter(this.val$set1.iterator(), this.val$notInSet2);
        }

        public int size() {
            return Iterators.size(iterator());
        }

        public boolean isEmpty() {
            return this.val$set2.containsAll(this.val$set1);
        }

        public boolean contains(Object element) {
            return this.val$set1.contains(element) && !this.val$set2.contains(element);
        }
    }

    private static final class CartesianSet<E> extends ForwardingCollection<List<E>> implements Set<List<E>> {
        private final transient ImmutableList<ImmutableSet<E>> axes;
        private final transient CartesianList<E> delegate;

        static class 1 extends ImmutableList<List<E>> {
            final /* synthetic */ ImmutableList val$axes;

            1(ImmutableList immutableList) {
                this.val$axes = immutableList;
            }

            public int size() {
                return this.val$axes.size();
            }

            public List<E> get(int index) {
                return ((ImmutableSet) this.val$axes.get(index)).asList();
            }

            boolean isPartialView() {
                return true;
            }
        }

        static <E> Set<List<E>> create(List<? extends Set<? extends E>> sets) {
            ImmutableList.Builder<ImmutableSet<E>> axesBuilder = new ImmutableList.Builder(sets.size());
            for (Collection set : sets) {
                Object copy = ImmutableSet.copyOf(set);
                if (copy.isEmpty()) {
                    return ImmutableSet.of();
                }
                axesBuilder.add(copy);
            }
            ImmutableList<ImmutableSet<E>> axes = axesBuilder.build();
            return new CartesianSet(axes, new CartesianList(new 1(axes)));
        }

        private CartesianSet(ImmutableList<ImmutableSet<E>> axes, CartesianList<E> delegate) {
            this.axes = axes;
            this.delegate = delegate;
        }

        protected Collection<List<E>> delegate() {
            return this.delegate;
        }

        public boolean equals(@Nullable Object object) {
            if (!(object instanceof CartesianSet)) {
                return super.equals(object);
            }
            return this.axes.equals(((CartesianSet) object).axes);
        }

        public int hashCode() {
            int adjust = size() - 1;
            for (int i = 0; i < this.axes.size(); i++) {
                adjust = ((adjust * 31) ^ -1) ^ -1;
            }
            int hash = 1;
            Iterator i$ = this.axes.iterator();
            while (i$.hasNext()) {
                Set<E> axis = (Set) i$.next();
                hash = (((hash * 31) + ((size() / axis.size()) * axis.hashCode())) ^ -1) ^ -1;
            }
            return ((hash + adjust) ^ -1) ^ -1;
        }
    }

    private static class FilteredSet<E> extends FilteredCollection<E> implements Set<E> {
        FilteredSet(Set<E> unfiltered, Predicate<? super E> predicate) {
            super(unfiltered, predicate);
        }

        public boolean equals(@Nullable Object object) {
            return Sets.equalsImpl(this, object);
        }

        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }
    }

    private static class FilteredSortedSet<E> extends FilteredSet<E> implements SortedSet<E> {
        FilteredSortedSet(SortedSet<E> unfiltered, Predicate<? super E> predicate) {
            super(unfiltered, predicate);
        }

        public Comparator<? super E> comparator() {
            return ((SortedSet) this.unfiltered).comparator();
        }

        public SortedSet<E> subSet(E fromElement, E toElement) {
            return new FilteredSortedSet(((SortedSet) this.unfiltered).subSet(fromElement, toElement), this.predicate);
        }

        public SortedSet<E> headSet(E toElement) {
            return new FilteredSortedSet(((SortedSet) this.unfiltered).headSet(toElement), this.predicate);
        }

        public SortedSet<E> tailSet(E fromElement) {
            return new FilteredSortedSet(((SortedSet) this.unfiltered).tailSet(fromElement), this.predicate);
        }

        public E first() {
            return iterator().next();
        }

        public E last() {
            SortedSet<E> sortedUnfiltered = (SortedSet) this.unfiltered;
            while (true) {
                E element = sortedUnfiltered.last();
                if (this.predicate.apply(element)) {
                    return element;
                }
                sortedUnfiltered = sortedUnfiltered.headSet(element);
            }
        }
    }

    @GwtIncompatible("NavigableSet")
    private static class FilteredNavigableSet<E> extends FilteredSortedSet<E> implements NavigableSet<E> {
        FilteredNavigableSet(NavigableSet<E> unfiltered, Predicate<? super E> predicate) {
            super(unfiltered, predicate);
        }

        NavigableSet<E> unfiltered() {
            return (NavigableSet) this.unfiltered;
        }

        @Nullable
        public E lower(E e) {
            return Iterators.getNext(headSet(e, false).descendingIterator(), null);
        }

        @Nullable
        public E floor(E e) {
            return Iterators.getNext(headSet(e, true).descendingIterator(), null);
        }

        public E ceiling(E e) {
            return Iterables.getFirst(tailSet(e, true), null);
        }

        public E higher(E e) {
            return Iterables.getFirst(tailSet(e, false), null);
        }

        public E pollFirst() {
            return Iterables.removeFirstMatching(unfiltered(), this.predicate);
        }

        public E pollLast() {
            return Iterables.removeFirstMatching(unfiltered().descendingSet(), this.predicate);
        }

        public NavigableSet<E> descendingSet() {
            return Sets.filter(unfiltered().descendingSet(), this.predicate);
        }

        public Iterator<E> descendingIterator() {
            return Iterators.filter(unfiltered().descendingIterator(), this.predicate);
        }

        public E last() {
            return descendingIterator().next();
        }

        public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
            return Sets.filter(unfiltered().subSet(fromElement, fromInclusive, toElement, toInclusive), this.predicate);
        }

        public NavigableSet<E> headSet(E toElement, boolean inclusive) {
            return Sets.filter(unfiltered().headSet(toElement, inclusive), this.predicate);
        }

        public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
            return Sets.filter(unfiltered().tailSet(fromElement, inclusive), this.predicate);
        }
    }

    private static final class PowerSet<E> extends AbstractSet<Set<E>> {
        final ImmutableMap<E, Integer> inputSet;

        class 1 extends AbstractIndexedListIterator<Set<E>> {
            1(int x0) {
                super(x0);
            }

            protected Set<E> get(int setBits) {
                return new SubSet(PowerSet.this.inputSet, setBits);
            }
        }

        PowerSet(Set<E> input) {
            boolean z;
            ImmutableMap.Builder<E, Integer> builder = ImmutableMap.builder();
            int i = 0;
            for (E e : (Set) Preconditions.checkNotNull(input)) {
                int i2 = i + 1;
                builder.put(e, Integer.valueOf(i));
                i = i2;
            }
            this.inputSet = builder.build();
            if (this.inputSet.size() <= 30) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkArgument(z, "Too many elements to create power set: %s > 30", Integer.valueOf(this.inputSet.size()));
        }

        public int size() {
            return 1 << this.inputSet.size();
        }

        public boolean isEmpty() {
            return false;
        }

        public Iterator<Set<E>> iterator() {
            return new 1(size());
        }

        public boolean contains(@Nullable Object obj) {
            if (!(obj instanceof Set)) {
                return false;
            }
            return this.inputSet.keySet().containsAll((Set) obj);
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof PowerSet)) {
                return super.equals(obj);
            }
            return this.inputSet.equals(((PowerSet) obj).inputSet);
        }

        public int hashCode() {
            return this.inputSet.keySet().hashCode() << (this.inputSet.size() - 1);
        }

        public String toString() {
            return "powerSet(" + this.inputSet + ")";
        }
    }

    private static final class SubSet<E> extends AbstractSet<E> {
        private final ImmutableMap<E, Integer> inputSet;
        private final int mask;

        class 1 extends UnmodifiableIterator<E> {
            final ImmutableList<E> elements;
            int remainingSetBits;

            1() {
                this.elements = SubSet.this.inputSet.keySet().asList();
                this.remainingSetBits = SubSet.this.mask;
            }

            public boolean hasNext() {
                return this.remainingSetBits != 0;
            }

            public E next() {
                int index = Integer.numberOfTrailingZeros(this.remainingSetBits);
                if (index == 32) {
                    throw new NoSuchElementException();
                }
                this.remainingSetBits &= (1 << index) ^ -1;
                return this.elements.get(index);
            }
        }

        SubSet(ImmutableMap<E, Integer> inputSet, int mask) {
            this.inputSet = inputSet;
            this.mask = mask;
        }

        public Iterator<E> iterator() {
            return new 1();
        }

        public int size() {
            return Integer.bitCount(this.mask);
        }

        public boolean contains(@Nullable Object o) {
            Integer index = (Integer) this.inputSet.get(o);
            if (index == null || (this.mask & (1 << index.intValue())) == 0) {
                return false;
            }
            return true;
        }
    }

    @GwtIncompatible("NavigableSet")
    static final class UnmodifiableNavigableSet<E> extends ForwardingSortedSet<E> implements NavigableSet<E>, Serializable {
        private static final long serialVersionUID = 0;
        private final NavigableSet<E> delegate;
        private transient UnmodifiableNavigableSet<E> descendingSet;

        UnmodifiableNavigableSet(NavigableSet<E> delegate) {
            this.delegate = (NavigableSet) Preconditions.checkNotNull(delegate);
        }

        protected SortedSet<E> delegate() {
            return Collections.unmodifiableSortedSet(this.delegate);
        }

        public E lower(E e) {
            return this.delegate.lower(e);
        }

        public E floor(E e) {
            return this.delegate.floor(e);
        }

        public E ceiling(E e) {
            return this.delegate.ceiling(e);
        }

        public E higher(E e) {
            return this.delegate.higher(e);
        }

        public E pollFirst() {
            throw new UnsupportedOperationException();
        }

        public E pollLast() {
            throw new UnsupportedOperationException();
        }

        public NavigableSet<E> descendingSet() {
            UnmodifiableNavigableSet<E> result = this.descendingSet;
            if (result != null) {
                return result;
            }
            result = new UnmodifiableNavigableSet(this.delegate.descendingSet());
            this.descendingSet = result;
            result.descendingSet = this;
            return result;
        }

        public Iterator<E> descendingIterator() {
            return Iterators.unmodifiableIterator(this.delegate.descendingIterator());
        }

        public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
            return Sets.unmodifiableNavigableSet(this.delegate.subSet(fromElement, fromInclusive, toElement, toInclusive));
        }

        public NavigableSet<E> headSet(E toElement, boolean inclusive) {
            return Sets.unmodifiableNavigableSet(this.delegate.headSet(toElement, inclusive));
        }

        public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
            return Sets.unmodifiableNavigableSet(this.delegate.tailSet(fromElement, inclusive));
        }
    }

    private Sets() {
    }

    @GwtCompatible(serializable = true)
    public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(E anElement, E... otherElements) {
        return ImmutableEnumSet.asImmutable(EnumSet.of(anElement, otherElements));
    }

    @GwtCompatible(serializable = true)
    public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(Iterable<E> elements) {
        if (elements instanceof ImmutableEnumSet) {
            return (ImmutableEnumSet) elements;
        }
        if (elements instanceof Collection) {
            Collection<E> collection = (Collection) elements;
            if (collection.isEmpty()) {
                return ImmutableSet.of();
            }
            return ImmutableEnumSet.asImmutable(EnumSet.copyOf(collection));
        }
        Iterator<E> itr = elements.iterator();
        if (!itr.hasNext()) {
            return ImmutableSet.of();
        }
        EnumSet<E> enumSet = EnumSet.of((Enum) itr.next());
        Iterators.addAll(enumSet, itr);
        return ImmutableEnumSet.asImmutable(enumSet);
    }

    public static <E extends Enum<E>> EnumSet<E> newEnumSet(Iterable<E> iterable, Class<E> elementType) {
        EnumSet<E> set = EnumSet.noneOf(elementType);
        Iterables.addAll(set, iterable);
        return set;
    }

    public static <E> HashSet<E> newHashSet() {
        return new HashSet();
    }

    public static <E> HashSet<E> newHashSet(E... elements) {
        HashSet<E> set = newHashSetWithExpectedSize(elements.length);
        Collections.addAll(set, elements);
        return set;
    }

    public static <E> HashSet<E> newHashSetWithExpectedSize(int expectedSize) {
        return new HashSet(Maps.capacity(expectedSize));
    }

    public static <E> HashSet<E> newHashSet(Iterable<? extends E> elements) {
        return elements instanceof Collection ? new HashSet(Collections2.cast(elements)) : newHashSet(elements.iterator());
    }

    public static <E> HashSet<E> newHashSet(Iterator<? extends E> elements) {
        HashSet<E> set = newHashSet();
        Iterators.addAll(set, elements);
        return set;
    }

    public static <E> Set<E> newConcurrentHashSet() {
        return newSetFromMap(new ConcurrentHashMap());
    }

    public static <E> Set<E> newConcurrentHashSet(Iterable<? extends E> elements) {
        Set<E> set = newConcurrentHashSet();
        Iterables.addAll(set, elements);
        return set;
    }

    public static <E> LinkedHashSet<E> newLinkedHashSet() {
        return new LinkedHashSet();
    }

    public static <E> LinkedHashSet<E> newLinkedHashSetWithExpectedSize(int expectedSize) {
        return new LinkedHashSet(Maps.capacity(expectedSize));
    }

    public static <E> LinkedHashSet<E> newLinkedHashSet(Iterable<? extends E> elements) {
        if (elements instanceof Collection) {
            return new LinkedHashSet(Collections2.cast(elements));
        }
        LinkedHashSet<E> set = newLinkedHashSet();
        Iterables.addAll(set, elements);
        return set;
    }

    public static <E extends Comparable> TreeSet<E> newTreeSet() {
        return new TreeSet();
    }

    public static <E extends Comparable> TreeSet<E> newTreeSet(Iterable<? extends E> elements) {
        TreeSet<E> set = newTreeSet();
        Iterables.addAll(set, elements);
        return set;
    }

    public static <E> TreeSet<E> newTreeSet(Comparator<? super E> comparator) {
        return new TreeSet((Comparator) Preconditions.checkNotNull(comparator));
    }

    public static <E> Set<E> newIdentityHashSet() {
        return newSetFromMap(Maps.newIdentityHashMap());
    }

    @GwtIncompatible("CopyOnWriteArraySet")
    public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet() {
        return new CopyOnWriteArraySet();
    }

    @GwtIncompatible("CopyOnWriteArraySet")
    public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet(Iterable<? extends E> elements) {
        return new CopyOnWriteArraySet(elements instanceof Collection ? Collections2.cast(elements) : Lists.newArrayList((Iterable) elements));
    }

    public static <E extends Enum<E>> EnumSet<E> complementOf(Collection<E> collection) {
        if (collection instanceof EnumSet) {
            return EnumSet.complementOf((EnumSet) collection);
        }
        Preconditions.checkArgument(!collection.isEmpty(), "collection is empty; use the other version of this method");
        return makeComplementByHand(collection, ((Enum) collection.iterator().next()).getDeclaringClass());
    }

    public static <E extends Enum<E>> EnumSet<E> complementOf(Collection<E> collection, Class<E> type) {
        Preconditions.checkNotNull(collection);
        return collection instanceof EnumSet ? EnumSet.complementOf((EnumSet) collection) : makeComplementByHand(collection, type);
    }

    private static <E extends Enum<E>> EnumSet<E> makeComplementByHand(Collection<E> collection, Class<E> type) {
        EnumSet<E> result = EnumSet.allOf(type);
        result.removeAll(collection);
        return result;
    }

    public static <E> Set<E> newSetFromMap(Map<E, Boolean> map) {
        return Platform.newSetFromMap(map);
    }

    public static <E> SetView<E> union(Set<? extends E> set1, Set<? extends E> set2) {
        Preconditions.checkNotNull(set1, "set1");
        Preconditions.checkNotNull(set2, "set2");
        return new 1(set1, difference(set2, set1), set2);
    }

    public static <E> SetView<E> intersection(Set<E> set1, Set<?> set2) {
        Preconditions.checkNotNull(set1, "set1");
        Preconditions.checkNotNull(set2, "set2");
        return new 2(set1, Predicates.in(set2), set2);
    }

    public static <E> SetView<E> difference(Set<E> set1, Set<?> set2) {
        Preconditions.checkNotNull(set1, "set1");
        Preconditions.checkNotNull(set2, "set2");
        return new 3(set1, Predicates.not(Predicates.in(set2)), set2);
    }

    public static <E> SetView<E> symmetricDifference(Set<? extends E> set1, Set<? extends E> set2) {
        Preconditions.checkNotNull(set1, "set1");
        Preconditions.checkNotNull(set2, "set2");
        return difference(union(set1, set2), intersection(set1, set2));
    }

    public static <E> Set<E> filter(Set<E> unfiltered, Predicate<? super E> predicate) {
        if (unfiltered instanceof SortedSet) {
            return filter((SortedSet) unfiltered, (Predicate) predicate);
        }
        if (!(unfiltered instanceof FilteredSet)) {
            return new FilteredSet((Set) Preconditions.checkNotNull(unfiltered), (Predicate) Preconditions.checkNotNull(predicate));
        }
        FilteredSet<E> filtered = (FilteredSet) unfiltered;
        return new FilteredSet((Set) filtered.unfiltered, Predicates.and(filtered.predicate, predicate));
    }

    public static <E> SortedSet<E> filter(SortedSet<E> unfiltered, Predicate<? super E> predicate) {
        return Platform.setsFilterSortedSet(unfiltered, predicate);
    }

    static <E> SortedSet<E> filterSortedIgnoreNavigable(SortedSet<E> unfiltered, Predicate<? super E> predicate) {
        if (!(unfiltered instanceof FilteredSet)) {
            return new FilteredSortedSet((SortedSet) Preconditions.checkNotNull(unfiltered), (Predicate) Preconditions.checkNotNull(predicate));
        }
        FilteredSet<E> filtered = (FilteredSet) unfiltered;
        return new FilteredSortedSet((SortedSet) filtered.unfiltered, Predicates.and(filtered.predicate, predicate));
    }

    @GwtIncompatible("NavigableSet")
    public static <E> NavigableSet<E> filter(NavigableSet<E> unfiltered, Predicate<? super E> predicate) {
        if (!(unfiltered instanceof FilteredSet)) {
            return new FilteredNavigableSet((NavigableSet) Preconditions.checkNotNull(unfiltered), (Predicate) Preconditions.checkNotNull(predicate));
        }
        FilteredSet<E> filtered = (FilteredSet) unfiltered;
        return new FilteredNavigableSet((NavigableSet) filtered.unfiltered, Predicates.and(filtered.predicate, predicate));
    }

    public static <B> Set<List<B>> cartesianProduct(List<? extends Set<? extends B>> sets) {
        return CartesianSet.create(sets);
    }

    public static <B> Set<List<B>> cartesianProduct(Set<? extends B>... sets) {
        return cartesianProduct(Arrays.asList(sets));
    }

    @GwtCompatible(serializable = false)
    public static <E> Set<Set<E>> powerSet(Set<E> set) {
        return new PowerSet(set);
    }

    static int hashCodeImpl(Set<?> s) {
        int hashCode = 0;
        for (Object o : s) {
            hashCode = ((hashCode + (o != null ? o.hashCode() : 0)) ^ -1) ^ -1;
        }
        return hashCode;
    }

    static boolean equalsImpl(Set<?> s, @Nullable Object object) {
        boolean z = true;
        if (s == object) {
            return true;
        }
        if (!(object instanceof Set)) {
            return false;
        }
        Set<?> o = (Set) object;
        try {
            if (!(s.size() == o.size() && s.containsAll(o))) {
                z = false;
            }
            return z;
        } catch (NullPointerException e) {
            return false;
        } catch (ClassCastException e2) {
            return false;
        }
    }

    @GwtIncompatible("NavigableSet")
    public static <E> NavigableSet<E> unmodifiableNavigableSet(NavigableSet<E> set) {
        return ((set instanceof ImmutableSortedSet) || (set instanceof UnmodifiableNavigableSet)) ? set : new UnmodifiableNavigableSet(set);
    }

    @GwtIncompatible("NavigableSet")
    public static <E> NavigableSet<E> synchronizedNavigableSet(NavigableSet<E> navigableSet) {
        return Synchronized.navigableSet(navigableSet);
    }

    static boolean removeAllImpl(Set<?> set, Iterator<?> iterator) {
        boolean changed = false;
        while (iterator.hasNext()) {
            changed |= set.remove(iterator.next());
        }
        return changed;
    }

    static boolean removeAllImpl(Set<?> set, Collection<?> collection) {
        Preconditions.checkNotNull(collection);
        if (collection instanceof Multiset) {
            collection = ((Multiset) collection).elementSet();
        }
        if (!(collection instanceof Set) || collection.size() <= set.size()) {
            return removeAllImpl((Set) set, collection.iterator());
        }
        return Iterators.removeAll(set.iterator(), collection);
    }
}
