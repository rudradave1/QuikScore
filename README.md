# QuikScore

![QuikScore](https://github.com/rudradave1/QuikScore/assets/35660907/ecb77b41-e96f-48ac-a1a9-647d64f74d20)

Floating live-score overlay for Android — exploring system-level UI, background updates, and real-time data rendering outside standard app boundaries.

---

## Why this exists

Android provides limited guidance for building persistent UI that lives outside the app (e.g., chat heads, overlays).  
This project explores how to safely and reliably implement:

- system overlay windows
- real-time updates without foreground UI
- background execution constraints

---

## Key Focus Areas

- **System Overlay Windows**
  - Draw over other apps using overlay permissions
  - Draggable, persistent floating UI

- **Real-Time Data Updates**
  - Live football scores fetched from remote API
  - Continuous updates without interrupting user activity

- **Background + UI Coordination**
  - Keeping UI responsive while updating data
  - Handling lifecycle outside traditional Activity/Fragment model

---

## Architecture


UI (Compose overlay window)
↓
ViewModel (state holder)
↓
UseCases
↓
Repository
↓
Remote API (API-Football)


### Design Notes

- UI is intentionally minimal to prioritize **system behavior over visuals**
- ViewModel manages state independently of traditional screens
- Repository abstracts API interactions for easier replacement/testing

---

## Key Engineering Decisions

- **Overlay-based UI instead of Activity**
  Enables persistent visibility across apps

- **MVVM for state isolation**
  Keeps UI reactive even outside standard lifecycle

- **Compose for rapid UI iteration**
  Simplifies rendering dynamic floating components

---

## Tradeoffs

- Overlay permission introduces UX friction and user trust concerns  
- Background execution limits vary across OEMs and Android versions  
- Real-time updates increase battery/network usage  

---

## Features

- Floating live-score window over any app
- Draggable and repositionable UI
- Real-time football match updates
- Minimal, distraction-free design

---

## Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/rudradave1/QuikScore.git

Add API key:

api_football_api=your_api_key_here
Run on device (overlay permission required)
Limitations
No offline caching (network required)
Limited background resilience on aggressive battery-optimized devices
Depends on third-party API reliability
Future Improvements
Offline-first caching for recent matches
Smarter refresh strategies (reduce battery usage)
Expand to multiple sports or event types
Notification-based fallback instead of persistent overlay
License

MIT License


---
