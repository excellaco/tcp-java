INSERT INTO skill_category (name) VALUES ('Agile'), ('Business'), ('Dev Ops'), ('Management'), ('Software');

INSERT INTO skill (name, category_id) VALUES ('Scrum Master', 1), ('Agile Coach', 1), ('Certified Scrum Instructor', 1);
INSERT INTO skill (name, category_id) VALUES ('Business Analysis', 2), ('Business Development', 2), ('Marketing', 2), ('Recruiting', 2);
INSERT INTO skill (name, category_id) VALUES ('Jenkins', 3), ('Heroku', 3), ('AWS', 3), ('ECS (AWS)', 3), ('Kubernetes', 3), ('CircleCI', 3);
INSERT INTO skill (name, category_id) VALUES ('Supervisor', 4), ('Engagement Management', 4), ('Project Management', 4), ('Relationship Management', 4);
INSERT INTO skill (name, category_id) VALUES ('Javascript', 5), ('Angular (JS)', 5), ('React (JS)', 5), ('Java', 5), ('Ruby', 5), ('.Net', 5), ('C', 5);

INSERT INTO employee (first_name, middle_name, last_name, birth_date, gender, ethnicity, us_citizen, email, phone_number, line1, line2, city, state_code, zip_code) VALUES
    ('John', 'T', 'Winchester', TIMESTAMP '1980-05-13', 'MALE', 'CAUCASIAN', true, 'john@winchester.com', '(123)444-5555', '2300 Wilson Blvd', null, 'Arlington', 'VA', '22201'),
    ('Dean', 'L', 'Winchester', TIMESTAMP '1980-05-13', 'MALE', 'CAUCASIAN', true, 'dean@winchester.com', '(123)444-5555', '2300 Wilson Blvd', null, 'Arlington', 'VA', '22201'),
    ('Sam', 'D', 'Winchester', TIMESTAMP '1980-05-13', 'MALE', 'CAUCASIAN', true, 'sam@winchester.com', '(123)444-5555', '2300 Wilson Blvd', null, 'Arlington', 'VA', '22201'),
    ('Louis', 'C', 'Armstrong', TIMESTAMP '1980-05-13', 'MALE', 'BLACK', true, 'king_louis@gmail.com', '(123)444-5555', '2300 Wilson Blvd', null, 'Arlington', 'VA', '22201'),
    ('Duke', 'T', 'Ellington', TIMESTAMP '1980-05-13', 'MALE', 'BLACK', true, 'the_duke@yahoo.com', '(123)444-5555', '2300 Wilson Blvd', null, 'Arlington', 'VA', '22201'),
    ('Ella', 'L', 'Fitzgerald', TIMESTAMP '1980-05-13', 'FEMALE', 'BLACK', true, 'ella.fitzgerald@gmail.com', '(123)444-5555', '2300 Wilson Blvd', null, 'Arlington', 'VA', '22201'),
    ('John', 'T', 'Glenn', TIMESTAMP '1980-05-13', 'MALE', 'CAUCASIAN', true, 'john.glenn@nasa.gov', '(123)444-5555', '2300 Wilson Blvd', null, 'Arlington', 'VA', '22201'),
    ('Neil', 'A', 'Armstrong', TIMESTAMP '1980-05-13', 'MALE', 'CAUCASIAN', true, 'niel.armstrong@nasa.gov', '(123)444-5555', '2300 Wilson Blvd', null, 'Arlington', 'VA', '22201'),
    ('Buzz', null, 'Aldrin', TIMESTAMP '1980-05-13', 'MALE', 'CAUCASIAN', true, 'buzz.aldrin@nasa.gov', '(123)444-5555', '2300 Wilson Blvd', null, 'Arlington', 'VA', '22201'),
    ('Michael', null, 'Collins', TIMESTAMP '1980-05-13', 'MALE', 'CAUCASIAN', true, 'michael.collins@nasa.gov', '(123)444-5555', '2300 Wilson Blvd', null, 'Arlington', 'VA', '22201'),
    ('Andy', null, 'Dwyer', TIMESTAMP '1980-05-13', 'MALE', 'CAUCASIAN', true, 'andy.dwyer@in.parks.gov', '(123)444-5555', '2300 Wilson Blvd', null, 'Arlington', 'VA', '22201'),
    ('Tom', null, 'Haverford', TIMESTAMP '1980-05-13', 'MALE', 'CAUCASIAN', true, 'tom@entertainment420.com', '(123)444-5555', '2300 Wilson Blvd', null, 'Arlington', 'VA', '22201'),
    ('Anne', 'S', 'Perkins', TIMESTAMP '1980-05-13', 'FEMALE', 'CAUCASIAN', true, 'anne.perkins@in.parks.gov', '(123)444-5555', '2300 Wilson Blvd', null, 'Arlington', 'VA', '22201'),
    ('Leslie', 'A', 'Knope', TIMESTAMP '1980-05-13', 'FEMALE', 'CAUCASIAN', true, 'leslie.knope@in.parks.gov', '(123)444-5555', '2300 Wilson Blvd', null, 'Arlington', 'VA', '22201'),
    ('Ron', 'J', 'Swanson', TIMESTAMP '1980-05-13', 'MALE', 'CAUCASIAN', true, 'ron.swanson@in.parks.gov', '(123)444-5555', '2300 Wilson Blvd', null, 'Arlington', 'VA', '22201');

INSERT INTO employee_skill (employee_id, skill_id, proficiency, is_primary) VALUES
    (1, 19, 'HIGH', true), (1, 21, 'MID', false), (1, 1, 'MID', false), (1, 4, 'LOW', false), (1, 8, 'MID', false), (1, 9, 'LOW', false), (1, 16, 'MID', false), (1, 17, 'LOW', false),
    (2, 18, 'MID', true), (2, 21, 'MID', false), (2, 1, 'MID', false), (2, 4, 'LOW', false), (2, 9, 'LOW', false), (2, 17, 'LOW', false),
    (3, 20, 'MID', true), (3, 21, 'MID', false), (3, 1, 'MID', false), (3, 4, 'LOW', false), (3, 8, 'MID', false), (3, 17, 'LOW', false),
    (4, 17, 'HIGH', true), (4, 1, 'MID', false), (4, 14, 'MID', false), (4, 15, 'MID', false), (4, 6, 'MID', false),
    (5, 15, 'HIGH', true), (5, 1, 'MID', false), (5, 14, 'MID', false), (5, 17, 'MID', false),
    (6, 14, 'HIGH', true), (6, 1, 'MID', false), (6, 15, 'MID', false), (6, 17, 'MID', false),
    (7, 10, 'HIGH', true), (7, 12, 'LOW', false), (7, 13, 'MID', false), (7, 9, 'LOW', false), (7, 14, 'MID', false), (7, 16, 'LOW', false),
    (8, 12, 'HIGH', true), (8, 10, 'LOW', false), (8, 13, 'MID', false), (8, 9, 'LOW', false), (8, 14, 'MID', false), (8, 17, 'LOW', false),
    (9, 8, 'HIGH', true), (9, 10, 'MID', false), (9, 12, 'MID', false), (9, 17, 'LOW', false),
    (10, 8, 'HIGH', true), (10, 10, 'MID', false), (10, 12, 'MID', false), (10, 14, 'MID', false),
    (11, 7, 'MID', true), (11, 5, 'MID', false), (11, 6, 'MID', false), (11, 4, 'LOW', false), (11, 17, 'MID', false), (11, 15, 'LOW', false),
    (12, 17, 'MID', true), (12, 5, 'MID', false), (12, 6, 'MID', false), (12, 4, 'MID', false), (12, 7, 'MID', false), (12, 15, 'LOW', false),
    (13, 14, 'MID', true), (13, 4, 'MID', false), (13, 17, 'MID', false), (13, 15, 'LOW', false),
    (14, 14, 'HIGH', true), (14, 5, 'MID', false), (14, 7, 'MID', false), (14, 4, 'MID', false), (14, 17, 'MID', false), (14, 16, 'LOW', false),
    (15, 16, 'HIGH', true), (15, 5, 'MID', false), (15, 6, 'MID', false), (15, 4, 'LOW', false), (15, 17, 'MID', false), (15, 15, 'LOW', false);

