CREATE TABLE skill (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    category VARCHAR(100)
);

CREATE TABLE employee_skill (
    employee_id INTEGER REFERENCES employee(id) ON DELETE CASCADE,
    skill_id INTEGER REFERENCES skill(id) ON DELETE CASCADE,
    proficiency VARCHAR(16),
    is_primary BOOLEAN,
    PRIMARY KEY (employee_id, skill_id)
);