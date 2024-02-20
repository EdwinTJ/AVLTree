public interface Queue<E> {
    E remove();
    void add(E data);
    boolean isEmpty();
}
