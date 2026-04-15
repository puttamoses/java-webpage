# 🏎️ Car Ordering Puzzle — Java WAR Application

An interactive drag-and-drop car ordering puzzle game,
packaged as a standard Java EE WAR file for Tomcat deployment.

## 📁 Project Structure

```
car-puzzle-app/
├── pom.xml                                  ← Maven build file (WAR packaging)
├── src/main/
│   ├── java/com/carpuzzle/servlet/
│   │   ├── GameServlet.java                 ← Main game logic
│   │   └── ScoreServlet.java                ← REST leaderboard API
│   └── webapp/
│       ├── WEB-INF/web.xml                  ← Servlet deployment descriptor
│       ├── index.jsp                         ← Redirect to /game
│       ├── game.jsp                          ← Main game UI (JSTL + HTML5)
│       ├── error.jsp                         ← 404/500 error page
│       ├── css/style.css                    ← Racing-theme stylesheet
│       └── js/
│           ├── drag.js                      ← HTML5 + touch drag-and-drop
│           └── fireworks.js                 ← Win celebration animation
```

## 🚀 Build the WAR File

**Prerequisites:** Java 11+, Maven 3.6+

```bash
cd car-puzzle-app
mvn clean package
```

Output: `target/car-puzzle.war`

## 🐱 Deploy to Tomcat

1. Copy `car-puzzle.war` to Tomcat's `webapps/` folder
2. Start Tomcat: `bin/startup.sh` (Linux/Mac) or `bin/startup.bat` (Windows)
3. Open browser: **http://localhost:8080/car-puzzle**

## 🧪 Run Locally (no Tomcat needed)

```bash
mvn tomcat7:run
# Open: http://localhost:8080/car-puzzle
```

## 🎮 Game Features

| Feature | Description |
|---------|-------------|
| **Drag & Drop** | HTML5 drag API + full touch support |
| **3 Game Modes** | Order by Speed ⚡ / Price 💰 / Name 🔤 |
| **Live Scoring** | Score based on moves taken |
| **Leaderboard** | REST API (`/api/score`) with auto-refresh |
| **Fireworks** | Canvas animation on correct answer 🎆 |
| **SVG Cars** | Inline SVG illustrations, unique colors per car |
| **Responsive** | Works on mobile & desktop |

## 🔧 API Endpoints

| Method | URL | Description |
|--------|-----|-------------|
| GET | `/game` | Load game page |
| POST | `/game?action=shuffle&mode=speed` | New game |
| POST | `/game?action=validate` | Check current order |
| GET | `/api/score` | Get top 10 scores (JSON) |
| POST | `/api/score` | Submit score (JSON body) |

## 📦 Tech Stack

- **Java 11** + **Servlet 4.0**
- **JSP** + **JSTL** templating
- **Jackson** for JSON (leaderboard API)
- **Maven** with `maven-war-plugin`
- Vanilla **HTML5 / CSS3 / JavaScript** (no frontend framework)
- Deployable on **Tomcat 9+**, **JBoss**, **WildFly**, **Jetty**
