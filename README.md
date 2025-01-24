# MULTITHREADED-CHAT-APPLICATION-3

**COMPANY**: CODETECH IT SOLUTIONS

**NAME**: Devashya Tulsidas Patil

**INTERN ID**: CT08JFE

**DOMAIN**: JAVA PROGRAMMING

**BATCH DURATIONN**: January 5th, 2025 to February 5th, 2025

**MENTOR NAME**: Neela Santhosh Kumar

# DESCRIPTION: 

    Objective:
        The primary objective of this task was to design and implement a robust, multithreaded chat 
        application that supports real-time communication between multiple clients while incorporating a 
        secure and user-friendly authentication mechanism. The task emphasized using Java's multithreading 
        and networking capabilities to ensure scalability and efficiency.
    
    Technologies and Tools Used:
        -> Programming Language: Java
        -> Networking: Sockets (TCP/IP protocol)
        -> Concurrency: Java Threads
        -> GUI Development: Swing (for creating the client-side graphical interface)
        -> Data Structures: Maps and Lists (to manage users and connections)
    
    Key Features Implemented:
        1. User Authentication:
          -> The application supports user sign-up, requiring a unique username, password, and a valid
             10-digit mobile number.
          -> Users can sign in using their credentials, which are validated against the server’s database.
          -> Errors such as duplicate usernames, already registered mobile numbers, and incorrect login
             credentials are handled effectively, ensuring data integrity.
      
        2. Real-Time Chat Functionality:
          -> The core feature of the application allows users to exchange messages in real time.
          -> Messages sent by a user are broadcasted to all connected clients, ensuring everyone
             in the chat receives updates simultaneously.
          -> Users can also see their own messages displayed in their chat area, enhancing the
             user experience.
           
        3. Typing Indicator:
          -> Implemented a typing indicator that notifies other users when someone is typing a message.
          -> This feature is achieved by sending specific signals from the client to the server,
             which then notifies other clients.
        
        4. Server-Side Logging:
          -> The server logs all major activities, including:
            - Successful or failed sign-up and sign-in attempts.
            - Clients joining or leaving the chat.
            - Messages exchanged between clients.
          -> These logs are displayed on the server console, providing administrators with real-time
             insights into the application’s usage.
            
        5. Multithreaded Architecture:
          -> The server uses multithreading to handle multiple clients concurrently.
          -> Each client connection is managed by a dedicated thread, ensuring efficient
             communication without bottlenecks.
          -> Shared resources like the user database and client connection list are synchronized to
             prevent conflicts in a multithreaded environment.
        
        6. Graphical User Interface (GUI):
          -> The client-side application features an intuitive GUI built using Java Swing.
          -> The interface includes a chat display area, a message input field, a send button,
             and a typing indicator label.
          -> Dialog boxes notify users about the success or failure of actions like signing
             up or signing in.
           
        7. Error Handling and Notifications:
          -> The application handles errors gracefully, providing meaningful feedback to users.
          -> Notifications such as “Signup successful” or “Invalid username or password” are
             displayed through dialog boxes, improving user interaction.
    
    How the Application Works:
        1. Server-Side Operations:
          -> The server listens for incoming client connections using a ServerSocket.
          -> For each connected client, a new thread (ClientHandler) is spawned to
             manage communication independently.
          -> The server maintains a synchronized user database for authentication and
             a list of connected clients for broadcasting messages.
      
        2. Client-Side Operations:
          -> Clients connect to the server using a Socket.
          -> Users can sign up, sign in, and exchange messages through a user-friendly GUI.
          -> Messages typed by the user are sent to the server, which then relays them to other clients.
        
        3. Communication:
          -> The server and clients communicate using text-based protocols, with specific messages for
             actions like broadcasting, typing indicators, and authentication results.
      
    Challenges Faced and Solutions:
        1. Thread Synchronization:
          -> Managing shared resources like the user database and client connection list required
             synchronization to avoid race conditions. Java’s synchronized keyword was used effectively
             to address this issue.
        
        2. Real-Time Performance:
          -> Ensuring smooth and lag-free communication in a multithreaded environment was a
             priority. Efficient use of sockets and non-blocking I/O streams helped achieve this.
        
        3. Error Feedback:
          -> Handling scenarios like duplicate usernames, incorrect credentials, and invalid mobile
             numbers required robust error-handling mechanisms to guide users appropriately.
        
        4. User Interface Design:
          -> Creating a responsive and intuitive GUI for the client was a challenge that was
             addressed using Java Swing components like JTextArea, JTextField, and JOptionPane.
    
    Learning Outcomes:
        1. Advanced Java Skills:
          -> Deepened understanding of Java’s multithreading and socket programming.
          -> Gained experience in managing concurrent connections and shared resources.
        
        2. GUI Development:
          -> Learned to design user-friendly interfaces using Swing, ensuring a smooth user experience.
        
        3. Problem Solving:
          -> Developed debugging skills to identify and resolve issues related to networking and concurrency.
        
        4.Real-Time Systems:
          -> Understood the complexities of building real-time systems that handle multiple users simultaneously.
          
    Conclusion:
        This task successfully demonstrated the design and implementation of a multithreaded chat 
        application with real-time communication and robust user authentication. The project leveraged
        Java’s powerful networking and multithreading features to create a scalable and efficient solution.
        With features like server-side logging, typing indicators, and an intuitive GUI, the application meets
        all functional requirements and provides an excellent user experience.
      
        The knowledge and skills gained from this task will be invaluable for future projects involving 
        multithreading, networking, and user-centric application development.

# Output : 

![Image](https://github.com/user-attachments/assets/9963c26c-0853-40df-8ec6-7ab45594348d)
![Image](https://github.com/user-attachments/assets/78f91872-f813-438d-aefb-24c62bbbfca1)
![Image](https://github.com/user-attachments/assets/bc1edf43-3c4b-4fb9-a8de-2723bdea7cee)
![Image](https://github.com/user-attachments/assets/af94f5a5-dc5a-4bdf-8fc7-e2d1775aa6cd)
![Image](https://github.com/user-attachments/assets/ef436665-dd48-41d1-88e1-ae5986c308e9)
![Image](https://github.com/user-attachments/assets/173275da-e0e3-49b9-ab07-4dfd9eb7ee60)
![Image](https://github.com/user-attachments/assets/7c6b190e-5517-4b54-a300-47d3ecb2c3a8)
![Image](https://github.com/user-attachments/assets/fe941ea0-f775-42ea-b48b-a9210ecf1b6a)
  
  
  
