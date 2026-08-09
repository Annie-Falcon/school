select s.name as name_student, s.age, f.name as name_faculty, f.color
  from student s
 inner join faculty f ON s.faculty_id = f.id
 order by f.name, s.age

select s.name as name_student, s.age
  from avatar a
  inner join student s on a.student_id = s.id
order by s.name