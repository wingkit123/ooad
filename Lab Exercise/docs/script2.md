# Hospital Management System (HMS) - Presentation Video Script (Final Code-Only Version)

This script follows the exact 5-section sequence required by Section 7 of the assignment rubric. 
* Section 4 is fully self-contained for Presenter 3 to perform a solo demo.
* Section 5 is custom-tailored exclusively for Wing Kit to deliver a high-impact, professional code review of core OOP principles.

Clear, non-complex English phrasing is paired with Chinese contextual notes (`[中文提示]`) to aid delivery.

---

## 🕒 Video Timing & Presenter Rotation Sheet (Finalized)

| Rubric Section | Presentation Topic | Target Timing | Primary Speaker |
| :--- | :--- | :--- | :--- |
| **Section 1** | **Introduction & System Overview** | 0:00 - 1:00 (1 min) | **Presenter 2 (Wing Kit)** |
| **Section 2** | **Class Diagram Design & Rationale** | 1:00 - 4:00 (3 mins) | Presenter 1 & Presenter 4 |
| **Section 3** | **Sequence Diagrams Workflow Analysis** | 4:00 - 7:00 (3 mins) | Presenter 4 |
| **Section 4** | **Live System Functional Demonstration** | 7:00 - 13:00 (6 mins) | Presenter 3 (Solo Demo Friend) |
| **Section 5** | **Code Architecture & OOP Implementation** | 13:00 - 15:00 (2 mins) | **Presenter 2 (Wing Kit)** |

---

## 🎬 Full Production Script

### 🎬 Section 1: Introduction (0:00 - 1:00)
**[Screen Action]:** *Show Title Slide containing "Hospital Management System (HMS) MVP", Group Section, and all 4 Student Names/IDs clearly listed.*

**Presenter 2 (Wing Kit):**
> "Hello everyone, and welcome to our project presentation. I am Wing Kit, the Lead Software Engineer and Architect for this project. I will be guiding you through our system overview and later explain our backend code implementation. Before we begin, let me introduce our group members:
> * **Presenter 1:** [Name & ID] - Project Coordinator
> * **Presenter 2 (Wing Kit):** See Wing Kit [& ID] - Lead Software Engineer & Architect
> * **Presenter 3:** [Name & ID] - Live System Presenter
> * **Presenter 4:** [Name & ID] - Systems Integrator
> 
> *Visual Cue: Switch to Slide 2 - System Overview Map*
> 
> Our application is built to optimize administrative workflows inside clinical environments. The entire object blueprint and package structures are fully detailed in our repository named `"wingkit123/ooad"`.
> 
> Let's open our structural blueprints and examine Section 2: the Class Diagram."

---

### 🎬 Section 2: Class Diagram (1:00 - 4:00)
**[Screen Action]:** *Switch on-screen image to your Class Diagram. Zoom in onto the upper section containing the base User attributes.*

**Presenter 1:**
> "For our static structure design, we organized our domain objects into dedicated model packages.
> 
> *(Point mouse cursor to the top `User` box)* At the foundation of our access control model, we have an abstract base class named `User`. This class encapsulates shared account attributes such as `username`, `password`, and `displayName`. To enforce strict role boundaries across the application, we coupled this structure with an Enum class called `Role`, which defines our constant entries for `ADMIN`, `DOCTOR`, and `RECEPTIONIST`.
> 
> Presenter 4 will now explain our structural relationships and design pattern justifications."

**Presenter 4:**
> "Thank you, Presenter 1. Let's look closer at our structural associations and object constraints.
> 
> *(Hover mouse cursor over the inheritance arrows)* We implemented the principle of **Inheritance** by extending our base `User` class into three concrete sub-classes: `AdminUser`, `DoctorUser`, and `ReceptionistUser`. This layout forces structural consistency while allowing polymorphic behavior when loading specific user views.
> 
> *Visual Cue: Move mouse cursor to point to `Appointment`, `Patient`, and `Doctor` boxes*
> 
> *(Point to `Appointment` connections)* An `Appointment` entity maintains a clear **Composition** relationship with exactly one `Patient` and one `Doctor` instance. It contains independent tracking variables for `date` and `time` to accurately map clinical scheduling realities.
> 
> *Visual Cue: Move mouse to highlight the `SystemController` block*
> 
> We strongly justify this layered structure through the **Facade Design Pattern** `[中文提示: 外观设计模式]`. Our view frames are completely decoupled from backend data mutations. They talk exclusively to a unified interface layer called `SystemController`. The controller acts as our system facade, safe-delegating user actions out to independent service managers like `PatientService` or `AppointmentService`.
> 
> Now, let's transition smoothly into Section 3 to evaluate our dynamic execution paths via Sequence Diagrams."

---

### 🎬 Section 3: Sequence Diagrams (4:00 - 7:00)
**[Screen Action]:** *Switch on-screen view to `uml/sequence-login.puml`. Trace line movements horizontally from left to right as you present the message passes.*

**Presenter 4:**
> "In Section 3, we analyze how our decoupled objects interact dynamically at runtime.
> 
> Let's first examine the **Login Workflow** `[中文提示: 登录流程]`. When a user submits their credentials from our `LoginFrame`, the view captures the input and invokes `SystemController.login()`. The controller forwards the request to the `AuthenticationService`, which queries our data access layer, the `HospitalRepository`. If the records match, the backend returns a successful state, prompting the controller to securely launch the correct dashboard view.
> 
> *Visual Cue: Switch image file to `uml/sequence-add-patient.puml`*
> 
> Next is our **Record Management flow**, specifically focusing on adding a patient `[中文提示: 患者信息维护流程]`. When the receptionist hits the submit form, the payload passes through the controller straight into our business layer, `PatientService`. The service calls `HospitalRepository.addPatient()`, which runs an internal auto-increment identity counter to issue a unique patient ID. The saved object reference flows back up to update our visual tables automatically.
> 
> *Visual Cue: Open `uml/sequence-book-appointment.puml` and highlight the alt combined fragment block*
> 
> Finally, let's look at our most critical sequence: the **Appointment Booking Flow** `[中文提示: 预约挂号冲突校验流程]`. Before confirming a transaction, the `AppointmentService` pulls active records from the repository to perform state validation. As shown inside this conditional **Alternative Block** `[中文提示: 选择分支组合碎片]`, if the targeted doctor already has an active slot matching the requested date and time, our backend catches the conflict and throws a custom `DuplicateAppointmentException`. This instantly stops execution, protects database integrity, and pushes an error code to the UI.
> 
> Now, for Section 4, Presenter 3 will take you through a full live demonstration of our running application."

---

### 🎬 Section 4: System Demonstration (7:00 - 13:00)
**[Screen Action]:** *Launch the live Java Swing application. Presenter 3 takes full control of the interaction, performing actions cleanly and matching the pace of the narration.*

**Presenter 3 (Solo Demo):**
> "Thank you, Presenter 4. Now, I will present a complete live demonstration of our working Hospital Management System, guiding you directly through our core user workflows.
> 
> **Step 1: Secure Role-Based Login `[中文提示: 角色登录与权限控制]`**
> Let's start with secure authentication. I will type the credentials `reception` and `reception123` to log in as a receptionist. The system loads our role dashboard. If I click on the Doctors tab, you will notice that all management inputs and action buttons are disabled. This proves our role-based security works: receptionists only have read-only access here.
> 
> Let's sign out and log back in using our Admin credentials: `admin` and `admin123`. As an administrator, our dashboard completely unlocks full management controls across all windows.
> 
> **Step 2: Add Patient Record with Input Validation `[中文提示: 添加患者与输入校验]`**
> Let's move to the Patients tab to test our input filters. If I intentionally input characters like 'abc' inside our Age field and click add, our controller intercepts the bad data and blocks the transaction, showing a popup warning: 'Patient age must be a valid positive integer'. 
> 
> Now, let's enter valid data: Name as 'Alice Green', Age as '30', Gender as 'Female', and click **Add Patient**. The table updates instantly with our new entry and its unique generated tracking ID.
> 
> **Step 3: Conflict-Free Appointment Booking `[中文提示: 冲突拦截演示]`**
> Let's switch over to the Appointments tab to schedule a clinical visit. The patient dropdown automatically reflects our new patient, 'Alice Green', and our available doctor, 'Dr. Kumar'. Let's pick date '2026-06-20' at time '09:00' and click **Book Appointment**. The record saves smoothly.
> 
> Now, let's trigger our scheduling safety logic. I will attempt to book an appointment with the exact same doctor at the exact same date and time slot again. When I click the button, our backend validation catches the duplicate state, blocks the double-booking, and pops up our caught exception message: 'Doctor already has an appointment at this date and time'. 
> 
> **Step 4: View Aggregated Report `[中文提示: 实时统计报表]`**
> Finally, let's click the Reports tab and select **Generate Report**. The application aggregates raw system data in real-time, displaying total registered patients, total scheduled sessions, and complete summaries of active doctor workloads.
> 
> Now, our Lead Software Engineer, Wing Kit, will guide you through Section 5 to explain our core code implementation."

---

### 🎬 Section 5: Code Explanation (13:00 - 15:00)
**[Screen Action]:** *Wing Kit shares his code editor view, explicitly highlighting code lines, keywords, and class properties as he reviews them.*

**Presenter 2 (Wing Kit - Code Only):**
> "Thank you, Presenter 3. To conclude our presentation, I will explain how our underlying backend source code maps out core Object-Oriented Programming principles `[中文提示: 源码层面的面向对象原则落地讲解]`.
> 
> *Visual Cue: Open `hms/model/User.java` in the editor*
> 
> Starting with abstraction and inheritance `[中文提示: 抽象与继承的源码表现]`, here is our base domain model class, `User.java`. As you can see, it is explicitly declared with the `abstract` modifier `[中文提示: 抽象类关键字]`. This guarantees that it cannot be initialized directly, while concrete subclasses like `DoctorUser.java` and `AdminUser.java` extend it to share baseline attributes and implement their own version of the abstract home message method.
> 
> *Visual Cue: Open `hms/model/Patient.java` in the editor*
> 
> Next, if we look at our `Patient` model file, we practice strict encapsulation `[中文提示: 封装与数据防御]`. All operational fields like `name` and `age` are declared as `private` and accessed only through clean public getters. To protect our object state, any modifications must pass through a unified update method that runs validation logic to intercept empty values or negative ages before they can save.
> 
> *Visual Cue: Open `hms/service/AppointmentService.java`*
> 
> Finally, our business rules are decoupled from the user interface and isolated in service components like `AppointmentService.java` `[中文提示: 业务逻辑与界面解耦]`. For example, our scheduling safety checks are executed at the service layer, where we query the repository to ensure no duplicate slot allocations occur, preventing double-bookings. By separating concerns this way, we keep our codebase highly maintainable, clean, and safe from runtime state corruption.
> 
> This architecture ensures that our Hospital Management framework remains fully future-proof and ready for secondary module expansions. 
> 
> That brings us to the conclusion of our presentation. Thank you very much for your time and attention!"
