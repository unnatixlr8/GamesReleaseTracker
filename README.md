# 🎮 Game Releases Tracker

A Spring Boot web application to track and export newly released games from PlayStation Store, Steam Store, and Nintendo eShop.

![Screenshot of Game Tracker](static/screenshot.png)

## 🌟 Features
- **Multi-Platform Support**: Fetch releases from PlayStation, Steam, and Nintendo stores
- **Export Functionality**: Save game lists as CSV with Unicode support
- **Modern UI**: Clean Bootstrap 5 interface with dark/light mode
- **Responsive Design**: Works across desktop and mobile devices
- **Simple Integration**: Self-contained Spring Boot application

## 🛠️ Tech Stack
**Backend**:
- Java 17+
- Spring Boot 3.x
- Spring Web MVC
- RestTemplate

**Frontend**:
- Thymeleaf templates
- Bootstrap 5
- Responsive CSS

## 🚀 Installation & Usage
### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Internet connection (for API access)

### Running the Application
```bash
git clone https://github.com/yourusername/game-releases-tracker.git
cd game-releases-tracker
./mvnw spring-boot:run
