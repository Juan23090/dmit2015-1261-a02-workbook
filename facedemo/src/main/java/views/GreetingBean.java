package views;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@Named
@RequestScoped
public class GreetingBean {

    private String firstName;
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName (String lastName){
        this.lastName = lastName;
    }
    public String getGreetingMessage() {

        if (firstName == null || firstName.isBlank()
        || lastName == null || lastName.isBlank()){
            return "";
        }

        return "Welcome " + firstName +" "+ lastName+ " to DMIT2015!";
    }
}