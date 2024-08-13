<?php
// database_connection.php
$servername = "localhost";
$username = "your_username";
$password = "your_password";
$dbname = "nordic_aurora";

$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
?>

<?php
// aurora_forecast.php
require_once 'database_connection.php';

// Execute Java application and get results
$output = shell_exec('java -jar NordicAuroraForecast.jar');
$predictions = json_decode($output, true);

// Store predictions in the database
foreach ($predictions as $prediction) {
    $location_id = $prediction['location_id'];
    $date = $prediction['date'];
    $probability = $prediction['probability'];

    $sql = "INSERT INTO predictions (location_id, prediction_date, aurora_probability) 
            VALUES ($location_id, '$date', $probability)";
    $conn->query($sql);
}

// Fetch and display results
$sql = "SELECT l.name, p.prediction_date, p.aurora_probability 
        FROM predictions p 
        JOIN locations l ON p.location_id = l.id 
        ORDER BY p.prediction_date, p.aurora_probability DESC";
$result = $conn->query($sql);

while ($row = $result->fetch_assoc()) {
    echo "Location: " . $row["name"] . "<br>";
    echo "Date: " . $row["prediction_date"] . "<br>";
    echo "Aurora Probability: " . $row["aurora_probability"] . "%<br><br>";
}

$conn->close();
?>