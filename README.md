# Comics store project
Project given as an exam for online Java courses by TOP Academy.
The goal is to combine 4 variants given by an academy and simultaneously fulfil requirement for each one.

## Combined task
Create console application **Comics store**.
It has to contain:
- information about current assortment of comics in the store;
- documents, containing descriptions for each comic;
- employees data and company structure;
- management of finances;

## According to the requirements above, this project has four distinct parts:
- comics
- employees
- finances
- store

#### Comics
Properties:
- name;
- author;
- genre;
- release date;
- description as a text document class

Text document properties:
- author;
- creation date;
- file size;

Documents has to allow:
- opening and writing output page by page;
- searching in the document for a string;
- editing string in the document;
- creation of new document;

#### Employees
Properties:
- surname and name;
- birth date
- gender;
- phone number;
- position;
- date of employment;
- salary;

System has to allow:
- to hire and fire employees, and change information about them;
- searching by name or position;
- create *documents* work reports;

#### Finances
Properties: 
- income;
- spendings;
- list of possible salaries;
- list of cards
- card class containing information of its owner, card number

System has to allow:
- transact salaries to according cards
- change card info
- create *documents* for salary reports;

#### Store
Properties:
- name;
- location;
- establishment date;
- list of comics;
- total sales;

System has to allow:
- to add comics;
- to remove comics;
- to edit comics info;
- to create sales;
- to write off comics;
- searching for comics by name, author or genre;
- listing of available, new and best comics;
- listing of best genres of the week, month or year;
- creaton of *documents* for each listing category;

## Connections
Finances do not have access to Employees information.
Employees system calls Finance System to give salary for each Employee.
Store has information about every employee.
Store contains comics info.


---
that's it for now :P
