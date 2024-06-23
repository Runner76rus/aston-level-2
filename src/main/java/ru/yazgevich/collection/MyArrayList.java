package ru.yazgevich.collection;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Objects;

/**
 * * This class is my ArrayList version. It implements only CRUD operations,{@code subList},
 * * {@code hashCode},{@code toString} and {@code equals}.
 * * The remaining operations use standard implementation.
 *
 * @param <E> - the type of elements in this list
 */
public class MyArrayList<E> {

    private static final int DEFAULT_CAPACITY = 10;
    /**
     * The internal array of the list that stores all the elements.
     */
    private E[] elementData;
    private int size;
    /**
     * The number of times this list has been structurally modified.
     * Structural modifications are those that change the size of the list,
     * * or otherwise perturb it in such a fashion that iterations in progress may yield incorrect results.
     */
    private int modCount = 0;

    /**
     * constructs a list with a default capacity of ten.
     */
    public MyArrayList() {
        elementData = (E[]) new Object[DEFAULT_CAPACITY];
    }

    /**
     * constructs a list with specified capacity.
     *
     * @param capacity - an initial capacity of the list
     */
    public MyArrayList(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity cannot be < 0 : " + capacity);
        } else {
            elementData = (E[]) new Object[capacity];
        }
    }

    /**
     * Constructs a new list and appends all elements from the specified list.
     * If the specified list == {@code null}, then construct an empty list with an initial capacity of ten.
     *
     * @param list the list of elements which will be added to the new list
     */
    public MyArrayList(List<? extends E> list) {
        this();
        if (list != null && !list.isEmpty()) {
            elementData = (E[]) new Object[list.size()];
            addAll(list);
        }
    }

    /**
     * Adds all elements from the specified list to this list.
     *
     * @param list the list of elements which will be added to the new list
     * @return {@code true} if the list has been modified
     */
    public boolean addAll(List<? extends E> list) {
        boolean modified = false;
        for (E e : list) {
            add(e);
            modified = true;
        }
        return modified;
    }

    /**
     * Returns the element from the list at the specified index.
     *
     * @param index index of the element to return
     * @return element at the specified position
     */
    public E get(int index) {
        Objects.checkIndex(index, size());
        return elementData[index];
    }

    /**
     * Adds the specified element to the end of the list.
     *
     * @param e element whose presence in this collection is to be ensured
     * @return {@code true} if the specified element was added
     */
    public boolean add(E e) {
        if (elementData.length <= size) grow();
        elementData[size++] = e;
        modCount++;
        return true;
    }

    /**
     * Inserts the specified element to the list at the specified index.
     * Shifts the element at the specified position and all subsequent elements to the right.
     *
     * @param index   index at which the specified element is to be inserted
     * @param element element to be inserted
     */
    public void add(int index, E element) {
        Objects.checkIndex(index, size());
        if (elementData.length >= size) grow();
        System.arraycopy(elementData, index, elementData, index + 1, size - index);
        elementData[index] = element;
        modCount++;
        size++;
    }


    /**
     * Doubles the length of the internal array. If the length of the internal array is less than 10
     * then value for length is set to 10.
     */
    private void grow() {
        int newCapacity = elementData.length < DEFAULT_CAPACITY ? DEFAULT_CAPACITY : elementData.length * 2;
        E[] newElementData = (E[]) new Object[newCapacity];
        System.arraycopy(elementData, 0, newElementData, 0, elementData.length);
        elementData = newElementData;
    }

    /**
     * Replace the values in the list at the specified index with the specified value.
     *
     * @param index   index of the element to replace
     * @param element element to be stored at the specified position
     * @return the value replaced
     */
    public E set(int index, E element) {
        Objects.checkIndex(index, size());
        E old = elementData[index];
        elementData[index] = element;
        return old;
    }

    /**
     * Removes an element from the list at the specified index
     *
     * @param index the index of the element to be removed
     * @return the removed element
     */
    public E remove(int index) {
        Objects.checkIndex(index, size());
        E old = elementData[index];
        System.arraycopy(elementData, index + 1, elementData, index, size() - index - 1);
        elementData[--size] = null;
        modCount++;
        return old;
    }

    /**
     * Returns a sublist of this list between the specified fromIndex, inclusive, and toIndex, exclusive.
     * (If fromIndex and toIndex are equal, the returned list is empty.)
     *
     * @param fromIndex initial index (inclusive) of the subList
     * @param toIndex   endpoint (exclusive) of the subList
     * @return returns a sublist of this list
     */
    public MyArrayList<E> subList(int fromIndex, int toIndex) {
        checkRange(fromIndex, toIndex);
        E[] newData = (E[]) new Object[toIndex - fromIndex];
        System.arraycopy(elementData, fromIndex, newData, 0, newData.length);
        return new MyArrayList<>(Arrays.asList(newData));
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
     * Returns hash code for the list based on elements of this list.
     *
     * @return - hash code of this list
     */
    @Override
    public int hashCode() {
        int expectedModCount = modCount;
        int hash = hashCode(size);
        equalsModCount(expectedModCount);
        return hash;
    }

    private int hashCode(int to) {
        int hash = 1;
        for (int i = 0; i < to; i++) {
            E e = elementData[i];
            hash = 31 * hash + (e == null ? 0 : e.hashCode());
        }
        return hash;
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
        if (!(o instanceof MyArrayList<?> that)) return false;
        if (!super.equals(o)) return false;
        boolean result = size == that.size && Objects.deepEquals(elementData, that.elementData);
        equalsModCount(expectedModCount);
        return result;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (size == 0) return "[]";
        sb.append("[");
        for (E e : elementData) {
            sb.append(e);
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("]");
        return sb.toString();
    }

    /**
     * Returns amount of elements in the list.
     *
     * @return amount of elements in the list
     */
    public int size() {
        return size;
    }
}
