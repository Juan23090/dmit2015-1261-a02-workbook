package views;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
@Named
@RequestScoped

public class HelloBean {
    private String userInput;

    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    public String getMessage(){
        return "'Hello, " + userInput;
    }

    public String onSubmit() {

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("FacesMessage from java part" + userInput));
        userInput = null;
        return null;
    }
}
