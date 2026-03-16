# Privy | Password Manager

An Offline Password Manager solution for privacy geeks out there 😎

# Preview
| Login | Dashboard |
| :--- | :--- |
| ![Login](/screenshots/new_login.png) | ![Dashboard](/screenshots/new_dashboard.png) |

# Under Development

Coming soon!

# Getting Started

This project is built using Java 21, JavaFX, and Maven. While the program is still under development, if you want to make a copy of the program, please follow the steps below.

## Prerequisites

- Java Development Kit (JDK) 21 or higher.

- Maven (integrated in most IDEs).

- SQLite (The database file privy.db will be initialized automatically on the first run).

## Step 1: Clone the Repository

First, you need to get the source code onto your local machine. Open your terminal or command prompt and run:

``` 
git clone https://github.com/bray-hiramis/privy.git
```
```
cd privy 
```

## Step 2: How to Run

### Option 1: Using IntelliJ IDEA

1. Open IntelliJ and select File > Open.

2. Navigate to the cloned privy folder and select the pom.xml file.

3. Choose Open as Project.

4. Wait for Maven to download dependencies (check the progress bar at the bottom).

5. Open src/main/java/com/privy/App.java.

6. Right-click anywhere in the file and select Run 'App.main()'.

### Option 2: Using Eclipse IDE

1. Go to File > Import...

2. Select Maven > Existing Maven Projects and click Next.

3. Browse to the cloned privy root directory and click Finish.

4. Right-click on the project in the Project Explorer.

5. Select Run As > Java Application.

6. Select App - com.privy as the entry point.

### Option 3: Using the Command Line (Terminal)

If you have Maven installed globally, you can run the app without opening an IDE:

```
mvn clean javafx:run
```

## Troubleshooting

- Please let me know if you encouter issues running the program so I can assist :)

# Update Logs

- Feb. 23, 2026
   - Successfully connected the database (sqlite) and implemented the login function.
- Feb. 24, 2026
   - Refactor the code for the user login to populate the tableview in the Dashboard.
   - Designed the dashboard so it will be responsive in full screen.
- Feb. 28, 2026
   - Added the function to show the password details in the textfields upon clicking on the table (list of passwords).
   - Design the first iteration of the *Create Account form*.
- March 01, 2026
   - Added the security questions (static database) to the Create Account form (UI).
- March 05, 2026
   - *Create new account form* is complete and working:
      - Added a feature to Hash the password using *PBKDF2*.
      - Added a validation to check if email format is valid using *Java Regex*.
   - Updated the login function so it will accept the Hash Password.
- March 06, 2026
   - Added a show and hide password feature in Create Account form.
   - Added an additional error handler to ensure user typed a minimum of 8 character password.
   - Added a Navigation class to control the switching scene.
- March 07, 2026
   - Design the form for Reset Password:
      - Verification requires:
         - Recovery email and Security Question - status: *working*.
         - Verfiy Answer function - status: *work in progress*.
         - Reset Password function - status: *work in progress*.
- March 08, 2026
   - For Reset Password form:
      - Verfiy Answer function - status: *working*.
      - Reset Password function - status: *work in progress*.
- March 09, 2026
   - For Reset Password form:
      - Reset Password function - status: *working*.
   - All forms have *Show and Hide password* features.
- March 10, 2026
   - Dashboard Update:
      - Updating your saved password is working.
      - Add password form is done and added a function so it will show in the Dashboard.
- March 11, 2026
   - Dashboard Update:
      - Some quality of life fixes when switching to Add New Password form.
      - Add new password is now working and saving to the database.
- March 12, 2026
   - Dashboard Update:
      - Quality of life fixes again when switching to Add New Password form.
      - Deleting a saved password is now working.
- March 13, 2026
   - Dashboard Update:
      - Work on the logout function.
- March 14, 2026
   - Dashboard Update:
      - Dynamic Search is now working.
- March 15, 2026
   - Added *Change Master Password* form added a method for it to appear in the Dashboard form.