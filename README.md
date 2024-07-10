# Search


The app use the github api. https://api.github.com/


The app has one screen with a search bar and a list allowing to scroll the result 
of your research indefinitly.


- The code base is entirely in Kotlin.
- Dependency injection is done using Hilt.
- The list works with pagination.
- The app architecture has three layers: data, domain, feature (UI).
- The entire app is implemented using coroutines.
- Several Jetpack libraries are used, such as: Material, Paging, Compose, Hilt, Room, Test.
- And an unit test example implementation of a mapping function in MapperTest.kt
