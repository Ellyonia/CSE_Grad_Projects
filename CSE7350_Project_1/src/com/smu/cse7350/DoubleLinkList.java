package com.smu.cse7350;

public class DoubleLinkList<T> {
    Node head;
    Node tail;
    int length;

    public DoubleLinkList()
    {
        length = 0;
    }

    protected class Node {
        T data;
        Node next;
        Node prev;

        public Node(T data, Node next, Node prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

        public void addFirst(T newData) {
            Node tmp = new Node(newData, head, null);
            if (head != null) {
                head.prev = tmp;
            }
            head = tmp;
            if (tail == null) {
                tail = tmp;
            }
            length++;
        }

        public void addLast(T newData) {

            Node tmp = new Node(newData, null, tail);
            if (tail != null) {
                tail.next = tmp;
            }
            tail = tmp;
            if (head == null) {
                head = tmp;
            }
            length++;
        }

//        public String toString()
//        {
//            Node tmp = head;
//            StringBuilder sb = new StringBuilder();
//            while (tmp.next != null)
//            {
//
//            }
//        }



}
