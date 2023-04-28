# O aplikacji:

Aplikacja budżetowa to REST API napisane w języku Java, która umożliwia użytkownikom zarządzanie swoimi budżetami. Aplikacja pozwala na dodawanie, usuwanie i aktualizowanie budżetów oraz wydatków, a także generowanie raportów z danymi dotyczącymi budżetów i wydatków dla konkretnego użytkownika.


# Wymagania:

#### Java 17+

#### Docker

#### Postman

# Instrukcja instalacji:
##### 1. Sklonuj repozytorium z aplikacją.
##### 2. Uruchom aplikację w środowisku programistycznym.
##### 3. Skonfiguruj plik application.yml, aby odpowiadał Twojemu środowisku.
##### 4. Otwórz plik docker-compose.yml aby stworzyć obraz dokerowy.
##### 5. Uruchom aplikację.


## Zabezpiecznie:
##### Aby w pełni korzystać z aplikacji, użytkownik musi utworzyć konto, aby to zrobić,należy wysłać odpowiednie żądanie HTTP na endpoint odpowiedzialny za rejestracje użytkowników. przykład:
#### Screanshot z Postmana:
![](https://github.com/RafalLup/financialResources-master/blob/master/Screenshots/1.png?raw=true)
###   Po stworzeniu użytkownika, możemy już w pełni korzystać z aplikacji, nie zapominając o dodaniu podanych przez nas wartości, dokładnie tak ja na zdjęciu poniżej:
![](https://github.com/RafalLup/financialResources-master/blob/master/Screenshots/2.png?raw=true)

#### Wszystie punkty końcowe:
##### Endpoint: POST localhost:8080/user <- Rejestracja użytkownika
Przykładowe ciało żądania:
```javascript
 {
"username": "rafal",
"password": "rafalek12345",<-(HASŁO MINIMUM 12 ZNAKÓW)|
"email": "janek123@example.com"|
}
```
##### Endpoint: POST localhost:8080/budgets <-Dodanie budżetu
Przykładowe ciało żądania:
```javascript
{
"title": "Mój budżet",
"limit": "33",
"typeOfBudget :"HALF",
"maxSingleExpense" : "333"
}
```
##### Endpoint: GET localhost:8080/budgets <-Pobranie wszystkich budżetów
##### Endpoint: GET localhost:8080/budgets/{id}/status <-Pobranie statusu budżetu
##### Endpoint: GET localhost:8080/budgets/{id} <-Pobranie budżetu po ID
##### Endpoint: PUT localhost:8080/budgets/{id}<- Aktualizacja budżetu
Przykładowe ciało żądania:
```javascript
{
"title": "Mój nowy budżet",
"limit": "34",
"typeOfBudget :"HALF",
"maxSingleExpense" : "334"
}
```
##### Endpoint: DELETE localhost:8080/budgets/{id} <-Usunięcie budżetu

##### Endpoint: POST localhost:8080/expenses <-Dodanie wydatku
Przykładowe ciało żądania:
```javascript
{
  "title" : " Title",
  "amount" : "34",
  "budgetId": "bccd93bb-6a0a-478a-9af5-235bc03f5935"
}
```
##### Endpoint: GET localhost:8080/expenses <-Pobranie listy wydatków:
##### Endpoint: GET localhost:8080/expenses/{id}<-Pobranie  wydatku po ID
##### Endpoint: PUT localhost:8080/expenses/{id} <-Aktualizacja wydatku
Przykładowe ciało żądania:
```javascript
{ 
  "title" : "update Title",
    "amount" : "35",
    "budgetId": "bccd93bb-6a0a-478a-9af5-235bc03f5935"
}
```
##### Endpoint: DELETE /expenses/{id} <-Usunięcie wydatku
