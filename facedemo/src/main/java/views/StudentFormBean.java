package views;


import model.StudentInfo;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.omnifaces.cdi.ViewScoped;

import java.io.Serializable;

@Named
@ViewScoped
public class StudentFormBean  implements Serializable {

    private int submissionCount;
    public int getSubmissionCount() {
        return submissionCount;
    }


    @Inject
    private StudentListSession studentListSession;


    private StudentInfo studentInfo = new StudentInfo();


    public StudentInfo getStudentInfo() {
        return studentInfo;
    }

    public void setStudentInfo(StudentInfo studentInfo) {
        this.studentInfo = studentInfo;
    }

    public void submit(){
        submissionCount ++;

        studentListSession.add(studentInfo);

        FacesMessage message = new FacesMessage(
                FacesMessage.SEVERITY_INFO, "From Submitted: " ,
                String.format( "Welcome %s to %s program (%s)", studentInfo.getFullName(),studentInfo.getProgram(), studentInfo.isFullTime() ? "Full Time" : "Part Time" )

        );

        FacesContext.getCurrentInstance().addMessage(null,message);

        studentInfo =new StudentInfo();



    }

}