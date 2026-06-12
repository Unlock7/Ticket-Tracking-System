# **API Design Document — Ticket Tracking System**

# 1. Database diagram

Below is the ER diagram for the Ticket Tracking System:

![diagram](database-diagram.png)

**Relationships:**

- One **project** → many **tickets**
- Many **tickets** ↔ many **users** (via `ticket_assignees`)

# **2. Endpoint summary**

| Method   | Path                          | Description                      |
| -------- | ----------------------------- | -------------------------------- |
| **GET**  | /users                        | List all users                   |
| GET      | /users/{id}                   | Get user by ID                   |
| POST     | /users                        | Create a new user                |
| PUT      | /users/{id}                   | Update a user                    |
| DELETE   | /users/{id}                   | Delete a user                    |
| **GET**  | /projects                     | List projects with ticket counts |
| **POST** | /tickets                      | Create a new ticket              |
| GET      | /tickets                      | Search tickets                   |
| GET      | /tickets/{id}                 | Get ticket by ID                 |
| PUT      | /tickets/{id}                 | Update ticket                    |
| PUT      | /tickets/{id}/assign/{userId} | Assign user to ticket            |
| DELETE   | /tickets/{id}/assign/{userId} | Remove user from ticket          |

# **3. Endpoint description**

Below are the detailed descriptions for each endpoint.

## **POST /users**

Create a new user.

| Field             | Description                                             |
| ----------------- | ------------------------------------------------------- |
| **Endpoint**      | /users                                                  |
| **Method**        | POST                                                    |
| **Request body**  | `{ "name": "string", "email": "string" }`               |
| **Response body** | `{ "id": number, "name": "string", "email": "string" }` |
| **Validations**   | - name ≥ 3 chars - email valid - email unique           |

## **GET /users**

List all users.

| Field             | Description                                               |
| ----------------- | --------------------------------------------------------- |
| **Endpoint**      | /users                                                    |
| **Method**        | GET                                                       |
| **Request body**  | none                                                      |
| **Response body** | `[{ "id": number, "name": "string", "email": "string" }]` |
| **Validations**   | none                                                      |

## **GET /users/ {id}**

Retrieve a single user.

| Field             | Description                                             |
| ----------------- | ------------------------------------------------------- |
| **Endpoint**      | /users/{id}                                             |
| **Method**        | GET                                                     |
| **Response body** | `{ "id": number, "name": "string", "email": "string" }` |
| **Validations**   | - user must exist                                       |

## **PUT /users/ {id}**

Update a user.

| Field             | Description                                  |
| ----------------- | -------------------------------------------- |
| **Endpoint**      | /users/{id}                                  |
| **Method**        | PUT                                          |
| **Request body**  | `{ "name": "string", "email": "string" }`    |
| **Response body** | Updated user                                 |
| **Validations**   | - name ≥ 3 chars - email valid - user exists |

## **DELETE /users/ {id}**

Delete a user.

| Field             | Description                     |
| ----------------- | ------------------------------- |
| **Endpoint**      | /users/{id}                     |
| **Method**        | DELETE                          |
| **Response body** | `{ "message": "User deleted" }` |
| **Validations**   | - user must exist               |

## **GET /projects**

List all projects with ticket counts.

| Field             | Description                                                                                         |
| ----------------- | --------------------------------------------------------------------------------------------------- |
| **Endpoint**      | /projects                                                                                           |
| **Method**        | GET                                                                                                 |
| **Response body** | Example:`{ "id": 1, "name": "Website", "tickets": { "open": 10, "in_progress": 5, "closed": 30 } }` |
| **Validations**   | none                                                                                                |

## **POST /tickets**

Create a new ticket.

| Field             | Description                                                                             |
| ----------------- | --------------------------------------------------------------------------------------- |
| **Endpoint**      | /tickets                                                                                |
| **Method**        | POST                                                                                    |
| **Request body**  | `{ "title": "string", "description": "string", "projectId": number, "status": "open" }` |
| **Response body** | Created ticket                                                                          |
| **Validations**   | - title required - project exists - status valid                                        |

## **GET /tickets**

Search tickets.

| Field             | Description                |
| ----------------- | -------------------------- |
| **Endpoint**      | /tickets                   |
| **Method**        | GET                        |
| **Query params**  | `status`, `text`           |
| **Response body** | List of matching tickets   |
| **Validations**   | - status valid if provided |

## **GET /tickets/ {id}**

Retrieve a single ticket.

| Field             | Description         |
| ----------------- | ------------------- |
| **Endpoint**      | /tickets/{id}       |
| **Method**        | GET                 |
| **Response body** | Full ticket details |
| **Validations**   | - ticket must exist |

## **PUT /tickets/ {id}**

Update ticket fields.

| Field             | Description                                                                      |
| ----------------- | -------------------------------------------------------------------------------- |
| **Endpoint**      | /tickets/{id}                                                                    |
| **Method**        | PUT                                                                              |
| **Request body**  | `{ "title": "...", "description": "...", "status": "...", "projectId": number }` |
| **Response body** | Updated ticket                                                                   |
| **Validations**   | - status valid - project exists - ticket exists                                  |

## **PUT /tickets/ {id}/assign/ {userId}**

Assign a user to a ticket.

| Field             | Description                                               |
| ----------------- | --------------------------------------------------------- |
| **Endpoint**      | /tickets/{id}/assign/{userId}                             |
| **Method**        | PUT                                                       |
| **Response body** | Updated ticket with assigned users                        |
| **Validations**   | - ticket exists - user exists - user not already assigned |

## **DELETE /tickets/ {id}/assign/ {userId}**

Remove a user from a ticket.

| Field             | Description                                           |
| ----------------- | ----------------------------------------------------- |
| **Endpoint**      | /tickets/{id}/assign/{userId}                         |
| **Method**        | DELETE                                                |
| **Response body** | Updated ticket                                        |
| **Validations**   | - ticket exists - user exists - user must be assigned |

# **4. Email notifications**

**When is an email sent?**

- Whenever a ticket is updated (title, description, status, project, or assignees)

**Who receives it?**

- All users assigned to that ticket

**What does the email contain?**

- Ticket ID
- Updated fields
- Current status
- List of assignees

Example:

Code

```
Ticket #123 updated:
Title: "Fix login bug"
Status: "In progress"
Assignees: Max, Dave
```

**What happens if sending fails?**

- The ticket update still succeeds
- The failure is logged
- No retry is required for this assignment
