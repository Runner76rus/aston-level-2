package ru.yazgevich.collection;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Objects;

/**
 * This class is my LinkedList version. It implements only CRUD operations,{@code subList},
 * {@code hashCode},{@code toString} and {@code equals}.
 * The remaining operations use standard implementation.
 *
 * @param <E> - the type of elements in this list
 */
public class MyLinkedList<E> {

    /**
     * The first element of the list
     */
    private Node<E> head;
    /**
     * The last element of the list
     */
    private Node<E> tail;

    private int size = 0;
    /**
     * The number of times this list has been structurally modified.
     * Structural modifications are those that change the size of the list,
     * * or otherwise perturb it in such a fashion that iterations in progress may yield incorrect results.
     */
    private int modCount = 0;

    /**
     * Constructs an empty list
     */
    public MyLinkedList() {
        head = null;
        tail = null;
    }

    /**
     * Constructs a new list and appends all elements from the specified collection
     *
     * @param c the collection of elements which will be added to the new list
     */
    public MyLinkedList(Collection<? extends E> c) {
        this();
        if (c != null && !c.isEmpty()) {
            addAll(c);
            size = c.size();
        }
    }

    /**
     * Adds all elements from the specified list to this list.
     *
     * @param c the collection of elements which will be added to the new list
     * @return {@code true} if the list has been modified
     */
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            add(e);
            modified = true;
        }
        return modified;
    }

    /**
     * Adds the specified element to the start of the list
     *
     * @param e element whose presence in this collection is to be ensured
     * @see #addLast
     */
    public void addFirst(E e) {
        Node<E> oldHead = head;
        Node<E> newNode = new Node<>(null, e, oldHead);
        head = newNode;
        if (oldHead == null) {
            tail = newNode;
        } else {
            oldHead.prev = newNode;
        }
        size++;
        modCount++;
    }

    /**
     * Adds the specified element to the end of the list
     *
     * @param e element whose presence in this collection is to be ensured
     * @see #addFirst
     */
    public void addLast(E e) {
        Node<E> oldTail = tail;
        Node<E> newNode = new Node<>(oldTail, e, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = tail;
        }
        size++;
        modCount++;
    }

    /**
     * Adds the specified element to the end of the list.
     *
     * @param e element whose presence in this collection is to be ensured
     * @return {@code true} if the specified element was added
     * @see #addLast
     * @see #addFirst
     */
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    /**
     * Inserts the specified element to the list at the specified index.
     *
     * @param index index at which the specified element is to be inserted
     * @param e     element to be inserted
     */
    public void add(int index, E e) {
        Objects.checkIndex(index, size);
        Node<E> nextNode = head;
        for (int i = 0; i < index; i++) {
            nextNode = nextNode.next;
        }
        Node<E> prevNode = nextNode.prev;
        Node<E> curNode = new Node<>(prevNode, e, nextNode);
        prevNode.next = curNode;
        nextNode.prev = curNode;
        size++;
        modCount++;
    }

    /**
     * Returns the element from the list at the specified position.
     *
     * @param index position of the element to return
     * @return element at the specified position
     */
    public E get(int index) {
        Objects.checkIndex(index, size);
        Node<E> node = head;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        return node.value;
    }

    /**
     * Replace the values in the list at the specified position with the specified value.
     *
     * @param index   position of the element to replace
     * @param element element to be stored at the specified position
     * @return the value replaced
     */
    public E set(int index, E element) {
        Objects.checkIndex(index, size);
        Node<E> node = head;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        E oldValue = node.value;
        node.value = element;
        return oldValue;
    }

    /**
     * Removes an element from the list at the specified position
     *
     * @param index the position of the element to be removed
     * @return the removed element
     */
    public E remove(int index) {
        Objects.checkIndex(index, size);
        Node<E> node = head;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        E oldValue = node.value;
        node.prev.next = node.next;
        node.next.prev = node.prev;
        size--;
        modCount++;
        return oldValue;
    }

    /**
     * Returns a sublist of this list between the specified fromIndex ( inclusive ) and toIndex ( exclusive ).
     * If fromIndex and toIndex are equal, the returned list is empty.
     *
     * @param fromIndex initial position (inclusive) of the subList
     * @param toIndex   endpoint (exclusive) of the subList
     * @return returns a sublist of this list
     */
    public MyLinkedList<E> subList(int fromIndex, int toIndex) {
        checkRange(fromIndex, toIndex);
        Node<E> firstNode = head;
        for (int i = 0; i < fromIndex; i++) {
            firstNode = firstNode.next;
        }
        Node<E> lastNode = firstNode;
        for (int i = 0; i < toIndex - fromIndex - 1; i++) {
            lastNode = lastNode.prev;
        }

        MyLinkedList<E> list = new MyLinkedList<>();
        list.head = firstNode;
        list.tail = lastNode;
        list.size = toIndex - fromIndex;
        return list;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Node<E> node = head;
        while (node != null) {
            E e = node.value;
            sb.append(e);
            node = node.next;
            sb.append(',').append(' ');
        }
        sb.delete(sb.length() - 2, sb.length());
        return sb.append(']').toString();
    }

    /**
     * Checks if the internal array indexes are valid
     *
     * @throws IndexOutOfBoundsException if the indexes are outside the bounds of the internal array
     *                                   or {@code from} < {@code to}
     */
    private void checkRange(int from, int to) {
        if (from > to) {
            throw new IndexOutOfBoundsException("from=" + from + ", to=" + to);
        } else if (from < 0) {
            throw new IndexOutOfBoundsException("from=" + from);
        } else if (to > size) {
            throw new IndexOutOfBoundsException("to=" + to);
        }
    }

    /**
     * Returns amount of elements in the list.
     *
     * @return amount of elements in the list
     */
    public int size() {
        return size;
    }


    private static class Node<E> {

        private E value;
        private Node<E> next;
        private Node<E> prev;

        public Node(Node<E> prev, E value, Node<E> next) {
            this.prev = prev;
            this.value = value;
            this.next = next;
        }


    }

    /**
     * Checks if structural changes modified have been made.
     * Structural modifications are those that change the size of the list,
     * or otherwise perturb it in such a fashion that iterations in progress may yield incorrect results.
     *
     * @param modCount The number of times this list has been structurally modified.
     * @throws ConcurrentModificationException - if the list was modified during the execution of the method
     */
    private void equalsModCount(int modCount) {
        if (this.modCount != modCount) throw new ConcurrentModificationException();
    }

    /**
     * Compares the specified object with this list for equality.
     * Returns {@code true} if and only if the specified object is also a list, both lists have the same size,
     * and all corresponding pairs of elements in the two lists are equal.
     *
     * @param o the object to be compared for equality with this list
     * @return {@code true} if two lists contain the same elements in the same order
     */
    @Override
    public boolean equals(Object o) {
        int expectedModCount = modCount;
        if (this == o) return true;
        if (!(o instanceof MyLinkedList<?> that)) return false;
        boolean result = size == that.size;
        Node<E> thisNode = head;
        Node<?> thatNode = that.head;
        while (thisNode != null) {
            if (thatNode != null) {
                result &= Objects.equals(thisNode.value, thatNode.value);
            } else return false;
            thisNode = thisNode.next;
            thatNode = thatNode.next;
        }
        equalsModCount(expectedModCount);
        return result;
    }

    /**
     * Returns hash code for the list based on elements of this list.
     *
     * @return - hash code of this list
     */
    @Override
    public int hashCode() {
        int expectedModCount = modCount;
        int hash = Objects.hash(size);
        Node<E> node = head;
        while (node != null) {
            hash += Objects.hash(node.value);
            node = node.next;
        }
        equalsModCount(expectedModCount);
        return hash;
    }
}
