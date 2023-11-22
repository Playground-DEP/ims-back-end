CREATE TABLE IF NOT EXISTS teacher_course(
    teacher_id INT NOT NULL ,
    course_id INT NOT NULL ,
    CONSTRAINT PRIMARY KEY (teacher_id,course_id),
    CONSTRAINT fk_1 FOREIGN KEY (teacher_id) REFERENCES teacher(id),
    CONSTRAINT fk_2 FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE
);