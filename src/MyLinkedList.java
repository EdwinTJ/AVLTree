public class MyLinkedList<E> {
    private Node<E> front;
    private Node<E> rear;

    public void addAtEnd(E data) {
        Node<E> newNode = new Node<>(data);
        if (front == null) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = rear.next;
        }
    }

    public E removeFromFront() {
        if (front == null) {
            return null; // Queue is empty
        }

        E data = front.data;
        front = front.next;

        if (front == null) {
            rear = null; // Queue is now empty
        }

        return data;
    }

    public void printContents() {
        Node<E> current = front;
        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next;
        }
        System.out.println();
    }

    public boolean isEmpty() {
        return front == null;
    }

    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
            this.next = null;
        }
    }
}

