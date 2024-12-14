/* Design & Implement the following scenario: Draw a class diagram, and use case diagram for the library system. Discuss your 
design decisions, and any limitations of your model. For each book held by the library, the catalogue contains the title, author's name and ISBN of the book. There 
may be multiple copies of a each book and each copy with unique accession number. Registered readers belonging to the library, each of whom is issued with a 
number of tickets. The system records the name and address of each reader, and the number of tickets that they have been issued with. Readers can borrow one book for 
each ticket that they possess, and the system keeps a record of which books a reader has borrowed, along with the date that the book must be returned by. Readers can 
borrow, return and renew books from the library by forwarding a ticket to the library staff at the library desk. Readers have an option of purchasing additional 
tickets from the library. Any book that is not returned by the due date is subject to a fine of Rs.1 per day. Library staff is responsible for collecting fines, adding new 
books to the library. */

import java.util.*;


// Represents a Book Copy with unique accession number
class BookCopy {
    private String accessionNumber;
    private Book book;

    public BookCopy(Book book, String accessionNumber) {
        this.book = book;
        this.accessionNumber = accessionNumber;
    }

    public Book getBook() {
        return book;
    }

    public String getAccessionNumber() {
        return accessionNumber;
    }
}

// Represents Book object
class Book {
    private String title;
    private String author;
    private String isbn;
    private List<BookCopy> copies;

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.copies = new ArrayList<>();
    }

    public void addCopy(String accessionNumber) {
        copies.add(new BookCopy(this, accessionNumber));
    }

    public List<BookCopy> getCopies() {
        return copies;
    }

    public String getTitle() {
        return title ;
    }
}

// Represents LibraryTicket object
class LibraryTicket {
    private BookCopy bookCopy;
    private Date borrowDate;
    private Date dueDate;

    public LibraryTicket(BookCopy bookCopy, Date borrowDate, Date dueDate) {
        this.bookCopy = bookCopy;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }

    public BookCopy getBookCopy() {
        return bookCopy;
    }

    public Date getDueDate() {
        return dueDate;
    }
}

// Represents Reader object
class Reader {
    public String name;
    private String address;
    private List<LibraryTicket> tickets;
    private int availableTickets;

    public Reader(String name, String address, int initialTickets) {
        this.name = name;
        this.address = address;
        this.tickets = new ArrayList<>();
        this.availableTickets = initialTickets;
    }

    public void borrowTicket(LibraryTicket ticket) {
        if (availableTickets > 0) {
            tickets.add(ticket);
            availableTickets--;
        } else {
            System.out.println("No tickets available. Please purchase more tickets.");
        }
    }

    public void returnTicket(LibraryTicket ticket) {
        tickets.remove(ticket);
        availableTickets++;
    }

    public void purchaseAdditionalTickets(int numTickets) {
        availableTickets += numTickets;
    }

    public List<LibraryTicket> getTickets() {
        return tickets;
    }
}

// Represents Library Staff who can manage books and fines
class LibraryStaff {
    private Library library;

    public LibraryStaff(Library library) {
        this.library = library;
    }

    public void addBook(String title, String author, String isbn, int numCopies) {
        Book book = new Book(title, author, isbn);
        for (int i = 1; i <= numCopies; i++) {
            book.addCopy("ACC" + (library.getTotalCopies() + i));
        }
        library.addBook(book);
        System.out.println("Added " + numCopies + " copies of book: " + title);
    }

    public void collectFine(double amount) {
        System.out.println("Collected fine of Rs. " + amount);
    }
}

// Represents Library
class Library {
    private List<Book> books;
    private List<Reader> readers;

    public Library() {
        this.books = new ArrayList<>();
        this.readers = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void addReader(Reader reader) {
        readers.add(reader);
    }

    public void borrowBook(Reader reader, Book book) {
        BookCopy availableCopy = findAvailableCopy(book);
        if (availableCopy != null) {
            LibraryTicket ticket = new LibraryTicket(availableCopy, new Date(), calculateDueDate());
            reader.borrowTicket(ticket);
            System.out.println(reader.name + " borrowed " + availableCopy.getBook().getTitle());
        } else {
            System.out.println("No copies available for this book.");
        }
    }

    public void returnBook(Reader reader, Book book) {
        LibraryTicket ticket = null;
        for (LibraryTicket t : reader.getTickets()) {
            if (t.getBookCopy().getBook().equals(book)) {
                ticket = t;
                break;
            }
        }
        if (ticket != null) {
            // Calculate fine
            long daysLate = calculateDaysLate(ticket.getDueDate());
            double fine = calculateFine(daysLate);
            if (fine > 0) {
                System.out.println("Fine for late return: Rs. " + fine);
            }
            reader.returnTicket(ticket);
        }
    }

    private BookCopy findAvailableCopy(Book book) {
        for (BookCopy copy : book.getCopies()) {
            if (copy != null) { 
                return copy;
            }
        }
        return null;
    }

    private Date calculateDueDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 14);
        return cal.getTime();
    }

    private long calculateDaysLate(Date dueDate) {
        long diffInMillis = new Date().getTime() - dueDate.getTime();
        return diffInMillis / (1000 * 60 * 60 * 24);
    }

    private double calculateFine(long daysLate) {
        if (daysLate > 0) {
            return daysLate * 1;
        }
        return 0;
    }

    public int getTotalCopies() {
        return books.stream().mapToInt(book -> book.getCopies().size()).sum();
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<Reader> getReaders() {
        return readers;
    }
}

public class LMS {
    public static void main(String[] args) {
        Library library = new Library();
        LibraryStaff staff = new LibraryStaff(library);

        // Add books to the library
        staff.addBook("Java Programming", "Author A", "12345", 3);
        staff.addBook("Data Structures", "Author B", "67890", 2);

        // Add a reader
        Reader reader = new Reader("Vineesha", "123 Street, City", 1);
        library.addReader(reader);

        // Borrow a book

        library.borrowBook(reader, library.getBooks().get(0));

        // Simulate the reader purchasing additional tickets and borrowing more books
        reader.purchaseAdditionalTickets(2);
        library.borrowBook(reader, library.getBooks().get(1));

        // Return a book 
        library.returnBook(reader, library.getBooks().get(0));
    }
}
