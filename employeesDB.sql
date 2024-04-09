CREATE TABLE genders (
    id SERIAL PRIMARY KEY,
    gender VARCHAR(10) NOT NULL
);
CREATE TABLE positions (
    id SERIAL PRIMARY KEY,
    position VARCHAR(40) NOT NULL
);
CREATE TABLE employees (
    id SERIAL PRIMARY KEY,
    surname VARCHAR(80) NOT NULL,
    name VARCHAR(80) NOT NULL,
    patronymic VARCHAR(80) NOT NULL,
    birthday DATE NOT NULL,
    gender_id INTEGER,
    employment_day DATE NOT NULL,
    hours_worked INTEGER NOT NULL,
    position_id INTEGER,
    bank_accound_number INTEGER NOT NULL,
    FOREIGN KEY(gender_id) REFERENCES genders(id),
    FOREIGN KEY(position_id) REFERENCES positions(id)
);

ALTER TABLE employees
ADD CONSTRAINT positive_hours_worked_check
CHECK (hours_worked >= 0)
