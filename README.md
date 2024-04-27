# Examination project
Simple terminal application, which emulates management system for Comics store.

It provides basic means for managing comics in storage and employees.
Also the main task of given variant was implemented, the goal of which was to make a system for managing special text files treated as documents.

## Document System
Document system consists of two classes. The main one *Document.java* allows actual interaction with documents and *DocumentAPI.java* which controls output in the terminal.

## Employee System
Employee system keeps track of each employee and saves its data in .txt files. It support multimple types of Employees, each having its own class inherited from abstract one `Employee.java`.
System allows to:
- list employees by name;
- adding new employees to data bank;
- remove employees from data bank;
- calculate employees 
