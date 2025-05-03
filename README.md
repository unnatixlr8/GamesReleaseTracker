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
git clone https://github.com/unnatixlr8/GamesReleaseTracker.git
cd GamesReleaseTracker
./mvnw spring-boot:run

run at localhost:8080

```
## 🔌 API Integration
The application integrates with three major gaming platforms:

### PlayStation Store  
Uses GraphQL endpoint to fetch new releases

### Steam Store  
Connects to Steam's Featured Categories API

### Nintendo eShop  
Uses EU Search API with proper headers

## 📊 Data Export
Exported CSV files contain:
- Game title (Unicode supported)
- Store URL  
- Release date (when available)

**Sample output** (`nintendo-2023-11-15.csv`):
```csv
"Hotel Business Simulator","https://www.nintendo.com/store/hotel-business-simulator](https://www.nintendo.com/en-gb/Games/Nintendo-Switch-download-software/Hotel-Business-Simulator-2813379.html"
"Zelda: Tears of the Kingdom","https://www.nintendo.com/store/zelda-tears-kingdom](https://www.nintendo.com/en-gb/Games/Nintendo-Switch-2-Edition/The-Legend-of-Zelda-Tears-of-the-Kingdom-Nintendo-Switch-2-Edition-2787249.html"
```

## 🙏 Acknowledgments

### Game Platform Providers
- [Sony Interactive Entertainment](https://www.playstation.com) (PlayStation)
- [Valve Corporation](https://store.steampowered.com) (Steam)  
- [Nintendo of Europe](https://www.nintendo.com) (Nintendo eShop)

### Open Source Technologies
- [Bootstrap](https://getbootstrap.com) - Frontend framework
- [Spring](https://spring.io) - Backend framework  
- [Thymeleaf](https://www.thymeleaf.org) - Templating engine

---

> ℹ️ **Disclaimer**  
> This is an independent, unofficial project not endorsed by or affiliated with any game platform. All game titles, trademarks, and associated content are property of their respective owners. Data is collected from publicly available APIs for educational purposes only.
