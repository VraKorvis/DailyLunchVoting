[![Codacy Badge](https://app.codacy.com/project/badge/Grade/e273c144ac1e46378d1974361397501d)](https://app.codacy.com/gh/VraKorvis/DailyLunchVoting/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)

# DailyLunchVoting
Design and implement a REST API using Spring-Boot/Spring Data JPA **without frontend**.

The task is:

Build a voting system for deciding where to have lunch.

 * 2 types of users: admin and regular users
 * Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
 * Menu changes each day (admins do the updates)
 * Users can vote for a restaurant they want to have lunch at today
 * Only one vote counted per user
 * If user votes again the same day:
    - If it is before 11:00 we assume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides a new menu each day.

-----------------------------
## 📘 Swagger UI
### API доступен через [Swagger UI](http://localhost:8080/swagger-ui/index.html)

### 👤 User Profile Endpoints (`/api/profile`)
Endpoints to manage your user profile:

- 🔍 **Get user profile** — Retrieve current user profile information.
- ✏️ **Update user profile** — Update name, email, or password.
- ✍️ **Register user** — Create a new user account.

🔍 Get user profile
```bash
curl -u user1@gmail.com:password -X GET 'http://localhost:8080/api/profile' -H 'accept: application/json'
```
  
✏️ Update user profile
```bash 
curl -u user1@gmail.com:password -X PUT 'http://localhost:8080/api/profile' -H 'accept: */*' -H 'Content-Type: application/json' -d '{"name":"Updated User","email":"user1@gmail.com","password":"password"}'
```
✍️ Register user
```bash
curl -X POST 'http://localhost:8080/api/profile' -H 'accept: application/json' -H 'Content-Type: application/json' -d '{"name":"user3","email":"user3@gmail.com","password":"password"}'
```
---
### 👤 Admin: User Management APIs (`/api/admin/`)
Endpoints for admins to manage users:
- 🔍 **Get user by ID** — **Retrieve details of a user by their ID.**
- ✏️ **Edit user by ID** — **Update user information, roles, and status.**
- ✏️ **Enable/disable user by ID** — **Activate or deactivate a user account.**
- 🔍 **Get all users** — **List all users in the system.**
- ➕ **Add new user** — **Create a new user account.**
- 🔍 **Get users with pagination** — **Retrieve users in paginated form.**
- 🔍 **Get user by email** — **Find a user by their email address.**
- 
```bash
curl -u admin@gmail.com:admin -X GET 'http://localhost:8080/api/admin/users/100000' -H 'accept: application/json'
```
✏️ Edit user by id
```bash
curl -u admin@gmail.com:admin -X PUT 'http://localhost:8080/api/admin/users/100000' -H 'accept: */*' -H 'Content-Type: application/json' -d '{"id":100000,"name":"Updated User by Admin","email":"user1@gmail.com","password":"password","enabled":true,"roles":["USER"]}'
```
✏️ Enable/disable user by id
```bash
curl -u admin@gmail.com:admin -X PATCH 'http://localhost:8080/api/admin/users/100001?enabled=false' -H 'accept: */*'
```
🔍 Get all users
```bash
curl -u admin@gmail.com:admin -X GET 'http://localhost:8080/api/admin/users' -H 'accept: application/json'
```
➕ Add user
```bash
curl -u admin@gmail.com:admin -X POST 'http://localhost:8080/api/admin/users' -H 'accept: application/json' -H 'Content-Type: application/json' -d '{"name":"user4","email":"user4@gmail.com","password":"password","enabled":true,"roles":["USER"]}'
```
🔍 Get users with pagination
```bash
curl -u admin@gmail.com:admin -X GET 'http://localhost:8080/api/admin/users/page?page=0&size=5' -H 'accept: application/json' 
```
🔍 Get user by email
```bash
curl -u admin@gmail.com:admin -X GET 'http://localhost:8080/api/admin/users/by-email?email=user1%40gmail.com' -H 'accept: application/json' 
```
---
### 🍽️ Admin: Manage Restaurants & Daily Menus
🔍 Get all restaurants (without menu)
- 🔍 **Get all restaurants (without menu)** — Retrieve the list of all restaurants without their menus.
- ➕ **Create a new restaurant** — Add a new restaurant by providing its name.
- 🔍 **Get restaurant by ID** — Retrieve details of a restaurant by its ID.
- 🔍 **Get restaurants without assigned menu for today** — List restaurants without a menu assigned for today.
- 🔍 **Get restaurants without assigned menu for a specific date** — List restaurants without a menu assigned for the given date.
- ➕ **Create menu for a restaurant by ID** — Add a daily menu for a restaurant; only allowed for today or tomorrow before voting start time.
- ✏️ **Edit menu for a restaurant by ID** — Update an existing menu; allowed only for tomorrow or today before voting start time.
  
🔍 Get all restaurants (without menu)
```bash
curl -u admin@gmail.com:admin -X GET 'http://localhost:8080/api/admin/restaurants' -H 'accept: application/json'
```
➕ Create a new restaurant
```bash
curl -u admin@gmail.com:admin -X 'POST' \
  'http://localhost:8080/api/admin/restaurants' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "New Restaurant"
}'
```
🔍 Get restaurant by id
```bash
curl -u admin@gmail.com:admin -X GET 'http://localhost:8080/api/admin/restaurants/100004' -H 'accept: application/json'
```
🔍 Get restaurants without assigned menu for date
```bash
curl -u admin@gmail.com:admin -X 'GET' 'http://localhost:8080/api/admin/restaurants/without-assigned-menu/today' -H 'accept: application/json'
```
🔍 Get restaurants without assigned menu for date=2025-03-20
```bash
curl -u admin@gmail.com:admin -X GET "http://localhost:8080/api/admin/restaurants/without-assigned-menu?date=2025-03-20" -H "accept: application/json"
```
➕ Create menu for restaurant by id

📌 **Note:** Menu can be created only for tomorrow or today before `VOTING_START_TIME` (`06:00` by default); 
creation fails if a menu already exists.

📌 **Note:** Replace "2025-05-19" with tomorrow's date in YYYY-MM-DD format before running the command.
```bash
curl -u admin@gmail.com:admin -X POST 'http://localhost:8080/api/admin/restaurants/100004/menu' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
    "menuDate": "2025-05-19",
    "pricedMenuItemTos": [
      {
        "id": 100009,
        "price": 100
      },
      {
        "id": 100014,
        "price": 200
      }
    ]
  }'

```
✏️ Edit menu for restaurant by id

📌 **Note:** Menu can only be edited if previously assigned.  
Editing is allowed **only for tomorrow** or **today before `VOTING_START_TIME`**  
(**currently set to `06:00`** in the backend configuration).

```bash
curl -u admin@gmail.com:admin -X 'PUT' \
  'http://localhost:8080/api/admin/restaurants/100004/menu' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "menuDate": "2025-05-19",
  "pricedMenuItemTos": [
    {
      "id": 100016,
      "price": 7.71
    }
  ]
}'
```
---
### 🍲Admin: Menu Item Catalog

- 🔍 **Get menu item by id** — Retrieve a menu item by its ID.
- ✏️ **Edit menu item by id** — Update the name or details of a menu item by its ID.
- ➕ **Add menu item** — Create a new menu item in the catalog.
- 🔍 **Get all menu items** — List all available menu items.
- 🔍 **Get paginated menu items** — Retrieve menu items in paginated form.

🔍 Get menu item by id
```bash
curl -u admin@gmail.com:admin -X GET 'http://localhost:8080/api/admin/menu-item-catalog/100014' -H 'accept: application/json'
```
✏️ Edit menu item by id
```bash
curl -u admin@gmail.com:admin -X 'PUT' \
  'http://localhost:8080/api/admin/menu-item-catalog/100007' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "id": 100007,
  "name": "NewBurger"
}'
```
➕ Add menu item
```bash
curl -u admin@gmail.com:admin -X 'POST' \
  'http://localhost:8080/api/admin/menu-item-catalog' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "id": null,
  "name": "Mulled Wine"
}'
```
🔍 Get all menu items
```bash
curl -u admin@gmail.com:admin -X GET 'http://localhost:8080/api/admin/menu-item-catalog' -H 'accept: application/json'
```
🔍 Get paginated menu items
```bash
curl -u admin@gmail.com:admin -X GET 'http://localhost:8080/api/admin/menu-item-catalog/page?page=0&size=2' -H 'accept: application/json'
```
---
### 🗳️Vote — User Voting Actions
- 🔍 **Get all votes of the current user** — Retrieve all votes submitted by the current user.
- ➕ **Submit or change a vote for a restaurant (before deadline)** — Cast or update your vote for a restaurant before the voting deadline.
- 🔍 **Get current user's vote for today** — Retrieve the vote submitted by the current user for today's menu.

🔍 Get all votes of the current user
```bash
curl -u user1@gmail.com:password -X GET 'http://localhost:8080/api/votes' -H 'accept: application/json'
```
➕ Submit or change a vote for a restaurant (before deadline)

📌 Note: You can submit a new vote or change your existing vote for the given restaurant until the voting deadline configured as VOTING_END_TIME on the backend (11:00).
```bash
curl -u user1@gmail.com:password -X POST 'http://localhost:8080/api/votes?restaurantId=100006' -H 'accept: application/json' -d ''
```
🔍 Get current user's vote for today
```bash
curl -u user1@gmail.com:password -X GET 'http://localhost:8080/api/votes/for-today/me' -H 'accept: application/json'
```
---
### 🗳️🍽️Available Restaurants for Voting (public)
- 🔍 **Get all restaurants with today's assigned menu (for voting)** — Retrieve all restaurants that have menus assigned for today and are available for voting.
- 📊 **Get all users' votes for today** — Retrieve the votes cast by all users for today's menus.
- 📄 **Get a paginated list of all users' votes for today** — Retrieve a paginated list of votes from all users for today.

🔍 Get all restaurants with today's assigned menu (for voting)
```bash
curl -u user1@gmail.com:password -X GET "http://localhost:8080/api/restaurants/with-assigned-menu" -H "accept: application/json"
```
---
### 🗳️📊"Admin: Votes overview”
 📊 Get all users' votes for today
```bash
curl -u admin@gmail.com:admin -X GET "http://localhost:8080/api/admin/votes/for-today" -H "accept: application/json"
```
📄 Get a paginated list of all users' votes for today
```bash
curl -u admin@gmail.com:admin -X GET "http://localhost:8080/api/admin/votes/for-today/page?page=0&size=2" -H "accept: application/json"
```
