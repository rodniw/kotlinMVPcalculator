## MVP Kotlin Calculator

### Architecture:
- Presenter: Presenter is the "Logic" center of the Application. It is the thing which you must be most certain to test, as it essentially coordinates "the dance" (à la Uncle Bob), of the View and Model. It should not be full of libs (RxJava is the exception as I do need some kind of concurrency tool), and generally shouldn't have references to Android Framework Classes.
- View: The View binds data to XML, and forwards click events to the Presenter. With little exception, it shouldn't contain complex logic (complexity necessitates testing, I want my View to be relatively unnecessary to test), and essentially does what it is told.  
- Model: Models 'Model' (as in the verb) the information relevant to the Application. This can be long-term (like a Database), or short-term (like a ViewModel, and even a POJO/Data Class). Like the View, the Model shouldn't contain much logic (in most cases anyway). In this App, it helps us maintain and decouple the UI State, which keeps it safe from being destroyed by orientation changes, thus leading to poor UX.
- UseCase: To avoid our Presenter becoming too complex, and to reduce it's coupling to the backend (Validator and Calculator), the Presenter talks to a Use Case (or Interactor).
- Repository: In order to decouple the Use Case from specific backend implementations, it talks to them via Interfaces (which can be thought of as the Repository/Facade Pattern). This means that we could swap out Validator or Calculator for something entirely different, and we would not need to change the rest of the Application one bit!
- Activity: Container for my MVP Components.
- Injector: A simple home baked Dependency Injection class.

