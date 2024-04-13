package org.arctic.wolf;

import java.awt.desktop.AppReopenedEvent;

public class Node<T,U> {

    Node<T,U> prev;
    Node<T,U> next;
    T key;
    U value;


    public Node(Node<T, U> prev, Node<T, U> next, T key, U value) {
        this.prev = prev;
        this.next = next;
        this.key = key;
        this.value = value;
    }

    public T getKey() {
        return key;
    }

    public void setKey(T key) {
        this.key = key;
    }

    public U getValue() {
        return value;
    }

    public void setValue(U value) {
        this.value = value;
    }

}
