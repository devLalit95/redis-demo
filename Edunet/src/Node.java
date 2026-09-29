import java.util.List;

public class Node {
    int data;
    Node next;

    Node(int data) {
        this.data = data;
        this.next = null;
    }

    Node(){}
}

class Linked {
    Node head;

    public void insert(int data) {
        Node node = new Node(data);
        if (head == null) {
            head = node;
        } else {

            Node temp = head;

            while (temp.next!= null) {
                temp = temp.next;

            }
            temp.next = node;
        }


    }

    void show(){
        Node temp = head;
        if (temp == null) {
            System.out.print("Empty List");
        }
        while (temp!= null) {
            System.out.print( temp.data+" ->");
            temp = temp.next;
        }
        System.out.println(" null");
    }

    void  updateLast(int value){
        Node temp = head;
        if (head==null){
            System.out.print("Empty List");
        }
        else {
            while (temp.next!=null){
                temp = temp.next;
            }
            temp.data = value;
        }
    }

    void  updateAtPosition(int value, int pos){
        Node temp = head;
        while (temp.next!=null && pos > 1){
            temp = temp.next;
            pos--;
        }

        temp.data = value;
    }

    void deleteFirst(){
        if (head==null){
            System.out.println("No elements to remove");
        }else {
            head = head.next;
        }
    }

    void deleteLast(){
        if (head==null){
            System.out.println("Empty list");
        }
        else {
            Node temp = head;
            while (temp.next.next!=null){
                temp = temp.next;
            }
            temp.next = null;
        }
    }

    void  deleteAtPosition(int pos){
        if (head==null){
            System.out.println("empty list");
        }else {
            Node temp = head;
            while (temp.next.next!=null && pos > 1){
                temp = temp.next;
                pos--;
            }
            temp.next = temp.next.next;
        }
    }
    public static void main(String[] args) {
        Linked node =  new Linked();
        node.insert(3);
        node.insert(10);
        node.insert(5);
        node.insert(30);
        node.insert(40);
        node.insert(50);

        node.show();
        node.updateLast(60);
        System.out.println();
        node.show();
        node.updateAtPosition(10,1);
        System.out.println();

        node.show();
node.deleteAtPosition(6);
        node.show();

    }

}
