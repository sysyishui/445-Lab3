import java.io.*;
import java.util.Random;

/** A linked implementation of the ADT List.
 *
 * This code is from Chapter 14 of
 * Data Structures and Abstractions with Java 4/e
 *      @author Frank M. Carrano
 *
 * Modifications were made by Charles Hoot:
 * The toString method is overwritten to give a nice display of the items in
 * the list in this format { <1> <2> <3> <4> }
 *
 * An alternate display method has been created to print the list one item
 * to a line along with the index
 *
 *
  * @version 4.0
 */
class LList<T> implements ListInterface<T> {

    private Node firstNode;  // Reference to first node of chain
    private int numberOfEntries;

    public LList() {
        initializeDataFields();
    } // end default constructor

    public void clear() {
        initializeDataFields();
    } // end clear

    // Initialize the class's data fields to indicate an empty list.
    private void initializeDataFields() {
        firstNode = null;
        numberOfEntries = 0;
    }

    public void add(T newEntry) {
        Node newNode = new Node(newEntry);
        if (isEmpty()) {
            firstNode = newNode;
        } else {
            Node lastNode = getNodeAt(numberOfEntries);
            lastNode.setNextNode(newNode); // Make last node reference new node
        } // end if

        numberOfEntries++;
    } // end add

    public void add(int newPosition, T newEntry) {
        if ((newPosition >= 1) && (newPosition <= numberOfEntries + 1)) {
            Node newNode = new Node(newEntry);
            if (newPosition == 1) {
                newNode.setNextNode(firstNode);
                firstNode = newNode;
            } else {
                Node nodeBefore = getNodeAt(newPosition - 1);
                Node nodeAfter = nodeBefore.getNextNode();
                newNode.setNextNode(nodeAfter);
                nodeBefore.setNextNode(newNode);
            }
            numberOfEntries++;
        } else {
            throw new IndexOutOfBoundsException("Illegal position given to add operation.");
        }
    } // end add

    public T remove(int givenPosition) {
        T result = null;
        if ((givenPosition >= 1) && (givenPosition <= numberOfEntries)) {
            assert !isEmpty();
            if (givenPosition == 1) {
                result = firstNode.getData();
                firstNode = firstNode.getNextNode();
            } else {
                Node nodeBefore = getNodeAt(givenPosition - 1);
                Node nodeToRemove = nodeBefore.getNextNode();
                result = nodeToRemove.getData();
                Node nodeAfter = nodeToRemove.getNextNode();
                nodeBefore.setNextNode(nodeAfter);
            }
            numberOfEntries--;
            return result;
        } else {
            throw new IndexOutOfBoundsException("Illegal position given to remove operation.");
        }
    } // end remove

    public boolean contains(T anEntry) {
        boolean found = false;
        Node currentNode = firstNode;
        while (!found && (currentNode != null)) {
            if (anEntry.equals(currentNode.getData())) {
                found = true;
            } else {
                currentNode = currentNode.getNextNode();
            }
        }
        return found;
    } // end contains

    public T getEntry(int givenPosition) {
        if ((givenPosition >= 1) && (givenPosition <= numberOfEntries)) {
            assert !isEmpty();
            return getNodeAt(givenPosition).getData();
        } else {
            throw new IndexOutOfBoundsException("Illegal position given to getEntry operation.");
        }
    } // end getEntry

    /** Replaces the entry at a given position in the list.
     *  @param givenPosition The position of the entry to be replaced.
     *  @param newEntry The new entry that will replace the old one.
     *  @return The original entry that was replaced.
     */
    public T replace(int givenPosition, T newEntry) {
        if ((givenPosition >= 1) && (givenPosition <= numberOfEntries)) {
            assert !isEmpty();
            Node desiredNode = getNodeAt(givenPosition);
            T originalEntry = desiredNode.getData();
            desiredNode.setData(newEntry);
            return originalEntry;
        } else {
            throw new IndexOutOfBoundsException("Illegal position given to replace operation.");
        }
    } // end replace

    public int getLength() {
        return numberOfEntries;
    }

    public boolean isEmpty() {
        return numberOfEntries == 0;
    } // end isEmpty

    public T[] toArray() {
        @SuppressWarnings("unchecked")
        T[] result = (T[]) new Object[numberOfEntries];
        int index = 0;
        Node currentNode = firstNode;
        while ((index < numberOfEntries) && (currentNode != null)) {
            result[index] = currentNode.getData();
            currentNode = currentNode.getNextNode();
            index++;
        }
        return result;
    } // end toArray

    private Node getNodeAt(int givenPosition) {
        assert (firstNode != null)
                && (1 <= givenPosition) && (givenPosition <= numberOfEntries);
        Node currentNode = firstNode;
        for (int counter = 1; counter < givenPosition; counter++) {
            currentNode = currentNode.getNextNode();
        }
        return currentNode;
    } // end getNodeAt

    // Display the contents of the list
    public void display() {
        Node currentNode = firstNode;
        System.out.print("{ ");
        while (currentNode != null) {
            System.out.print("<" + currentNode.getData() + "> ");
            currentNode = currentNode.getNextNode();
        }
        System.out.println("}");
    } // end display

    // Reverse the order of items in a list.
  // Reverse the order of items in a list.
public void reverse() {
    if (isEmpty() || numberOfEntries == 1) {
        return; // No need to reverse if the list is empty or has only one element
    }

    Node previous = null;
    Node current = firstNode;
    Node next = null;

    while (current != null) {
        next = current.getNextNode(); // Store next node
        current.setNextNode(previous); // Reverse the current node's pointer
        previous = current; // Move pointers forward
        current = next;
    }

    firstNode = previous; // Set the last node as the new head
}// end reverse

    // Randomly permute the list.
    public void randomPermutation() {
        if (isEmpty() || numberOfEntries == 1) {
            return;
        }

        Random rand = new Random();
        for (int i = 1; i <= numberOfEntries; i++) {
            int randomIndex = rand.nextInt(numberOfEntries) + 1;
            T temp = getEntry(i);
            replace(i, getEntry(randomIndex));
            replace(randomIndex, temp);
        }
    } // end randomPermutation

    // Move the item at the given position to the back.
    public void moveToBack(int from) {
        if (from < 1 || from > numberOfEntries) {
            throw new IndexOutOfBoundsException("Invalid index.");
        }

        T item = remove(from);
        add(item);
    } // end moveToBack

    // Interleave the list by splitting into two halves and alternating the elements.
    public void interleave() {
        if (numberOfEntries <= 1) {
            return;
        }

        int mid = (numberOfEntries + 1) / 2;
        LList<T> firstHalf = new LList<>();
        LList<T> secondHalf = new LList<>();

        for (int i = 1; i <= mid; i++) {
            firstHalf.add(getEntry(i));
        }

        for (int i = mid + 1; i <= numberOfEntries; i++) {
            secondHalf.add(getEntry(i));
        }

        clear();

        int i = 1, j = 1;
        while (i <= firstHalf.getLength() || j <= secondHalf.getLength()) {
            if (i <= firstHalf.getLength()) {
                add(firstHalf.getEntry(i));
                i++;
            }
            if (j <= secondHalf.getLength()) {
                add(secondHalf.getEntry(j));
                j++;
            }
        }
    } // end interleave

    private class Node {

        private T data;
        private Node next;

        private Node(T dataPortion) {
            this(dataPortion, null);
        }

        private Node(T dataPortion, Node nextNode) {
            data = dataPortion;
            next = nextNode;
        }

        private T getData() {
            return data;
        }

        private void setData(T newData) {
            data = newData;
        }

        private Node getNextNode() {
            return next;
        }

        private void setNextNode(Node nextNode) {
            next = nextNode;
        }
    } // end Node
}
