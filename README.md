# -MULTITHREADED-CHAT-APPLICATION
COMPANY: CODTECH IT SOLUTIONS

NAME: YASHKUMAR PATEL

INTERN ID: CT04DL1118

DOMAIN: JAVA PROGRAMMING

DURATIONS: 4 WEEKS

MENTOR: NEELA SANTOSH

DESCRIPTION: This Java console application is a simple weather checker that lets users find out the current weather conditions of any city by just typing its name. The project uses the Open-Meteo API to fetch real-time weather data including temperature, humidity, and wind speed. The goal behind building this project was to understand how to connect a Java program with real-world APIs, handle JSON data, and display live weather information in a clean, user-friendly way.

Here’s how the program works step by step:

When the application starts, it prompts the user to enter the name of a city. If the user types "No", the program simply exits. Otherwise, it takes the input city name and sends a request to the Open-Meteo Geocoding API. This API returns the latitude and longitude coordinates of the city. These coordinates are important because weather APIs work based on geographic coordinates rather than names.

Once we get the latitude and longitude, another API call is made — this time to the Open-Meteo Forecast API — using those coordinates. This API gives the current weather details such as temperature at 2 meters above ground level, relative humidity, and wind speed at 10 meters height. The app then displays all this information in a readable format.

The data flow is designed to be clean and modular. The code has separate methods to:

Make the API request,

Read and convert the API response into a string,

Parse the JSON data, and

Display the required values to the user.

We used Java’s HttpURLConnection class for making HTTP GET requests. For parsing the JSON response, we used the org.json.simple library, which is lightweight and easy to integrate. All responses from the API come in JSON format, which makes it easy to extract only the information we need.

There’s also basic error handling in place. If the API call fails (for example, if the city name is incorrect or the internet connection is not available), the program catches the error and prints a friendly message instead of crashing. This improves the overall user experience and makes the program more reliable.

The project may look simple, but it helped in learning a lot of useful skills such as:

How to work with REST APIs in Java,

How to parse JSON data,

How to handle user input safely,

How to structure code into reusable methods, and

How to manage exceptions and error cases properly.

This project can easily be expanded in the future. For example, we could build a GUI using JavaFX or Swing, add a 7-day forecast feature, or allow users to compare the weather of multiple cities at once. But even in its current form, it serves as a great example of using Java for something practical and real-time.

In summary, this project is not just about checking weather data — it’s about learning how to interact with the internet using Java, work with live data, and build useful tools. It’s a solid base for anyone looking to learn API integration and JSON handling in a beginner-friendly way.
