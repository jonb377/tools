package com.bolin.set;

import java.util.*;

/**
 * Created by jonb3_000 on 7/27/2016.
 */
public class ListSet<E> implements java.util.Set<E>, java.util.List<E> {

    private ArrayList<E> data;

    public ListSet() {
        data = new ArrayList<E>();
    }


    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return data.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return data.iterator();
    }

    @Override
    public Object[] toArray() {
        return data.toArray();
    }

    @Override
    public boolean add(E o) {
        if (!data.contains(o)) {
            return data.add(o);
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return data.remove(o);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return data.addAll(index, c);
    }

    @Override
    public boolean addAll(Collection c) {
        for (Object o : c) {
            if (!data.contains(o)) {
                data.add((E) o);
            }
        }
        return true;
    }

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public Spliterator<E> spliterator() {
        return data.spliterator();
    }

    @Override
    public boolean removeAll(Collection c) {
        return data.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection c) {
        return false;
    }

    @Override
    public boolean containsAll(Collection c) {
        return data.containsAll(c);
    }

    @Override
    public Object[] toArray(Object[] a) {
        return data.toArray(a);
    }

    public E get(int index) {
        return data.get(index);
    }

    @Override
    public E set(int index, E element) {
        return data.set(index, element);
    }

    @Override
    public void add(int index, E element) {
        data.add(index, element);
    }

    @Override
    public E remove(int index) {
        return data.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return data.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return data.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return data.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return data.listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return data.subList(fromIndex, toIndex);
    }

}
