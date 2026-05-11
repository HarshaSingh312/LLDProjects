package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        LibraryManagementSystem system = new LibraryManagementSystem();
//        System.out.println(system.addBook("Journey of life", "Harsha Singh", 5));
//        System.out.println(system.addBook("Journey of life", "Harsha Singh", 5));
//        System.out.println(system.addBook("Journey of life", "Harsha Singhafdf", 5));
//        System.out.println(system.addBook("Journey of life", "Harsha awsf", 5));

//        System.out.println(system.addBook("Harry Potter and the Sorcerer's Stone", "J K Rowling", 2));
//        System.out.println(system.addBook("Harry Potter and the Sorcerer's Stone", "J K Rowling", 1));
//        System.out.println(system.registerUser("U1", "Alice"));
//        System.out.println(system.registerUser("U2", "Bob"));
//        System.out.println(system.requestBorrow("U1", "ROW1000", 1));
//        System.out.println(system.requestBorrow("U2", "ROW1000", 1));
//        System.out.println(system.usersHavingBook("ROW1000"));
//        System.out.println(system.booksIssuedToUser("U1"));

        // Waitlist / return / fine example
//        LibraryManagementSystem s2 = new LibraryManagementSystem();
//        System.out.println(s2.registerUser("U1", "Alice"));
//        System.out.println(s2.registerUser("U2", "Bob"));
//        System.out.println(s2.registerUser("U3", "Charlie"));
//        System.out.println(s2.addBook("Clean Code", "Robert C Martin", 1));
//        System.out.println(s2.requestBorrow("U1", "MAR1000", 5));
//        System.out.println(s2.requestBorrow("U2", "MAR1000", 6));
//        System.out.println(s2.requestBorrow("U3", "MAR1000", 6));
//        System.out.println(s2.returnBook("U1", "MAR1000", 10));
//        System.out.println(s2.requestBorrow("U3", "MAR1000", 10));
//        System.out.println(s2.requestBorrow("U2", "MAR1000", 10));
//        System.out.println(s2.returnBook("U2", "MAR1000", 30));
//        System.out.println(s2.requestBorrow("U3", "MAR1000", 30));
//        System.out.println(s2.usersHavingBook("MAR1000"));
//        System.out.println(s2.booksIssuedToUser("U2"));

        // Late return fine example
        LibraryManagementSystem s3 = new LibraryManagementSystem();
        System.out.println(s3.registerUser("U1", "Alice"));
        System.out.println(s3.addBook("The Hobbit", "J R R Tolkien", 1));
        System.out.println(s3.requestBorrow("U1", "TOL1000", 1));
        System.out.println(s3.returnBook("U1", "TOL1000", 20));
        System.out.println(s3.usersHavingBook("TOL1000"));
        System.out.println(s3.booksIssuedToUser("U1"));
    }
}
