Library Management System (OOP Project)
Project Overview:
Developed a Library Management System using Java to automate the process of borrowing, returning, and managing books in a library. The system allows readers to borrow books using tickets, 
return books with fine calculations for late returns, and manage book details. Library staff can add new books and manage fines. The project uses Object-Oriented Programming (OOP) principles 
to ensure scalability, maintainability, and clear code structure.

OOP Concepts Used:
Encapsulation:
Classes like Book, BookCopy, LibraryTicket, and Reader encapsulate relevant attributes and behaviors. Private fields ensure that data is protected, and public methods control access, ensuring data integrity.

Abstraction:
The system abstracts the complexity of borrowing and returning books, calculating fines, and managing tickets, presenting users with simple, high-level methods to interact with the system.

Modularity:
The system is divided into modular classes representing distinct entities (Book, Reader, LibraryStaff, etc.), making the code easy to maintain and extend.

Inheritance:
Inheritance is used to create a hierarchy where a general LibraryItem class (if introduced) could be extended by specific items such as Book. This allows for code reusability and a more structured design. 
For example, future library items like Magazines could inherit from LibraryItem, making it easier to extend the system.

Real-World Problem Solving:
The system simulates real-life library operations, such as borrowing and returning books, tracking fines, and inventory management, providing a practical application of OOP concepts.
