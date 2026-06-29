# Hospital Management System (HMS) - Presentation Video Script (Updated)

This script follows the exact 5-section sequence required by your guidelines. Section 4 is now fully self-contained for one single presenter to demonstrate the live running system. 

Clear English phrasing is paired with Chinese contextual notes (`[中文提示]`) to assist with technical terms and transitions.

---

## 🕒 Video Timing & Presenter Rotation Sheet (Revised)

| Rubric Section | Presentation Topic | Target Timing | Primary Speaker |
| :--- | :--- | :--- | :--- |
| **Section 1** | **Introduction & System Overview** | 0:00 - 1:00 (1 min) | Presenter 1 |
| **Section 2** | **Class Diagram Design & Rationale** | 1:00 - 4:00 (3 mins) | Presenter 1 & **Presenter 2 (Wing Kit)** |
| **Section 3** | **Sequence Diagrams Workflow Analysis** | 4:00 - 7:00 (3 mins) | **Presenter 2 (Wing Kit)** & Presenter 4 |
| **Section 4** | **Live System Functional Demonstration** | 7:00 - 13:00 (6 mins) | **Presenter 3 (Solo Demo Friend)** |
| **Section 5** | **Code Architecture & OOP Implementation** | 13:00 - 15:00 (2 mins) | Presenter 4 |

---

## 🎬 Full Production Script

### 🎬 Section 1: Introduction (0:00 - 1:00)
**[Screen Action]:** *Show Title Slide containing "Hospital Management System (HMS) MVP", Group Section, and all 4 Student Names/IDs clearly listed.*

**Presenter 1:**
> "Hello everyone, and welcome to our project presentation. Our team has designed and implemented a standalone **Hospital Management System MVP** using Java Swing. Before we begin, let me introduce our group members:
> * **Presenter 1:** [Name & ID] - Project Coordinator
> * **Presenter 2 (Wing Kit):** See Wing Kit [& ID] - Lead Architect & Core Logic Developer
> * **Presenter 3:** [Name & ID] - Live System Presenter
> * **Presenter 4:** [Name & ID] - Systems Integrator & Code Reviewer
> 
> *Visual Cue: Switch to Slide 2 - System Overview Map*
> 
> Our application is built to improve medical workflows by supporting three core roles: Admin, Doctor, and Receptionist. The complete system implementation and model diagrams are fully detailed in our repository named `"wingkit123/ooad"`.
> 
> Let's open our structural blueprints and examine Section 2: the Class Diagram."

---

### 🎬 Section 2: Class Diagram (1:00 - 4:00)
**[Screen Action]:** *Switch on-screen image to your Class Diagram. Zoom in onto the upper section containing the User class hierarchy.*

**Presenter 1:**
> "For our static structure design, we created a centralized object model mapped across specific domain packages. 
> 
> *(Point mouse cursor to the top `User` box)* At the foundation of our access control model, we have an abstract base class `User`. This class encapsulates shared account credentials like `username`, `password`, and `displayName`. To support distinct roles inside a hospital, we use an Enum class called `Role`, which contains `ADMIN`, `DOCTOR`, and `RECEPTIONIST`.
> 
> I will now pass the floor to Wing Kit to explain our structural inheritance and design justifications."

**Presenter 2 (Wing Kit):**
> "Thank you, Presenter 1. Let's look closer at our structural hierarchy and class relationships `[中文提示: 类的继承与关联]`.
> 
> *(Hover mouse cursor over the inheritance lines pointing down to sub-classes)* We implemented the principle of **Inheritance** `[中文提示: 继承]` by extending our base `User` class into three concrete sub-classes: `AdminUser`, `DoctorUser`, and `ReceptionistUser`. Each subclass overrides a polymorphic method to deliver role-specific dashboards back to the user interface.
> 
> *Visual Cue: Move mouse cursor to point to `Appointment`, `Patient`, and `Doctor` boxes*
> 
> *(Point to `Appointment` connections)* An `Appointment` object maintains a clear **Composition** relationship `[中文提示: 组合关系]` with exactly one `Patient` and one `Doctor` instance. It contains specific fields for tracking `date` and `time` to model real-world clinical entries.
> 
> *Visual Cue: Hover mouse around the Service blocks and SystemController*
> 
> We strongly justify this structure through the **Facade Design Pattern** `[中文提示: 外观设计模式]`. Our Swing UI frames never communicate directly with domain models. Instead, they communicate exclusively through a single gateway class called `SystemController`. This controller acts as our system facade, delegating actions out to highly specialized services like `PatientService` or `AppointmentService`. This keeps our system decoupled and easily expandable.
> 
> Next, let's look at Section 3 to see our Sequence Diagrams."

---

### 🎬 Section 3: Sequence Diagrams (4:00 - 7:00)
**[Screen Action]:** *Switch on-screen view to your Login Sequence Diagram. Trace line movements horizontally from left to right as you present.*

**Presenter 2 (Wing Kit):**
> "In Section 3, we analyze how our decoupled objects interact dynamically at runtime `[中文提示: 对象的动态交互流向]`. 
> 
> Let's look at the **Login Process workflow** `[中文提示: 登录流程]`. When a user types their details inside our `LoginFrame`, the view triggers the operation by passing arguments into `SystemController.login()`. The controller delegates authentication to our `AuthenticationService`. The service loops through the user collection loaded from our central `HospitalRepository`. If the username and password match perfectly, the controller safely opens the role-based dashboard.
> 
> *Visual Cue: Switch image file to the Patient Management Sequence Diagram*
> 
> Next is our **Record Management flow**, specifically adding a new patient `[中文提示: 患者信息维护流程]`. When a receptionist submits registration fields, the request travels through `SystemController` into `PatientService`. The service calls `HospitalRepository.addPatient()`, which encapsulates an auto-increment identity counter to generate a distinct patient ID. The newly created `Patient` reference is returned back up to refresh our UI data table.
> 
> Presenter 4 will now take over to present our critical appointment scheduling validation."

**Presenter 4:**
> "Thank you, Wing Kit. Let's move to our most critical validation pathway: the **Appointment Booking Flow** `[中文提示: 预约挂号流程]`.
> 
> *Visual Cue: Open the Appointment Booking Sequence Diagram and point to the alt conditional block*
> 
> When a booking request is made, the `AppointmentService` fetches all current bookings from the repository to verify scheduling conflicts. As explicitly mapped inside this conditional **Alternative Block** `[中文提示: 选择分支组合碎片]`, if a duplicate slot matches the same doctor, date, and time, the execution branch throws a `DuplicateAppointmentException` back to the view. This completely stops the transaction and prompts an alert on the screen. Only when the slot is confirmed available does the system save the record.
> 
> Now, for Section 4, Presenter 3 will guide you through a complete, live demonstration of our working system."

---

### 🎬 Section 4: System Demonstration (7:00 - 13:00)
**[Screen Action]:** *Launch the live Java Swing application. Presenter 3 takes full control of the screen. Keep the app centered and clearly visible, performing actions smoothly as you speak.*

**Presenter 3 (Solo Demo):**
> "Thank you, Presenter 4. Now, I will present a complete live demonstration of our working Hospital Management System, guiding you step-by-step through our core workflows.
> 
> **Step 1: Secure Role-Based Login `[中文提示: 角色登录与权限控制]`**
> First, let's look at authentication. I will type the credentials `reception` and `reception123` to log in as a receptionist. Notice that the dashboard instantly displays a custom welcome message showing our role. If we click on the Doctors management tab, you will notice that all input fields and action buttons are greyed out. This proves our role-based authorization works perfectly: receptionists only have read-only permissions here.
> 
> Let's log out, and log back in using our Admin account: `admin` and `admin123`. As an administrator, our dashboard completely unlocks full management controls across all modules.
> 
> **Step 2: Add Patient Record with Input Validation `[中文提示: 添加患者与输入校验]`**
> Let's move to the Patients tab to demonstrate our system's data defense. If I try to input characters like 'abc' inside the Age field and click add, our controller catches the error and blocks the transaction, showing a clear warning popup: 'Patient age must be a valid positive integer'. 
> 
> Now, let's enter valid information: Name as 'Alice Green', Age as '30', Gender as 'Female', and click **Add Patient**. The table instantly updates with our new patient entry and displays their auto-generated ID.
> 
> **Step 3: Conflict-Free Appointment Booking `[中文提示: 预约挂号与冲突拦截]`**
> Next, let's switch over to the Appointments tab to schedule a consultation. The patient dropdown automatically displays our newly registered patient, 'Alice Green', and our available doctor, 'Dr. Kumar'. Let's book an appointment for date '2026-06-20' at time '09:00' and click **Book Appointment**. The booking completes successfully and appears in the schedule grid.
> 
> Now, let's test our conflict interceptor. I will try to book the exact same doctor at the exact same date and time slot again. When I click the button, our backend validation catches the conflict, blocks the double-booking, and displays a clear warning popup: 'Doctor already has an appointment at this date and time'. This proves our system successfully protects clinical schedules.
> 
> **Step 4: View Aggregated Report `[中文提示: 查看统计报表]`**
> Finally, let's move to our Reports tab and click **Generate Report**. The system gathers data directly from our repository layer and displays live hospital summaries, including our total patient count, total active appointments, and organized daily schedules.
> 
> Now, Presenter 4 will present our final section: Section 5 for the Code Explanation."

---

### 🎬 Section 5: Code Explanation (13:00 - 15:00)
**[Screen Action]:** *Open your code editor. Bring up the backend source files and highlight key class lines using your cursor as they are mentioned.*

**Presenter 4:**
> "To wrap up our presentation, we will review how our source code implements core Object-Oriented Programming principles.
> 
> *Visual Cue: Open `hms/model/User.java` in the editor*
> 
> **1. Abstraction and Inheritance `[中文提示: 抽象与继承]`:**
> Look at our `User` class definition. It is declared as an `abstract class`. It defines our structural blueprint while protecting constructor access via the `protected` modifier. Subclasses like `AdminUser.java` or `DoctorUser.java` extend this base structure, showcasing clean Inheritance.
> 
> *Visual Cue: Open `hms/model/Patient.java` in the editor*
> 
> **2. Encapsulation and Data Defense `[中文提示: 封装与数据防御]`:**
> Inside our `Patient` model class, all core variables like `name`, `age`, and `gender` are strictly declared as `private` fields. Data can only be read through explicit getters. We also implemented data validation directly inside our setters to block invalid formatting or negative numbers from corrupting our database.
> 
> *Visual Cue: Open `hms/service/AppointmentService.java`*
> 
> **3. Business Layer Rationale `[中文提示: 业务层架构合理性]`:**
> Finally, our `AppointmentService` coordinates our core scheduling constraints. It completely encapsulates conflict checking and exception handling, ensuring that our core rules are protected in the business service layer rather than relying on UI controls. This decoupled framework ensures that our system is highly maintainable, scalable, and easy to extend.
> 
> This marks the end of our presentation. Thank you very much for your time!"
