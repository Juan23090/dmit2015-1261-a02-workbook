package views;


import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import model.StudentInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
public class StudentListSession  implements Serializable{

    private List<StudentInfo> students = new ArrayList<>();

    public List<StudentInfo> getStudents(){
        return students;
    }

    public void add(StudentInfo newStudentInfo){
        students.add(newStudentInfo);
    }

    public void remove(StudentInfo existingStudentInfo)

    {
        students.remove(existingStudentInfo);
    }
}

