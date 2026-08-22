-- liquibase formatted sql

-- changeset AnnS:1
CREATE INDEX student_name_index ON student (name);

-- changeset AnnS:2
CREATE INDEX idx_faculty_name_color ON faculty (name, color);

-- changeset AnnS:3
ALTER TABLE student ADD COLUMN avg_ball INTEGER;