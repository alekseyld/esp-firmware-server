// Изменить пароль
#define IS_TEST true
#define DISABLE_FIREBASE true

#define WIFI_SSID ""
#define WIFI_PASSWORD ""

#define HTTP_UPDATE_URL "http://:8080/firmware"

// Set these to run example.
#define FIREBASE_HOST ""
#define FIREBASE_AUTH ""

#include "FirebaseESP8266.h"
#include <ESP8266WiFi.h>
#include <DallasTemperature.h>
#include <ESP8266httpUpdate.h>

#define D2 4
// Все датчики температуры подключены к порту D2 на ESP8266
#define ONE_WIRE_BUS D2

OneWire oneWire(ONE_WIRE_BUS);
DallasTemperature sensors(&oneWire);

// Нужно добавить все данные 
uint8_t sensor1[8] = { 0x28, 0xFF, 0xD8, 0xE2, 0x83, 0x16, 0x03, 0x44 };
uint8_t sensor2[8] = { 0x28, 0xFF, 0x7E, 0xF8, 0x83, 0x16, 0x03, 0xC8 };
uint8_t sensor3[8] = { 0x28, 0xFF, 0x29, 0x2F, 0x85, 0x16, 0x05, 0x87 }; 
uint8_t sensor4[8] = { 0x28, 0xFF, 0x29, 0x2F, 0x85, 0x16, 0x05, 0x87 }; //Изменить
uint8_t sensor5[8] = { 0x28, 0xFF, 0x29, 0x2F, 0x85, 0x16, 0x05, 0x87 }; //Изменить

FirebaseData firebaseData;

void setup() {
  Serial.begin(115200);
  Serial.println("Booting"); 
  
  WiFi.mode(WIFI_STA);
  
  // connect to wifi.
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  
  while (WiFi.waitForConnectResult() != WL_CONNECTED) {
    Serial.println("Connection Failed! Rebooting...");
    delay(5000);
    ESP.restart();
  }
  
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());
  
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  delay(1000);
}

void httpUpdate() {
  t_httpUpdate_return ret = ESPhttpUpdate.update(HTTP_UPDATE_URL);
  
  switch(ret) {
    case HTTP_UPDATE_FAILED:
      Serial.println(ESPhttpUpdate.getLastErrorString().c_str());
      Serial.println("[update] Update failed.");
      break;
    case HTTP_UPDATE_NO_UPDATES:
      Serial.println("[update] Update no Update.");
      break;
    case HTTP_UPDATE_OK:
      Serial.println("[update] Update ok."); // may not called we reboot the ESP
      break;
  }
}

void sendTemp(int id, float temp) {
  String json = "{\"id\":\""+ String(id) +"\", \"temp\":\"" + String(temp, 2) +"\", \"time\": {\".sv\":\"timestamp\"}}";
  Firebase.pushJSON(firebaseData, "temps", json);
}

void getAndSendTemp(int id, uint8_t sensor[]) {
  float temp = -127.0;
  int timeout = 0;
  
  do {
    sensors.requestTemperaturesByAddress(sensor);
    
    temp = sensors.getTempC(sensor);
    timeout++;
    
    delay(75);
    
  } while (temp == -127.0 && timeout < 100);

  #if DISABLE_FIREBASE
    Serial.print(id);
    Serial.print(" - ");
    Serial.println(temp);

  #else
    sendTemp(id, temp);
}

void loop() {
  httpUpdate();
  
  sensors.requestTemperatures();

  getAndSendTemp(1, sensor1);
  getAndSendTemp(2, sensor2);
  getAndSendTemp(3, sensor3);
  getAndSendTemp(4, sensor4);
  getAndSendTemp(5, sensor5);

  #if IS_TEST
    delay(3000);

  #else
    ESP.deepSleep(30 * 60 * 1000000);
}
