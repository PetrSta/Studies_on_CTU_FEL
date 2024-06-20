package cz.cvut.fel.pjv;

/**
 * Implementation of the {@link Queue} backed by fixed size array.
 */
public class CircularArrayQueue implements Queue {

    int head;
    int tail;
    int capacity;
    int stored_count;
    String[] element_storage;

    /**
     * Creates the queue with capacity set to the value of 5.
     */
    public CircularArrayQueue() {
        this.capacity = 5;
        this.head = 0;
        this.tail = 0;
        this.stored_count = 0;
        this.element_storage = new String[this.capacity];
        for (int i = 0; i < capacity; i++) {
            this.element_storage[i] = null;
        }
    }

    /**
     * Creates the queue with given {@code capacity}. The capacity represents maximal number of elements that the
     * queue is able to store.
     * @param capacity of the queue
     */
    public CircularArrayQueue(int capacity) {
        this.capacity = capacity;
        this.head = 0;
        this.tail = 0;
        this.stored_count = 0;
        this.element_storage = new String[this.capacity];
    }

    @Override
    public int size() {
        return this.stored_count;
    }

    @Override
    public boolean isEmpty() {
        return this.stored_count <= 0;
    }

    @Override
    public boolean isFull() {
        return this.stored_count == this.capacity;
    }

    //not completed
    @Override
    public boolean enqueue(String obj) {
        if(obj == null || isFull()) {
            return false;
        } else if(!isFull() && this.head == this.capacity - 1) {
            this.element_storage[this.head] = obj;
            this.head = 0;
        } else {
            this.element_storage[this.head] = obj;
            this.head++;
        }
        this.stored_count++;
        return true;
    }

    //not completed
    @Override
    public String dequeue() {
        if(isEmpty()) {
            return null;
        }
        String pop_string = this.element_storage[this.tail];
        this.element_storage[this.tail] = null;
        if(this.tail == this.capacity - 1) {
            this.tail = 0;
        } else {
            this.tail++;
        }
        if(stored_count > 0) {
            this.stored_count--;
        }
        return pop_string;
    }

    @Override
    public void printAllElements() {
        String tmp_check;
        for (int i = 0; i <= this.stored_count; i++) {
            tmp_check = this.element_storage[this.tail + i];
            if(tmp_check != null) {
                System.out.println(element_storage[this.tail + i]);
            }
        }
    }
}
