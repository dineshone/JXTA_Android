/*
 * Written by Dawid Kurzyniec, on the basis of public specifications and
 * public domain sources from JSR 166 and the Doug Lea's collections package,
 * and released to the public domain,
 * as explained at http://creativecommons.org/licenses/publicdomain.
 */

package net.jxta.impl.util.backport.java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

/**
 * Sorted map implementation based on a red-black tree and implementing
 * all the methods from the NavigableMap interface.
 *
 * @author Dawid Kurzyniec
 */
@SuppressWarnings("unchecked")
public class TreeMap extends AbstractMap
                     implements NavigableMap, Serializable {

    private static final long serialVersionUID = 919286545866124006L;

    private final Comparator comparator;

    private transient Entry root;

    private transient int size = 0;
    private transient int modCount = 0;

    private transient EntrySet entrySet;
    private transient KeySet navigableKeySet;
    private transient NavigableMap descendingMap;
    private transient Comparator reverseComparator;

    public TreeMap() {
        this.comparator = null;
    }

    public TreeMap(Comparator comparator) {
        this.comparator = comparator;
    }

    public TreeMap(SortedMap map) {
        this.comparator = map.comparator();
        this.buildFromSorted(map.entrySet().iterator(), map.size());
    }

    public TreeMap(Map map) {
        this.comparator = null;
        putAll(map);
    }

    @Override
	public int size() { return size; }

    @Override
	public void clear() {
        root = null;
        size = 0;
        modCount++;
    }

    @Override
	public Object clone() {
        TreeMap clone;
        try { clone = (TreeMap)super.clone(); }
        catch (CloneNotSupportedException e) { throw new InternalError(); }
        clone.root = null;
        clone.size = 0;
        clone.modCount = 0;
        if (!isEmpty()) {
            clone.buildFromSorted(this.entrySet().iterator(), this.size);
        }
        return clone;
    }

    @Override
	public Object put(Object key, Object value) {
        if (root == null) {
            root = new Entry(key, value);
            size++;
            modCount++;
            return null;
        }
        else {
            Entry t = root;
            for (;;) {
                int diff = compare(key, t.getKey(), comparator);
                if (diff == 0) return t.setValue(value);
                else if (diff <= 0) {
                    if (t.left != null) t = t.left;
                    else {
                        size++;
                        modCount++;
                        Entry e = new Entry(key, value);
                        e.parent = t;
                        t.left = e;
                        fixAfterInsertion(e);
                        return null;
                    }
                }
                else {
                    if (t.right != null) t = t.right;
                    else {
                        size++;
                        modCount++;
                        Entry e = new Entry(key, value);
                        e.parent = t;
                        t.right = e;
                        fixAfterInsertion(e);
                        return null;
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
	public Object get(Object key) {
        Entry entry = getEntry(key);
        return (entry == null) ? null : entry.getValue();
    }

    @Override
	public boolean containsKey(Object key) {
        return getEntry(key) != null;
    }

    @Override
	public Set entrySet() {
        if (entrySet == null) {
            entrySet = new EntrySet();
        }
        return entrySet;
    }

    public static class Entry
        implements Map.Entry, Cloneable, java.io.Serializable {

        private static final boolean RED = false;
        private static final boolean BLACK = true;

        private Object key;
        private Object element;

        /**
         * The node color (RED, BLACK)
         */
        private boolean color;

        /**
         * Pointer to left child
         */
        private Entry left;

        /**
         * Pointer to right child
         */
        private Entry right;

        /**
         * Pointer to parent (null if root)
         */
        private Entry parent;

        /**
         * Make a new node with given element, null links, and BLACK color.
         * Normally only called to establish a new root.
         */
        public Entry(Object key, Object element) {
            this.key = key;
            this.element = element;
            this.color = BLACK;
        }

        /**
         * Return a new Entry with same element and color as self,
         * but with null links. (Since it is never OK to have
         * multiple identical links in a RB tree.)
         */
        @Override
		protected Object clone() throws CloneNotSupportedException {
            Entry t = new Entry(key, element);
            t.color = color;
            return t;
        }

        @Override
		public final Object getKey() {
            return key;
        }

        /**
         * return the element value
         */
        @Override
		public final Object getValue() {
            return element;
        }

        /**
         * set the element value
         */
        @Override
		public final Object setValue(Object v) {
            Object old = element;
            element = v;
            return old;
        }

        @Override
		public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) return false;
            Map.Entry e = (Map.Entry)o;
            return eq(key, e.getKey()) && eq(element, e.getValue());
        }

        @Override
		public int hashCode() {
            return (key == null ? 0 : key.hashCode()) ^
                   (element == null ? 0 : element.hashCode());
        }

        @Override
		public String toString() {
            return key + "=" + element;
        }
    }

    /**
     * Return the inorder successor, or null if no such
     */
    private static Entry successor(Entry e) {
        if (e.right != null) {
            for (e = e.right; e.left != null; e = e.left) {}
            return e;
        } else {
            Entry p = e.parent;
            while (p != null && e == p.right) {
                e = p;
                p = p.parent;
            }
            return p;
        }
    }

    /**
     * Return the inorder predecessor, or null if no such
     */
    private static Entry predecessor(Entry e) {
        if (e.left != null) {
            for (e = e.left; e.right != null; e = e.right) {}
            return e;
        }
        else {
            Entry p = e.parent;
            while (p != null && e == p.left) {
                e = p;
                p = p.parent;
            }
            return p;
        }
    }

    private Entry getEntry(Object key) {
        Entry t = root;
        if (comparator != null) {
            for (;;) {
                if (t == null) return null;
                int diff = comparator.compare(key, t.key);
                if (diff == 0) return t;
                t = (diff < 0) ? t.left : t.right;
            }
        }
        else {
            Comparable c = (Comparable)key;
            for (;;) {
                if (t == null) return null;
                int diff = c.compareTo(t.key);
                if (diff == 0) return t;
                t = (diff < 0) ? t.left : t.right;
            }
        }
    }

    private Entry getHigherEntry(Object key) {
        Entry t = root;
        if (t == null) return null;
        for (;;) {
            int diff = compare(key, t.key, comparator);
            if (diff < 0) {
                if (t.left != null) t = t.left; else return t;
            }
            else {
                if (t.right != null) {
                    t = t.right;
                }
                else {
                    Entry parent = t.parent;
                    while (parent != null && t == parent.right) {
                        t = parent;
                        parent = parent.parent;
                    }
                    return parent;
                }
            }
        }
    }

    private Entry getFirstEntry() {
        Entry e = root;
        if (e == null) return null;
        while (e.left != null) e = e.left;
        return e;
    }

    private Entry getLastEntry() {
        Entry e = root;
        if (e == null) return null;
        while (e.right != null) e = e.right;
        return e;
    }

    private Entry getCeilingEntry(Object key) {
        Entry e = root;
        if (e == null) return null;
        for (;;) {
            int diff = compare(key, e.key, comparator);
            if (diff < 0) {
                if (e.left != null) e = e.left; else return e;
            }
            else if (diff > 0) {
                if (e.right != null) {
                    e = e.right;
                }
                else {
                    Entry p = e.parent;
                    while (p != null && e == p.right) {
                        e = p;
                        p = p.parent;
                    }
                    return p;
                }
            }
            else return e;
        }
    }

    private Entry getLowerEntry(Object key) {
        Entry e = root;
        if (e == null) return null;
        for (;;) {
            int diff = compare(key, e.key, comparator);
            if (diff > 0) {
                if (e.right != null) e = e.right; else return e;
            }
            else {
                if (e.left != null) {
                    e = e.left;
                }
                else {
                    Entry p = e.parent;
                    while (p != null && e == p.left) {
                        e = p;
                        p = p.parent;
                    }
                    return p;
                }
            }
        }
    }

    private Entry getFloorEntry(Object key) {
        Entry e = root;
        if (e == null) return null;
        for (;;) {
            int diff = compare(key, e.key, comparator);
            if (diff > 0) {
                if (e.right != null) e = e.right; else return e;
            }
            else if (diff < 0) {
                if (e.left != null) {
                    e = e.left;
                }
                else {
                    Entry p = e.parent;
                    while (p != null && e == p.left) {
                        e = p;
                        p = p.parent;
                    }
                    return p;
                }
            }
            else return e;
        }
    }

    void buildFromSorted(Iterator itr, int size) {
        modCount++;
        this.size = size;
        // nodes at the bottom (unbalanced) level must be red
        int bottom = 0;
        for (int ssize = 1; ssize-1 < size; ssize <<= 1) bottom++;
        this.root = createFromSorted(itr, size, 0, bottom);
    }

    private static Entry createFromSorted(Iterator itr, int size,
                                          int level, int bottom) {
        level++;
        if (size == 0) return null;
        int leftSize = (size-1) >> 1;
        int rightSize = size-1-leftSize;
        Entry left = createFromSorted(itr, leftSize, level, bottom);
        Map.Entry orig = (Map.Entry)itr.next();
        Entry right = createFromSorted(itr, rightSize, level, bottom);
        Entry e = new Entry(orig.getKey(), orig.getValue());
        if (left != null) {
            e.left = left;
            left.parent = e;
        }
        if (right != null) {
            e.right = right;
            right.parent = e;
        }
        if (level == bottom) e.color = Entry.RED;
        return e;
    }

    /**
     * Delete the current node, and then rebalance the tree it is in
     * @param root the root of the current tree
     * @return the new root of the current tree. (Rebalancing
     * can change the root!)
     */
    private void delete(Entry e) {

        // handle case where we are only node
        if (e.left == null && e.right == null && e.parent == null) {
            root = null;
            size = 0;
            modCount++;
            return;
        }
        // if strictly internal, swap places with a successor
        if (e.left != null && e.right != null) {
            Entry s = successor(e);
            e.key = s.key;
            e.element = s.element;
            e = s;
        }

        // Start fixup at replacement node (normally a child).
        // But if no children, fake it by using self

        if (e.left == null && e.right == null) {

            if (e.color == Entry.BLACK)
                fixAfterDeletion(e);

            // Unlink  (Couldn't before since fixAfterDeletion needs parent ptr)

            if (e.parent != null) {
                if (e == e.parent.left)
                    e.parent.left = null;
                else if (e == e.parent.right)
                    e.parent.right = null;
                e.parent = null;
            }

        }
        else {
            Entry replacement = e.left;
            if (replacement == null)
                replacement = e.right;

            // link replacement to parent
            replacement.parent = e.parent;

            if (e.parent == null)
                root = replacement;
            else if (e == e.parent.left)
                e.parent.left = replacement;
            else
                e.parent.right = replacement;

            e.left = null;
            e.right = null;
            e.parent = null;

            // fix replacement
            if (e.color == Entry.BLACK)
                fixAfterDeletion(replacement);

        }

        size--;
        modCount++;
    }

    /**
     * Return color of node p, or BLACK if p is null
     * (In the CLR version, they use
     * a special dummy `nil' node for such purposes, but that doesn't
     * work well here, since it could lead to creating one such special
     * node per real node.)
     *
     */
    static boolean colorOf(Entry p) {
        return (p == null) ? Entry.BLACK : p.color;
    }

    /**
     * return parent of node p, or null if p is null
     */
    static Entry parentOf(Entry p) {
        return (p == null) ? null : p.parent;
    }

    /**
     * Set the color of node p, or do nothing if p is null
     */
    private static void setColor(Entry p, boolean c) {
        if (p != null) p.color = c;
    }

    /**
     * return left child of node p, or null if p is null
     */
    private static Entry leftOf(Entry p) {
        return (p == null) ? null : p.left;
    }

    /**
     * return right child of node p, or null if p is null
     */
    private static Entry rightOf(Entry p) {
        return (p == null) ? null : p.right;
    }

    /** From CLR */
    private final void rotateLeft(Entry e) {
        Entry r = e.right;
        e.right = r.left;
        if (r.left != null)
            r.left.parent = e;
        r.parent = e.parent;
        if (e.parent == null) root = r;
        else if (e.parent.left == e)
            e.parent.left = r;
        else
            e.parent.right = r;
        r.left = e;
        e.parent = r;
    }

    /** From CLR */
    private final void rotateRight(Entry e) {
        Entry l = e.left;
        e.left = l.right;
        if (l.right != null)
            l.right.parent = e;
        l.parent = e.parent;
        if (e.parent == null) root = l;
        else if (e.parent.right == e)
            e.parent.right = l;
        else
            e.parent.left = l;
        l.right = e;
        e.parent = l;
    }

    /** From CLR */
    private final void fixAfterInsertion(Entry e) {
        e.color = Entry.RED;
        Entry x = e;

        while (x != null && x != root && x.parent.color == Entry.RED) {
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                Entry y = rightOf(parentOf(parentOf(x)));
                if (colorOf(y) == Entry.RED) {
                    setColor(parentOf(x), Entry.BLACK);
                    setColor(y, Entry.BLACK);
                    setColor(parentOf(parentOf(x)), Entry.RED);
                    x = parentOf(parentOf(x));
                }
                else {
                    if (x == rightOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateLeft(x);
                    }
                    setColor(parentOf(x), Entry.BLACK);
                    setColor(parentOf(parentOf(x)), Entry.RED);
                    if (parentOf(parentOf(x)) != null)
                        rotateRight(parentOf(parentOf(x)));
                }
            }
            else {
                Entry y = leftOf(parentOf(parentOf(x)));
                if (colorOf(y) == Entry.RED) {
                    setColor(parentOf(x), Entry.BLACK);
                    setColor(y, Entry.BLACK);
                    setColor(parentOf(parentOf(x)), Entry.RED);
                    x = parentOf(parentOf(x));
                }
                else {
                    if (x == leftOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateRight(x);
                    }
                    setColor(parentOf(x), Entry.BLACK);
                    setColor(parentOf(parentOf(x)), Entry.RED);
                    if (parentOf(parentOf(x)) != null)
                        rotateLeft(parentOf(parentOf(x)));
                }
            }
        }
        root.color = Entry.BLACK;
    }

    /** From CLR */
    private final Entry fixAfterDeletion(Entry e) {
        Entry x = e;
        while (x != root && colorOf(x) == Entry.BLACK) {
            if (x == leftOf(parentOf(x))) {
                Entry sib = rightOf(parentOf(x));
                if (colorOf(sib) == Entry.RED) {
                    setColor(sib, Entry.BLACK);
                    setColor(parentOf(x), Entry.RED);
                    rotateLeft(parentOf(x));
                    sib = rightOf(parentOf(x));
                }
                if (colorOf(leftOf(sib)) == Entry.BLACK &&
                    colorOf(rightOf(sib)) == Entry.BLACK) {
                    setColor(sib, Entry.RED);
                    x = parentOf(x);
                }
                else {
                    if (colorOf(rightOf(sib)) == Entry.BLACK) {
                        setColor(leftOf(sib), Entry.BLACK);
                        setColor(sib, Entry.RED);
                        rotateRight(sib);
                        sib = rightOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), Entry.BLACK);
                    setColor(rightOf(sib), Entry.BLACK);
                    rotateLeft(parentOf(x));
                    x = root;
                }
            }
            else {
                Entry sib = leftOf(parentOf(x));
                if (colorOf(sib) == Entry.RED) {
                    setColor(sib, Entry.BLACK);
                    setColor(parentOf(x), Entry.RED);
                    rotateRight(parentOf(x));
                    sib = leftOf(parentOf(x));
                }
                if (colorOf(rightOf(sib)) == Entry.BLACK &&
                    colorOf(leftOf(sib)) == Entry.BLACK) {
                    setColor(sib, Entry.RED);
                    x = parentOf(x);
                }
                else {
                    if (colorOf(leftOf(sib)) == Entry.BLACK) {
                        setColor(rightOf(sib), Entry.BLACK);
                        setColor(sib, Entry.RED);
                        rotateLeft(sib);
                        sib = leftOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), Entry.BLACK);
                    setColor(leftOf(sib), Entry.BLACK);
                    rotateRight(parentOf(x));
                    x = root;
                }
            }
        }
        setColor(x, Entry.BLACK);
        return root;
    }

    private class BaseEntryIterator {
        Entry cursor;
        Entry lastRet;
        int expectedModCount;
        BaseEntryIterator(Entry cursor) {
            this.cursor = cursor;
            this.expectedModCount = modCount;
        }
        public boolean hasNext() {
            return (cursor != null);
        }
        Entry nextEntry() {
            Entry curr = cursor;
            if (curr == null) throw new NoSuchElementException();
            if (expectedModCount != modCount)
                throw new ConcurrentModificationException();
            cursor = successor(curr);
            lastRet = curr;
            return curr;
        }
        Entry prevEntry() {
            Entry curr = cursor;
            if (curr == null) throw new NoSuchElementException();
            if (expectedModCount != modCount)
                throw new ConcurrentModificationException();
            cursor = predecessor(curr);
            lastRet = curr;
            return curr;
        }
        public void remove() {
            if (lastRet == null) throw new IllegalStateException();
            if (expectedModCount != modCount)
                throw new ConcurrentModificationException();
            // if removal strictly internal, it swaps places with a successor
            if (lastRet.left != null && lastRet.right != null && cursor != null) cursor = lastRet;
            delete(lastRet);
            lastRet = null;
            expectedModCount++;
        }
    }

    class EntryIterator extends BaseEntryIterator implements Iterator {
        EntryIterator(Entry cursor) { super(cursor); }
        @Override
		public Object next() { return nextEntry(); }
    }

    class KeyIterator extends BaseEntryIterator implements Iterator {
        KeyIterator(Entry cursor) { super(cursor); }
        @Override
		public Object next() { return nextEntry().key; }
    }

    class ValueIterator extends BaseEntryIterator implements Iterator {
        ValueIterator(Entry cursor) { super(cursor); }
        @Override
		public Object next() { return nextEntry().element; }
    }

    class DescendingEntryIterator extends BaseEntryIterator implements Iterator {
        DescendingEntryIterator(Entry cursor) { super(cursor); }
        @Override
		public Object next() { return prevEntry(); }
    }

    class DescendingKeyIterator extends BaseEntryIterator implements Iterator {
        DescendingKeyIterator(Entry cursor) { super(cursor); }
        @Override
		public Object next() { return prevEntry().key; }
    }

    class DescendingValueIterator extends BaseEntryIterator implements Iterator {
        DescendingValueIterator(Entry cursor) { super(cursor); }
        @Override
		public Object next() { return prevEntry().element; }
    }

    private Entry getMatchingEntry(Object o) {
        if (!(o instanceof Map.Entry)) return null;
        Map.Entry e = (Map.Entry)o;
        Entry found = TreeMap.this.getEntry(e.getKey());
        return (found != null && eq(found.getValue(), e.getValue())) ? found : null;
    }

    class EntrySet extends AbstractSet {
        @Override
		public int size() { return TreeMap.this.size(); }
        @Override
		public boolean isEmpty() { return TreeMap.this.isEmpty(); }
        @Override
		public void clear() { TreeMap.this.clear(); }

        @Override
		public Iterator iterator() {
            return new EntryIterator(getFirstEntry());
        }

        @Override
		public boolean contains(Object o) {
            return getMatchingEntry(o) != null;
        }

        @Override
		public boolean remove(Object o) {
            Entry e = getMatchingEntry(o);
            if (e == null) return false;
            delete(e);
            return true;
        }
    }

    class DescendingEntrySet extends EntrySet {
        @Override
		public Iterator iterator() {
            return new DescendingEntryIterator(getLastEntry());
        }
    }

    class ValueSet extends AbstractSet {
        @Override
		public int size() { return TreeMap.this.size(); }
        @Override
		public boolean isEmpty() { return TreeMap.this.isEmpty(); }
        @Override
		public void clear() { TreeMap.this.clear(); }

        @Override
		public boolean contains(Object o) {
            for (Entry e = getFirstEntry(); e != null; e = successor(e)) {
                if (eq(o, e.element)) return true;
            }
            return false;
        }

        @Override
		public Iterator iterator() {
            return new ValueIterator(getFirstEntry());
        }

        @Override
		public boolean remove(Object o) {
            for (Entry e = getFirstEntry(); e != null; e = successor(e)) {
                if (eq(o, e.element)) {
                    delete(e);
                    return true;
                }
            }
            return false;
        }
    }

    abstract class KeySet extends AbstractSet implements NavigableSet {
        @Override
		public int size() { return TreeMap.this.size(); }
        @Override
		public boolean isEmpty() { return TreeMap.this.isEmpty(); }
        @Override
		public void clear() { TreeMap.this.clear(); }

        @Override
		public boolean contains(Object o) {
            return getEntry(o) != null;
        }

        @Override
		public boolean remove(Object o) {
            Entry found = getEntry(o);
            if (found == null) return false;
            delete(found);
            return true;
        }
        @Override
		public SortedSet subSet(Object fromElement, Object toElement) {
            return subSet(fromElement, true, toElement, false);
        }
        @Override
		public SortedSet headSet(Object toElement) {
            return headSet(toElement, false);
        }
        @Override
		public SortedSet tailSet(Object fromElement) {
            return tailSet(fromElement, true);
        }
    }

    class AscendingKeySet extends KeySet {

        @Override
		public Iterator iterator() {
            return new KeyIterator(getFirstEntry());
        }

        @Override
		public Iterator descendingIterator() {
            return new DescendingKeyIterator(getFirstEntry());
        }

        @Override
		public Object lower(Object e)   { return lowerKey(e); }
        @Override
		public Object floor(Object e)   { return floorKey(e); }
        @Override
		public Object ceiling(Object e) { return ceilingKey(e); }
        @Override
		public Object higher(Object e)  { return higherKey(e); }
        @Override
		public Object first()           { return firstKey(); }
        @Override
		public Object last()            { return lastKey(); }
        @Override
		public Comparator comparator()  { return TreeMap.this.comparator(); }

        @Override
		public Object pollFirst() {
            Map.Entry e = pollFirstEntry();
            return e == null? null : e.getKey();
        }
        @Override
		public Object pollLast() {
            Map.Entry e = pollLastEntry();
            return e == null? null : e.getKey();
        }

        @Override
		public NavigableSet subSet(Object fromElement, boolean fromInclusive,
                                   Object toElement,   boolean toInclusive) {
            return (NavigableSet)(subMap(fromElement, fromInclusive,
                                         toElement,   toInclusive)).keySet();
        }
        @Override
		public NavigableSet headSet(Object toElement, boolean inclusive) {
            return (NavigableSet)(headMap(toElement, inclusive)).keySet();
        }
        @Override
		public NavigableSet tailSet(Object fromElement, boolean inclusive) {
            return (NavigableSet)(tailMap(fromElement, inclusive)).keySet();
        }
        @Override
		public NavigableSet descendingSet() {
            return (NavigableSet)descendingMap().keySet();
        }
    }

    class DescendingKeySet extends KeySet {

        @Override
		public Iterator iterator() {
            return new DescendingKeyIterator(getLastEntry());
        }

        @Override
		public Iterator descendingIterator() {
            return new KeyIterator(getFirstEntry());
        }

        @Override
		public Object lower(Object e)   { return higherKey(e); }
        @Override
		public Object floor(Object e)   { return ceilingKey(e); }
        @Override
		public Object ceiling(Object e) { return floorKey(e); }
        @Override
		public Object higher(Object e)  { return lowerKey(e); }
        @Override
		public Object first()           { return lastKey(); }
        @Override
		public Object last()            { return firstKey(); }
        @Override
		public Comparator comparator()  { return descendingMap().comparator(); }

        @Override
		public Object pollFirst() {
            Map.Entry e = pollLastEntry();
            return e == null? null : e.getKey();
        }
        @Override
		public Object pollLast() {
            Map.Entry e = pollFirstEntry();
            return e == null? null : e.getKey();
        }

        @Override
		public NavigableSet subSet(Object fromElement, boolean fromInclusive,
                                   Object toElement,   boolean toInclusive) {
            return (NavigableSet)(descendingMap().subMap(fromElement, fromInclusive,
                                          toElement,   toInclusive)).keySet();
        }
        @Override
		public NavigableSet headSet(Object toElement, boolean inclusive) {
            return (NavigableSet)(descendingMap().headMap(toElement, inclusive)).keySet();
        }
        @Override
		public NavigableSet tailSet(Object fromElement, boolean inclusive) {
            return (NavigableSet)(descendingMap().tailMap(fromElement, inclusive)).keySet();
        }
        @Override
		public NavigableSet descendingSet() {
            return (NavigableSet)keySet();
        }
    }

    private static boolean eq(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }

    private static int compare(Object o1, Object o2, Comparator cmp) {
        return (cmp == null)
            ? ((Comparable)o1).compareTo(o2)
            : cmp.compare(o1, o2);
    }

    /**
     * @since 1.6
     */
    @Override
	public Map.Entry lowerEntry(Object key) {
        Map.Entry e = getLowerEntry(key);
        return (e == null) ? null : new AbstractMap.SimpleImmutableEntry(e);
    }

    /**
     * @since 1.6
     */
    @Override
	public Object lowerKey(Object key) {
        Map.Entry e = getLowerEntry(key);
        return (e == null) ? null : e.getKey();
    }

    /**
     * @since 1.6
     */
    @Override
	public Map.Entry floorEntry(Object key) {
        Entry e = getFloorEntry(key);
        return (e == null) ? null : new AbstractMap.SimpleImmutableEntry(e);
    }

    /**
     * @since 1.6
     */
    @Override
	public Object floorKey(Object key) {
        Entry e = getFloorEntry(key);
        return (e == null) ? null : e.key;
    }

    /**
     * @since 1.6
     */
    @Override
	public Map.Entry ceilingEntry(Object key) {
        Entry e = getCeilingEntry(key);
        return (e == null) ? null : new AbstractMap.SimpleImmutableEntry(e);
    }

    /**
     * @since 1.6
     */
    @Override
	public Object ceilingKey(Object key) {
        Entry e = getCeilingEntry(key);
        return (e == null) ? null : e.key;
    }

    /**
     * @since 1.6
     */
    @Override
	public Map.Entry higherEntry(Object key) {
        Entry e = getHigherEntry(key);
        return (e == null) ? null : new AbstractMap.SimpleImmutableEntry(e);
    }

    /**
     * @since 1.6
     */
    @Override
	public Object higherKey(Object key) {
        Entry e = getHigherEntry(key);
        return (e == null) ? null : e.key;
    }

    /**
     * @since 1.6
     */
    @Override
	public Map.Entry firstEntry() {
        Entry e = getFirstEntry();
        return (e == null) ? null : new AbstractMap.SimpleImmutableEntry(e);
    }

    /**
     * @since 1.6
     */
    @Override
	public Map.Entry lastEntry() {
        Entry e = getLastEntry();
        return (e == null) ? null : new AbstractMap.SimpleImmutableEntry(e);
    }

    /**
     * @since 1.6
     */
    @Override
	public Map.Entry pollFirstEntry() {
        Entry e = getFirstEntry();
        if (e == null) return null;
        Map.Entry res = new AbstractMap.SimpleImmutableEntry(e);
        delete(e);
        return res;
    }

    /**
     * @since 1.6
     */
    @Override
	public Map.Entry pollLastEntry() {
        Entry e = getLastEntry();
        if (e == null) return null;
        Map.Entry res = new AbstractMap.SimpleImmutableEntry(e);
        delete(e);
        return res;
    }

    /**
     * @since 1.6
     */
    @Override
	public NavigableMap descendingMap() {
        NavigableMap map = descendingMap;
        if (map == null) {
            descendingMap = map = new DescendingSubMap(true, null, true,
                                                       true, null, true);
        }
        return map;
    }

    @Override
	public NavigableSet descendingKeySet() {
        return descendingMap().navigableKeySet();
    }

    @Override
	public SortedMap subMap(Object fromKey, Object toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    @Override
	public SortedMap headMap(Object toKey) {
        return headMap(toKey, false);
    }

    @Override
	public SortedMap tailMap(Object fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
	public NavigableMap subMap(Object fromKey, boolean fromInclusive,
                               Object toKey,   boolean toInclusive) {
        return new AscendingSubMap(false, fromKey, fromInclusive,
                                   false, toKey, toInclusive);
    }

    @Override
	public NavigableMap headMap(Object toKey, boolean toInclusive) {
        return new AscendingSubMap(true,  null,  true,
                                   false, toKey, toInclusive);
    }

    @Override
	public NavigableMap tailMap(Object fromKey, boolean fromInclusive) {
        return new AscendingSubMap(false, fromKey, fromInclusive,
                                   true,  null,    true);
    }

    @Override
	public Comparator comparator() {
        return comparator;
    }

    final Comparator reverseComparator() {
        if (reverseComparator == null) {
            reverseComparator = Collections.reverseOrder(comparator);
        }
        return reverseComparator;
    }

    @Override
	public Object firstKey() {
        Entry e = getFirstEntry();
        if (e == null) throw new NoSuchElementException();
        return e.key;
    }

    @Override
	public Object lastKey() {
        Entry e = getLastEntry();
        if (e == null) throw new NoSuchElementException();
        return e.key;
    }

    @Override
	public boolean isEmpty() {
        return size == 0;
    }

    @Override
	public boolean containsValue(Object value) {
        if (root == null) return false;
        return (value == null) ? containsNull(root) : containsValue(root, value);
    }

    private static boolean containsNull(Entry e) {
        if (e.element == null) return true;
        if (e.left != null && containsNull(e.left)) return true;
        if (e.right != null && containsNull(e.right)) return true;
        return false;
    }

    private static boolean containsValue(Entry e, Object val) {
        if (val.equals(e.element)) return true;
        if (e.left != null && containsValue(e.left, val)) return true;
        if (e.right != null && containsValue(e.right, val)) return true;
        return false;
    }

    @Override
	public Object remove(Object key) {
        Entry e = getEntry(key);
        if (e == null) return null;
        Object old = e.getValue();
        delete(e);
        return old;
    }

    @Override
	public void putAll(Map map) {
        if (map instanceof SortedMap) {
            SortedMap smap = (SortedMap)map;
            if (eq(this.comparator, smap.comparator())) {
                this.buildFromSorted(smap.entrySet().iterator(), map.size());
                return;
            }
        }
        // not a sorted map, or comparator mismatch
        super.putAll(map);
    }

    @Override
	public Set keySet() {
        return navigableKeySet();
    }

    @Override
	public NavigableSet navigableKeySet() {
        if (navigableKeySet == null) {
            navigableKeySet = new AscendingKeySet();
        }
        return navigableKeySet;
    }

//    public Collection values() {
//        if (valueSet == null) {
//            valueSet = new ValueSet();
//        }
//        return valueSet;
//    }
//
    private abstract class NavigableSubMap extends AbstractMap
                                           implements NavigableMap, Serializable {

        private static final long serialVersionUID = -6520786458950516097L;

        final Object fromKey, toKey;
        final boolean fromStart, toEnd;
        final boolean fromInclusive, toInclusive;
        transient int cachedSize = -1, cacheVersion;
        transient SubEntrySet entrySet;
        transient NavigableMap descendingMap;
        transient NavigableSet navigableKeySet;

        NavigableSubMap(boolean fromStart, Object fromKey, boolean fromInclusive,
                        boolean toEnd,     Object toKey,   boolean toInclusive) {
            if (!fromStart && !toEnd) {
                if (compare(fromKey, toKey, comparator) > 0) {
                    throw new IllegalArgumentException("fromKey > toKey");
                }
            }
            else {
                if (!fromStart) compare(fromKey, fromKey, comparator);
                if (!toEnd) compare(toKey, toKey, comparator);
            }
            this.fromStart = fromStart;
            this.toEnd = toEnd;
            this.fromKey = fromKey;
            this.toKey = toKey;
            this.fromInclusive = fromInclusive;
            this.toInclusive = toInclusive;
        }

        final TreeMap.Entry checkLoRange(TreeMap.Entry e) {
            return (e == null || absTooLow(e.key)) ? null : e;
        }

        final TreeMap.Entry checkHiRange(TreeMap.Entry e) {
            return (e == null || absTooHigh(e.key)) ? null : e;
        }

        final boolean inRange(Object key) {
            return !absTooLow(key) && !absTooHigh(key);
        }

        final boolean inRangeExclusive(Object key) {
            return (fromStart || compare(key, fromKey, comparator) >= 0)
                && (toEnd     || compare(toKey, key, comparator) >= 0);
        }

        final boolean inRange(Object key, boolean inclusive) {
            return inclusive ? inRange(key) : inRangeExclusive(key);
        }

        private boolean absTooHigh(Object key) {
            if (toEnd) return false;
            int c = compare(key, toKey, comparator);
            return (c > 0 || (c == 0 && !toInclusive));
        }

        private boolean absTooLow(Object key) {
            if (fromStart) return false;
            int c = compare(key, fromKey, comparator);
            return (c < 0 || (c == 0 && !fromInclusive));
        }

        protected abstract TreeMap.Entry first();
        protected abstract TreeMap.Entry last();
        protected abstract TreeMap.Entry lower(Object key);
        protected abstract TreeMap.Entry floor(Object key);
        protected abstract TreeMap.Entry ceiling(Object key);
        protected abstract TreeMap.Entry higher(Object key);
        protected abstract TreeMap.Entry uncheckedHigher(TreeMap.Entry e);

        // absolute comparisons, for use by subclasses

        final TreeMap.Entry absLowest() {
            return checkHiRange((fromStart) ? getFirstEntry() :
                fromInclusive ? getCeilingEntry(fromKey) : getHigherEntry(fromKey));
        }

        final TreeMap.Entry absHighest() {
            return checkLoRange((toEnd) ? getLastEntry() :
                toInclusive ? getFloorEntry(toKey) : getLowerEntry(toKey));
        }

        final TreeMap.Entry absLower(Object key) {
            return absTooHigh(key) ? absHighest() : checkLoRange(getLowerEntry(key));
        }

        final TreeMap.Entry absFloor(Object key) {
            return absTooHigh(key) ? absHighest() : checkLoRange(getFloorEntry(key));
        }

        final TreeMap.Entry absCeiling(Object key) {
            return absTooLow(key) ? absLowest() : checkHiRange(getCeilingEntry(key));
        }

        final TreeMap.Entry absHigher(Object key) {
            return absTooLow(key) ? absLowest() : checkHiRange(getHigherEntry(key));
        }

        // navigable implementations, using subclass-defined comparisons

        @Override
		public Map.Entry firstEntry() {
            TreeMap.Entry e = first();
            return (e == null) ? null : new AbstractMap.SimpleImmutableEntry(e);
        }

        @Override
		public Object firstKey() {
            TreeMap.Entry e = first();
            if (e == null) throw new NoSuchElementException();
            return e.key;
        }

        @Override
		public Map.Entry lastEntry() {
            TreeMap.Entry e = last();
            return (e == null) ? null : new AbstractMap.SimpleImmutableEntry(e);
        }

        @Override
		public Object lastKey() {
            TreeMap.Entry e = last();
            if (e == null) throw new NoSuchElementException();
            return e.key;
        }

        @Override
		public Map.Entry pollFirstEntry() {
            TreeMap.Entry e = first();
            if (e == null) return null;
            Map.Entry result = new SimpleImmutableEntry(e);
            delete(e);
            return result;
        }

        @Override
		public java.util.Map.Entry pollLastEntry() {
            TreeMap.Entry e = last();
            if (e == null) return null;
            Map.Entry result = new SimpleImmutableEntry(e);
            delete(e);
            return result;
        }

        @Override
		public Map.Entry lowerEntry(Object key) {
            TreeMap.Entry e = lower(key);
            return (e == null) ? null : new AbstractMap.SimpleImmutableEntry(e);
        }

        @Override
		public Object lowerKey(Object key) {
            TreeMap.Entry e = lower(key);
            return (e == null) ? null : e.key;
        }

        @Override
		public Map.Entry floorEntry(Object key) {
            TreeMap.Entry e = floor(key);
            return (e == null) ? null : new AbstractMap.SimpleImmutableEntry(e);
        }

        @Override
		public Object floorKey(Object key) {
            TreeMap.Entry e = floor(key);
            return (e == null) ? null : e.key;
        }

        @Override
		public Map.Entry ceilingEntry(Object key) {
            TreeMap.Entry e = ceiling(key);
            return (e == null) ? null : new AbstractMap.SimpleImmutableEntry(e);
        }

        @Override
		public Object ceilingKey(Object key) {
            TreeMap.Entry e = ceiling(key);
            return (e == null) ? null : e.key;
        }

        @Override
		public Map.Entry higherEntry(Object key) {
            TreeMap.Entry e = higher(key);
            return (e == null) ? null : new AbstractMap.SimpleImmutableEntry(e);
        }

        @Override
		public Object higherKey(Object key) {
            TreeMap.Entry e = higher(key);
            return (e == null) ? null : e.key;
        }

        @Override
		public NavigableSet descendingKeySet() {
            return descendingMap().navigableKeySet();
        }

        @Override
		public SortedMap subMap(Object fromKey, Object toKey) {
            return subMap(fromKey, true, toKey, false);
        }

        @Override
		public SortedMap headMap(Object toKey) {
            return headMap(toKey, false);
        }

        @Override
		public SortedMap tailMap(Object fromKey) {
            return tailMap(fromKey, true);
        }

        @Override
		public int size() {
            if (cachedSize < 0 || cacheVersion != modCount) {
                cachedSize = recalculateSize();
                cacheVersion = modCount;
            }
            return cachedSize;
        }

        private int recalculateSize() {
            TreeMap.Entry terminator = absHighest();
            Object terminalKey = terminator != null ? terminator.key : null;

            int size = 0;
            for (TreeMap.Entry e = absLowest(); e != null;
                 e = (e.key == terminalKey) ? null : successor(e)) {
                size++;
            }
            return size;
        }

        @Override
		public boolean isEmpty() {
            return absLowest() == null;
        }

        @Override
		public boolean containsKey(Object key) {
            return (inRange(key) && TreeMap.this.containsKey(key));
        }

        @Override
		public Object get(Object key) {
            if (!inRange(key)) return null;
            else return TreeMap.this.get(key);
        }

        @Override
		public Object put(Object key, Object value) {
            if (!inRange(key))
                throw new IllegalArgumentException("Key out of range");
            return TreeMap.this.put(key, value);
        }

        @Override
		public Object remove(Object key) {
            if (!inRange(key)) return null;
            return TreeMap.this.remove(key);
        }

        @Override
		public Set entrySet() {
            if (entrySet == null) {
                entrySet = new SubEntrySet();
            }
            return entrySet;
        }

        @Override
		public Set keySet() {
            return navigableKeySet();
        }

        @Override
		public NavigableSet navigableKeySet() {
            if (navigableKeySet == null) {
                navigableKeySet = new SubKeySet();
            }
            return navigableKeySet;
        }

        private TreeMap.Entry getMatchingSubEntry(Object o) {
            if (!(o instanceof Map.Entry)) return null;
            Map.Entry e = (Map.Entry)o;
            Object key = e.getKey();
            if (!inRange(key)) return null;
            TreeMap.Entry found = getEntry(key);
            return (found != null && eq(found.getValue(), e.getValue())) ? found : null;
        }

        class SubEntrySet extends AbstractSet {
            @Override
			public int size() { return NavigableSubMap.this.size(); }
            @Override
			public boolean isEmpty() { return NavigableSubMap.this.isEmpty(); }

            @Override
			public boolean contains(Object o) {
                return getMatchingSubEntry(o) != null;
            }

            @Override
			public boolean remove(Object o) {
                TreeMap.Entry e = getMatchingSubEntry(o);
                if (e == null) return false;
                delete(e);
                return true;
            }

            @Override
			public Iterator iterator() {
                return new SubEntryIterator();
            }
        }

        class SubKeySet extends AbstractSet implements NavigableSet {
            @Override
			public int size() { return NavigableSubMap.this.size(); }
            @Override
			public boolean isEmpty() { return NavigableSubMap.this.isEmpty(); }
            @Override
			public void clear() { NavigableSubMap.this.clear(); }

            @Override
			public boolean contains(Object o) {
                return getEntry(o) != null;
            }

            @Override
			public boolean remove(Object o) {
                if (!inRange(o)) return false;
                TreeMap.Entry found = getEntry(o);
                if (found == null) return false;
                delete(found);
                return true;
            }
            @Override
			public SortedSet subSet(Object fromElement, Object toElement) {
                return subSet(fromElement, true, toElement, false);
            }
            @Override
			public SortedSet headSet(Object toElement) {
                return headSet(toElement, false);
            }
            @Override
			public SortedSet tailSet(Object fromElement) {
                return tailSet(fromElement, true);
            }

            @Override
			public Iterator iterator() {
                return new SubKeyIterator(NavigableSubMap.this.entrySet().iterator());
            }

            @Override
			public Iterator descendingIterator() {
                return new SubKeyIterator(NavigableSubMap.this.descendingMap().entrySet().iterator());
            }

            @Override
			public Object lower(Object e)   { return NavigableSubMap.this.lowerKey(e); }
            @Override
			public Object floor(Object e)   { return NavigableSubMap.this.floorKey(e); }
            @Override
			public Object ceiling(Object e) { return NavigableSubMap.this.ceilingKey(e); }
            @Override
			public Object higher(Object e)  { return NavigableSubMap.this.higherKey(e); }
            @Override
			public Object first()           { return NavigableSubMap.this.firstKey(); }
            @Override
			public Object last()            { return NavigableSubMap.this.lastKey(); }
            @Override
			public Comparator comparator()  { return NavigableSubMap.this.comparator(); }

            @Override
			public Object pollFirst() {
                Map.Entry e = NavigableSubMap.this.pollFirstEntry();
                return e == null? null : e.getKey();
            }
            @Override
			public Object pollLast() {
                Map.Entry e = NavigableSubMap.this.pollLastEntry();
                return e == null? null : e.getKey();
            }

            @Override
			public NavigableSet subSet(Object fromElement, boolean fromInclusive,
                                       Object toElement,   boolean toInclusive) {
                return (NavigableSet)(NavigableSubMap.this.subMap(fromElement, fromInclusive,
                                             toElement,   toInclusive)).keySet();
            }
            @Override
			public NavigableSet headSet(Object toElement, boolean inclusive) {
                return (NavigableSet)(NavigableSubMap.this.headMap(toElement, inclusive)).keySet();
            }
            @Override
			public NavigableSet tailSet(Object fromElement, boolean inclusive) {
                return (NavigableSet)(NavigableSubMap.this.tailMap(fromElement, inclusive)).keySet();
            }
            @Override
			public NavigableSet descendingSet() {
                return (NavigableSet)NavigableSubMap.this.descendingMap().keySet();
            }
        }

        class SubEntryIterator extends BaseEntryIterator implements Iterator {
            final Object terminalKey;
            SubEntryIterator() {
                super(first());
                TreeMap.Entry terminator = last();
                this.terminalKey = terminator == null ? null : terminator.key;
            }
            @Override
			public boolean hasNext() {
                return cursor != null;
            }
            @Override
			public Object next() {
                TreeMap.Entry curr = cursor;
                if (curr == null) throw new NoSuchElementException();
                if (expectedModCount != modCount)
                    throw new ConcurrentModificationException();
                cursor = (curr.key == terminalKey) ? null : uncheckedHigher(curr);
                lastRet = curr;
                return curr;
            }
        }

        class SubKeyIterator implements Iterator {
            final Iterator itr;
            SubKeyIterator(Iterator itr) { this.itr = itr; }
            @Override
			public boolean hasNext()     { return itr.hasNext(); }
            @Override
			public Object next()         { return ((Map.Entry)itr.next()).getKey(); }
            @Override
			public void remove()         { itr.remove(); }
        }
    }

    class AscendingSubMap extends NavigableSubMap {
        AscendingSubMap(boolean fromStart, Object fromKey, boolean fromInclusive,
                        boolean toEnd,     Object toKey,   boolean toInclusive) {
            super(fromStart, fromKey, fromInclusive, toEnd, toKey, toInclusive);
        }

        @Override
		public Comparator comparator() {
            return comparator;
        }

        @Override
		protected TreeMap.Entry first()             { return absLowest(); }
        @Override
		protected TreeMap.Entry last()              { return absHighest(); }
        @Override
		protected TreeMap.Entry lower(Object key)   { return absLower(key); }
        @Override
		protected TreeMap.Entry floor(Object key)   { return absFloor(key); }
        @Override
		protected TreeMap.Entry ceiling(Object key) { return absCeiling(key); }
        @Override
		protected TreeMap.Entry higher(Object key)  { return absHigher(key); }

        @Override
		protected TreeMap.Entry uncheckedHigher(TreeMap.Entry e) {
            return successor(e);
        }

        @Override
		public NavigableMap subMap(Object fromKey, boolean fromInclusive,
                                   Object toKey, boolean toInclusive) {
            if (!inRange(fromKey, fromInclusive)) {
                throw new IllegalArgumentException("fromKey out of range");
            }
            if (!inRange(toKey, toInclusive)) {
                throw new IllegalArgumentException("toKey out of range");
            }
            return new AscendingSubMap(false, fromKey, fromInclusive,
                                       false, toKey, toInclusive);
        }

        @Override
		public NavigableMap headMap(Object toKey, boolean toInclusive) {
            if (!inRange(toKey, toInclusive)) {
                throw new IllegalArgumentException("toKey out of range");
            }
            return new AscendingSubMap(fromStart, fromKey, fromInclusive,
                                       false, toKey, toInclusive);
        }

        @Override
		public NavigableMap tailMap(Object fromKey, boolean fromInclusive) {
            if (!inRange(fromKey, fromInclusive)) {
                throw new IllegalArgumentException("fromKey out of range");
            }
            return new AscendingSubMap(false, fromKey, fromInclusive,
                                       toEnd, toKey, toInclusive);
        }

        @Override
		public NavigableMap descendingMap() {
            if (descendingMap == null) {
                descendingMap =
                    new DescendingSubMap(fromStart, fromKey, fromInclusive,
                                         toEnd,     toKey,   toInclusive);
            }
            return descendingMap;
        }
    }

    class DescendingSubMap extends NavigableSubMap {
        DescendingSubMap(boolean fromStart, Object fromKey, boolean fromInclusive,
                         boolean toEnd,     Object toKey,   boolean toInclusive) {
            super(fromStart, fromKey, fromInclusive, toEnd, toKey, toInclusive);
        }

        @Override
		public Comparator comparator() { return TreeMap.this.reverseComparator(); }

        @Override
		protected TreeMap.Entry first()             { return absHighest(); }
        @Override
		protected TreeMap.Entry last()              { return absLowest(); }
        @Override
		protected TreeMap.Entry lower(Object key)   { return absHigher(key); }
        @Override
		protected TreeMap.Entry floor(Object key)   { return absCeiling(key); }
        @Override
		protected TreeMap.Entry ceiling(Object key) { return absFloor(key); }
        @Override
		protected TreeMap.Entry higher(Object key)  { return absLower(key); }

        @Override
		protected TreeMap.Entry uncheckedHigher(TreeMap.Entry e) {
            return predecessor(e);
        }

        @Override
		public NavigableMap subMap(Object fromKey, boolean fromInclusive,
                                   Object toKey,   boolean toInclusive) {
            if (!inRange(fromKey, fromInclusive)) {
                throw new IllegalArgumentException("fromKey out of range");
            }
            if (!inRange(toKey, toInclusive)) {
                throw new IllegalArgumentException("toKey out of range");
            }
            return new DescendingSubMap(false, toKey, toInclusive,
                                        false, fromKey, fromInclusive);
        }

        @Override
		public NavigableMap headMap(Object toKey, boolean toInclusive) {
            if (!inRange(toKey, toInclusive)) {
                throw new IllegalArgumentException("toKey out of range");
            }
            return new DescendingSubMap(false, toKey, toInclusive,
                                        this.toEnd, this.toKey, this.toInclusive);
        }

        @Override
		public NavigableMap tailMap(Object fromKey, boolean fromInclusive) {
            if (!inRange(fromKey, fromInclusive)) {
                throw new IllegalArgumentException("fromKey out of range");
            }
            return new DescendingSubMap(this.fromStart, this.fromKey, this.fromInclusive,
                                        false, fromKey, fromInclusive);
        }

        @Override
		public NavigableMap descendingMap() {
            if (descendingMap == null) {
                descendingMap =
                    new AscendingSubMap(fromStart, fromKey, fromInclusive,
                                        toEnd,     toKey,   toInclusive);

            }
            return descendingMap;
        }
    }

    // serialization

    static class IteratorIOException extends RuntimeException {
        IteratorIOException(java.io.IOException e) {
            super(e);
        }
        java.io.IOException getException() {
            return (java.io.IOException)getCause();
        }
    }

    static class IteratorNoClassException extends RuntimeException {
        IteratorNoClassException(ClassNotFoundException e) {
            super(e);
        }
        ClassNotFoundException getException() {
            return (ClassNotFoundException)getCause();
        }
    }

    static class IOIterator implements Iterator {
        final java.io.ObjectInputStream ois;
        int remaining;
        IOIterator(java.io.ObjectInputStream ois, int remaining) {
            this.ois = ois;
            this.remaining = remaining;
        }
        @Override
		public boolean hasNext() {
            return remaining > 0;
        }
        @Override
		public Object next() {
            if (remaining <= 0) throw new NoSuchElementException();
            remaining--;
            try {
                return new AbstractMap.SimpleImmutableEntry(ois.readObject(),
                                                            ois.readObject());
            }
            catch (java.io.IOException e) { throw new IteratorIOException(e); }
            catch (ClassNotFoundException e) { throw new IteratorNoClassException(e); }
        }
        @Override
		public void remove() { throw new UnsupportedOperationException(); }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(size);
        for (Entry e = getFirstEntry(); e != null; e = successor(e)) {
            out.writeObject(e.key);
            out.writeObject(e.element);
        }
    }

    private void readObject(ObjectInputStream in)
        throws java.io.IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        int size = in.readInt();
        try {
            buildFromSorted(new IOIterator(in, size), size);
        }
        catch (IteratorIOException e) {
            throw e.getException();
        }
        catch (IteratorNoClassException e) {
            throw e.getException();
        }
    }

    private class SubMap extends AbstractMap implements Serializable, NavigableMap {

        private static final long serialVersionUID = -6520786458950516097L;

        final Object fromKey, toKey;

        SubMap() { fromKey = toKey = null; }

        private Object readResolve() {
            return new AscendingSubMap(fromKey == null, fromKey, true,
                                       toKey == null, toKey, false);
        }

        @Override
		public Map.Entry lowerEntry(Object key)   { throw new Error(); }
        @Override
		public Object lowerKey(Object key)        { throw new Error(); }
        @Override
		public Map.Entry floorEntry(Object key)   { throw new Error(); }
        @Override
		public Object floorKey(Object key)        { throw new Error(); }
        @Override
		public Map.Entry ceilingEntry(Object key) { throw new Error(); }
        @Override
		public Object ceilingKey(Object key)      { throw new Error(); }
        @Override
		public Map.Entry higherEntry(Object key)  { throw new Error(); }
        @Override
		public Object higherKey(Object key)       { throw new Error(); }
        @Override
		public Map.Entry firstEntry()             { throw new Error(); }
        @Override
		public Map.Entry lastEntry()              { throw new Error(); }
        @Override
		public Map.Entry pollFirstEntry()         { throw new Error(); }
        @Override
		public Map.Entry pollLastEntry()          { throw new Error(); }
        @Override
		public NavigableMap descendingMap()       { throw new Error(); }
        @Override
		public NavigableSet navigableKeySet()     { throw new Error(); }
        @Override
		public NavigableSet descendingKeySet()    { throw new Error(); }
        @Override
		public Set entrySet()                     { throw new Error(); }

        @Override
		public NavigableMap subMap(Object fromKey, boolean fromInclusive,
                                   Object toKey, boolean toInclusive) {
            throw new Error();
        }

        @Override
		public NavigableMap headMap(Object toKey, boolean inclusive) {
            throw new Error();
        }

        @Override
		public NavigableMap tailMap(Object fromKey, boolean inclusive) {
            throw new Error();
        }

        @Override
		public SortedMap subMap(Object fromKey, Object toKey) {
            throw new Error();
        }

        @Override
		public SortedMap headMap(Object toKey)     { throw new Error(); }
        @Override
		public SortedMap tailMap(Object fromKey)   { throw new Error(); }
        @Override
		public Comparator comparator()             { throw new Error(); }
        @Override
		public Object firstKey()                   { throw new Error(); }
        @Override
		public Object lastKey()                    { throw new Error(); }
    }
}
