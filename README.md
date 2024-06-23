QuikScore
QuikScore is an Android app built with Kotlin and Jetpack Compose that displays live football scores in a floating window. The app is designed to hover over other applications, allowing users to keep track of live matches while multitasking. It features a minimal Material Theme design, with functionalities to drag the floating window, show live match updates, and close the window when desired.



Features
Live Football Scores: Fetches and displays real-time football match data.
Floating Window: The floating window can be moved around the screen and stays visible over other apps.
Minimal Material Design: Styled with a clean and simple Material Theme.
Draggable and Closeable: Users can drag the window to any position and close it with a single click.
Persistent Visibility: Remains visible over other applications for continuous score updates.
Screenshots

Floating window showing live scores.


Drag the floating window around the screen.


Floating window remains visible over other apps.


Close the floating window when not needed.

API Integration
QuikScore uses the API-Football to fetch live football scores. The API provides comprehensive and real-time data on football matches around the world.

Installation
Clone the Repository:

bash
Copy code
git clone https://github.com/yourusername/QuikScore.git
Open in Android Studio:
Open the cloned project in Android Studio.

Build and Run:
Ensure your development environment is configured for a TV 1080p viewport and run the app on an Android device or emulator.

Setup Instructions
API Key:

Sign up at API-Football to get your API key.
Add your API key to the local.properties file or directly in the code where the API call is made.
properties
Copy code
api_football_api=your_api_key_here
Gradle Sync:

Sync your Gradle files to ensure all dependencies are correctly installed.
Usage
Live Scores:

Launch the app to see live football scores.
The scores are fetched and displayed in the floating window, updating in real-time.
Floating Window:

Drag the window to any position on the screen.
The window remains visible over other apps, ensuring you don't miss any updates.
Close Button:

Click the close button on the window to dismiss it.
Architecture
QuikScore is built using the MVVM architecture pattern, ensuring a clear separation of concerns and making the codebase maintainable and scalable.

Key Components
ViewModel: Manages UI-related data and business logic.

MatchesViewModel: Handles live match data.
DetailedMatchViewModel: Manages detailed match information.
Repository: Responsible for fetching data from the API.

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
QuikScore relies on several key dependencies:

Jetpack Compose: For building the UI.
Hilt: For dependency injection.
Retrofit: For API calls.
Coroutines: For asynchronous operations.
Contribution
Contributions are welcome! Please fork the repository and submit a pull request with your improvements.
 
