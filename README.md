# SafePass
This is a password manager app developed in Kotlin for Android using the MVVM architecture. The app allows users to securely store their passwords and other sensitive information. It uses a Room database to persist the passwords, ViewModels to manage the data, Kotlin Flows and Coroutines for asynchronous tasks, Cipher for encryption, Dependency Injection for modularity, and the Android Navigation Component to manage the app's navigation.

## Features
  - Save and manage your passwords in an encrypted database
  - Automatic logout when the app is in the background
  - Secure password generation
  - Biometrics Login

## MVVM Architecture
The Model-View-ViewModel (MVVM) architecture is a design pattern that separates the business logic of an application from its user interface. It consists of three layers:

### Model
The model layer is responsible for handling the data and business logic of the app. In this app, the model layer consists of the Room database, which is used to store and retrieve the passwords. The database is managed by a Repository class, which acts as an intermediary between the ViewModel and the database.

### View
The view layer is responsible for displaying the data to the user and handling user input. In this app, the view layer consists of the app's Activity and Fragment classes, which define the layout and user interface elements.

### ViewModel
The ViewModel layer is responsible for managing the data presented to the user and handling the communication between the Model and View layers. It retrieves data from the Repository and prepares it for display in the View, and it also handles user input and updates the Model as necessary.

## TechStacks
  - [**Kotlin Flows and Coroutines**](https://kotlinlang.org/docs/reference/coroutines-overview.html)
    Kotlin Flows and Coroutines are used to perform asynchronous tasks in the app, such as querying the database or encrypting and decrypting data. Kotlin Flows are a new addition to the Kotlin language that allow developers to build reactive streams of data, while Coroutines are a lightweight way to perform asynchronous tasks.
    
  - [**Cipher**](https://developer.android.com/reference/javax/crypto/Cipher)
    The Cipher class is used to encrypt and decrypt data in the app. It uses a secure key to ensure that the data is only accessible to the user who created it.
    
  - [**Dependency Injection**](https://en.wikipedia.org/wiki/Dependency_injection)
    Dependency Injection is used in this app to improve modularity and testability. It allows developers to specify the dependencies of a class in a declarative way, rather than creating them directly within the class. This makes it easier to swap out implementations of a dependency and to write unit tests for the class.
    
  - [**Android Navigation Component**](https://developer.android.com/guide/navigation)
    The Android Navigation Component is a framework that helps developers manage the navigation of their app. It provides a way to define the different screens in the app and the actions that can be taken to move between them, as well as handling the back stack and saving the state of the app.

## Contributions
We welcome contributions to this project. If you have any suggestions or want to report a bug, please open an issue or submit a pull request
