# EV-ChargingPointsApp
Android app for locating electric vehicle charging points in Yorkshire. It Includes login/authentication, interactive map with city selection, charging point markers, and deep links to Google Maps


#**EV APP**
This  app is allow users to  log in, choose a city, view charging points on a Google Map, and open navigation via Google Maps. Includes admin/user roles and a local SQLite database with CSV import.

**Features**
	•	Splash screen and login/signup (email & password)
	•	User & Admin authentication 
	•	Map with city dropdown (Leeds, Bradford, Wakefield, etc.)
	•	Charging point markers with details
	•	Pop-up → deep link to Google Maps navigation
	•	SQLite database (DBHelper)
	•	CSV import for charging points (CSVHelper using OpenCSV)

**Setup**
	1.	Clone the repo:
 git clone https://github.com/MarahAQ/EV-ChargingPointsApp.git
 	2.	Open in Android Studio
	3.	Add your Google Maps API key in AndroidManifest.xml
	4.	Place sample_national_chargepoints.csv in app/src/main/res/raw/
	5.	Build & run


**Tech Stack**
	•	Java, Android Studio
	•	SQLite (SQLiteOpenHelper)
	•	Google Maps SDK
	•	OpenCSV

**Roadmap**
	•	Real-time charger availability
	•	Expand beyond Yorkshire
	•	Filters by connector type
