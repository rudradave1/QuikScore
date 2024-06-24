QuikScore
QuikScore is an Android app built with Kotlin and Jetpack Compose that displays live football scores in a floating window. The app is designed to hover over other applications, allowing users to keep track of live matches while multitasking. It features a minimal Material Theme design, with functionalities to drag the floating window, show live match updates, and close the window when desired.
                            ![QuikScore](https://github.com/rudradave1/QuikScore/assets/35660907/ecb77b41-e96f-48ac-a1a9-647d64f74d20)

Live Football Scores: Fetches and displays real-time football match data.
Floating Window: The floating window can be moved around the screen and stays visible over other apps.
Minimal Material Design: Styled with a clean and simple Material Theme.
Draggable and Closeable: Users can drag the window to any position and close it with a single click.
Persistent Visibility: Remains visible over other applications for continuous score updates.
Screenshots

![qsmain](https://github.com/rudradave1/QuikScore/assets/35660907/de880413-7a9a-4926-872f-4256ef625823)



API Integration
QuikScore uses the API-Football service to fetch live football scores. This API provides comprehensive and real-time data on football matches around the world.

Installation
Clone the Repository:

```git clone https://github.com/rudradave1/QuikScore.git```

Open in Android Studio:
Open the cloned project in Android Studio.

Build and Run:
Ensure your development environment is configured for a TV 1080p viewport and run the app on an Android device or emulator.

Setup Instructions
API Key:
Sign up at API-Football to get your API key.

Add your API key to the local.properties file or directly in the code where the API call is made: 
```api_football_api=your_api_key_here```

Gradle Sync:
Sync your Gradle files to ensure all dependencies are correctly installed.

Usage
Live Scores:
Launch the app to see live football scores in the floating window. The scores are fetched and updated in real-time.

Floating Window:
Drag the window to any position on the screen. The window will remain visible over other apps, ensuring continuous updates.

Close Button:
Click the close button on the window to dismiss it.

Architecture
QuikScore is designed using the MVVM (Model-View-ViewModel) architecture pattern, ensuring a clear separation of concerns and enhancing code maintainability and scalability.

Key Components
ViewModel: Manages UI-related data and business logic.
MatchesViewModel: Handles live match data.
DetailedMatchViewModel: Manages detailed match information.
Repository: Responsible for data fetching from the API.
MatchesRepository: Interface defining data operations.
MatchesRepositoryImpl: Implementation of the data fetching logic.
UI Components:
LiveScoreBubble: Displays live scores in a floating bubble.
FloatingMatchDetailsWindow: Shows detailed match information.
TeamScoreColumn, TeamInfo, GameTimeText: Various UI components used to build the score display.
Networking:
ApiService: Defines the API endpoints and interactions.
GetLiveMatchesUseCase, GetMatchByIdUseCase: Use cases for fetching live matches and match details.
Dependencies
QuikScore leverages several key dependencies to provide a robust and efficient experience:

Jetpack Compose: For building the UI.
Hilt: For dependency injection.
Retrofit: For API calls.
Coroutines: For managing asynchronous operations. 
 
