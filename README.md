# Project Loom

The presentation and Postman collection are located in the **assets** directory.  
This project demonstrates the use of Virtual Threads and compares changes between **Java 21** and **Java 25**.  
It also includes additional features related to Project Loom, listed below:

- **Virtual Threads** – Introduction to Java’s lightweight threads enabling massive concurrency.
- **Continuation** – Explanation and demo of continuations for pausing and resuming code execution.
- **Virtual Threads Examples** – Practical code samples demonstrating how to use virtual threads.
- **Scoped Value** – Usage of scoped values to share contextual data safely in concurrent code.
- **Structured Concurrency** – Managing groups of virtual threads as a single unit for better control and error handling.
- **Virtual Threads + Spring Boot** – Integrating virtual threads into Spring Boot applications for scalable web services.
- **Virtual Threads vs CompletableFuture** – Comparison of `Virtual Threads` and `CompletableFuture` in asynchronous programming.

---

## 🔥 Build

Before launching the API or running individual classes, you must add the required **VM options**.  
Descriptions of these options can be found inside the respective classes.

To run the project locally:

```bash
docker compose up -d
mvn clean install
```

---

## ⚙️ Tech Stack

- **Spring Boot 3.5.3**
- **Java 21** && **Java 25**
- **Project Loom** – Virtual Threads, ScopedValue, Structured Concurrency, `Continuation`, `ContinuationScope`
- **Docker Compose** – MySQL

---

## 📚 Sources

- [OpenJDK](https://openjdk.org/)
- *“Virtual Threads, 2 years later”* – Adam Warski, Devoxx Poland Conference
- *“Continuations: The magic behind virtual threads in Java”* – Balkrishna Rawool, Devoxx Poland Conference  
  ([GitHub Repository](https://github.com/balkrishnarawool/continuations))  