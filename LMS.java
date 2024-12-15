import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Calendar;

// library user class
abstract class LibraryUser {
    private String name;
    private String address;

    public LibraryUser(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public abstract void performAction();
}

// reader class
class Reader extends LibraryUser {
    private List<LibraryTicket> tickets;
    private int availableTickets;

    public Reader(String name, String address, int availableTickets) {
        super(name, address);
        this.tickets = new ArrayList<>();
        this.availableTickets = availableTickets;
    }

    public void borrowBook(LibraryTicket ticket) {
        if (availableTickets > 0) {
            tickets.add(ticket);
            availableTickets--;
            System.out.println(getName() + " borrowed the book: " + ticket.getBookCopy().getBook().getTitle());
        } else {
            System.out.println("No tickets available to borrow books.");
        }
    }

    public void returnBook(LibraryTicket ticket) {
        tickets.remove(ticket);
        availableTickets++;
        System.out.println(getName() + " returned the book: " + ticket.getBookCopy().getBook().getTitle());
    }

    @Override
    public void performAction() {
        System.out.println(getName() + " is a Reader who borrows and returns books.");
    }
}

// Library staff
class LibraryStaff extends LibraryUser {
    public LibraryStaff(String name, String address) {
        super(name, address);
    }

    public void addBook(Library library, Book book) {
        library.addBook(book);
        System.out.println("Book titled '" + book.getTitle() + "' added by " + getName());
    }

    public void collectFine(Reader reader, double fine) {
        System.out.println("Collected fine of Rs. " + fine + " from " + reader.getName());
    }

    @Override
    public void performAction() {
        System.out.println(getName() + " is a Library Staff who manages the library system.");
    }
}

// Book
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

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void addCopy(String accessionNumber) {
        copies.add(new BookCopy(this, accessionNumber));
    }

    public List<BookCopy> getCopies() {
        return copies;
    }
}

// Book Copy
class BookCopy {
    private Book book;
    private String accessionNumber;

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

// Ticket
class LibraryTicket {
    private BookCopy bookCopy;
    private Date borrowDate;
    private Date dueDate;
    private double finePerDay = 1.0;

    public LibraryTicket(BookCopy bookCopy, Date borrowDate, Date dueDate) {
        this.bookCopy = bookCopy;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }

    public BookCopy getBookCopy() {
        return bookCopy;
    }

    public double calculateFine() {
        long overdueDays = (new Date().getTime() - dueDate.getTime()) / (1000 * 60 * 60 * 24);
        return overdueDays > 0 ? overdueDays * finePerDay : 0;
    }
}

// Library
class Library {
    private List<Book> books;

    public Library() {
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public List<Book> getBooks() {
        return books;
    }

    public void displayBooks() {
        for (Book book : books) {
            System.out.println("Title: " + book.getTitle() + ", Author: " + book.getAuthor());
        }
    }
}

// main class
public class LibraryManagementSystem {
    public static void main(String[] args) {
        // Creating Library
        Library library = new Library();

        // Creating Books and BookCopies
        Book book1 = new Book("Java Programming", "John Doe", "12345");
        book1.addCopy("A001");
        book1.addCopy("A002");

        Book book2 = new Book("Python Basics", "Jane Doe", "67890");
        book2.addCopy("B001");

        // Adding books to the Library
        LibraryStaff staff = new LibraryStaff("Alice", "Library Street");
        staff.addBook(library, book1);
        staff.addBook(library, book2);

        // Display Library Books
        library.displayBooks();

        // Create a Reader
        Reader reader = new Reader("Bob", "Reader's Lane", 2);

        // Borrow Book
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 10); // Due in 10 days
        Date dueDate = calendar.getTime(); // Due date 10 days from today
        Date borrowDate = new Date();

        LibraryTicket ticket = new LibraryTicket(book1.getCopies().get(0), borrowDate, dueDate);
        reader.borrowBook(ticket);

        // Return Book
        reader.returnBook(ticket);

        // Calculate Fine
        double fine = ticket.calculateFine();
        if (fine > 0) {
            staff.collectFine(reader, fine);
        } else {
            System.out.println("No fine for " + reader.getName());
        }

        // Perform Actions
        staff.performAction();
        reader.performAction();
    }
}
