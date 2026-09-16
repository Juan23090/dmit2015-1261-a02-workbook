package views;


import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import org.omnifaces.cdi.ViewScoped;

import java.io.Serializable;

@Named
@ViewScoped
public class StudentFormBean implements Serializable {
    private int SubmissionCount;
    private String fullName;
    private String program;
    private boolean fullTime;

    public int getSubmissionCount() {
        return SubmissionCount;
    }

    public boolean isFullTime() {
        return fullTime;
    }

    public void setFullTime(boolean fullTime) {
        this.fullTime = fullTime;
    }

    public String getLastName() {
        return program;
    }

    public void setLastName(String lastName) {
        this.program = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void submit(){
        submissionCount ++;

        FacesMessage message = new FacesMessage(

                FacesMessage.SEVERITY_INFO,"Form Submitted: ",String.format("Welcome %s to %s program (%s)", fullName, program, fullTime ? "Full Time" : "Part Time")
        );
        FacesContext.getCurrentInstance().addMessage(null,message);

        fullName = null;
        program = null;
        fullTime= true;

    }
}
