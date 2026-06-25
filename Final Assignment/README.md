# Smart Equipment Rental & Billing System (Self-Service Kiosk Model)

A modern, desktop-based self-service kiosk application implemented in Java Swing that allows campus renters (students, staff, and final-year students) to register, borrow, and return laboratory, electronics, and media equipment.

---

## Key Features

- **Shopee-Inspired Card Flow:** A clean blue and white university theme supporting separate **Log In** and **Sign Up** cards, dynamically switched via active hyperlink toggle triggers.
- **E-Commerce Style Checkout:**
  - **Rental Information:** View rental durations.
  - **Order Summary:** Displays itemized selections showing planned calculations (`days × $rate/day = base fee`).
  - **Billing & Settlement Details:** Real-time estimates detailing subtotals, role-based discounts (20% for Staff, 10% for Final Year Students), security deposits, and a grand total payable.
- **Detailed Settlement Receipt:** When returns are processed, the system prints a detailed receipt showing daily base fees, applied discounts, late penalties, damage fees, deposit deductions, and the net refund or outstanding balance.
- **Admin Administrative Panel:** Gated by credentials, allowing staff to manage equipment conditions (`AVAILABLE`, `RENTED`, `MAINTENANCE`, `DAMAGED`) and add/remove inventory.
- **Robust Logic & Automated Verification:** Complete test runner validating 95 test scenarios covering all user roles, categories, and returns.

---

## System Requirements

- **Java Development Kit (JDK):** Version 8 or newer (JDK 22 tested and supported).
- **Environment:** Terminal/PowerShell on Windows, macOS, or Linux.

---

## How to Build and Run

All commands should be executed from the root repository directory.

### 1. Running the Main GUI Application
Compile and run the self-service kiosk GUI:
```powershell
# Compile the application
javac -d "Final Assignment/bin" -sourcepath "Final Assignment/src" "Final Assignment/src/Main.java"

# Launch the application
java -cp "Final Assignment/bin" Main
```

### 2. Running the QA Test Suite
Compile and run the automated matrix test runner:
```powershell
# Compile the test runner
javac -d "Final Assignment/bin" -sourcepath "Final Assignment/src" "Final Assignment/src/TestRunner.java"

# Run tests
java -cp "Final Assignment/bin" TestRunner
```

---

## Credentials for Testing

- **Self-Service Renter Portal:** 
  - Register as a new user by clicking **Sign Up** at the bottom of the card.
  - Log in using your registered **User ID** and **Full Name**.
- **Admin Verification Panel:**
  - Click **Admin Panel Access** at the bottom of the log-in page.
  - **Admin ID:** `admin123`
  - **Password:** `admin123`

---

## Code Architecture & Object-Oriented Principles

- **Facade Design Pattern:** The `RentalSystemFacade` acts as a unified entry point, coordinating the `EquipmentManager`, `RentalManager`, and `BillingManager` subsystems to decouple the GUI from backend logic.
- **Polymorphism & Abstraction:** An abstract base class `Equipment` defines polymorphic methods (`calculateBaseFee` and `calculatePenalty`) which concrete classes (`Electronics`, `MediaEquipment`, and `LaboratoryEquipment`) override to calculate category-specific rates, discount thresholds, and late penalty multipliers.
- **Encapsulation:** Renter and billing records maintain rigorous data privacy, updating states through clear getters/setters and transactional methods.
