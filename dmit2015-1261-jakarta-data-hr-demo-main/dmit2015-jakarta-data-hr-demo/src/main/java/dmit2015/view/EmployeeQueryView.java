package dmit2015.view;


import dmit2015.entity.Department;
import dmit2015.entity.Employee;
import dmit2015.repository.HumanRepository;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;
import org.primefaces.component.message.Message;

import java.io.Serializable;
import java.util.List;

@Named("currentEmployeeQueryView")
@ViewScoped
public class EmployeeQueryView implements Serializable {

    @Inject

    private transient HumanRepository hrRepository;

    @Getter
    private List<Employee> queryResults;

    @Setter
    @Getter
    private Department selectedDepartment;


    public  List<Department> completeDepartment(String query){
        return hrRepository.departmentsBy("%"+ query + "%");
    }

    public void onSearchByDepartment(){

        try{
            queryResults = hrRepository.employeesByDepartmentId(selectedDepartment.getId());
            Messages.addGlobalInfo("Query returedn {0} results", queryResults.size());
        }
        catch (Exception ex){
            handleException(ex);

        }

    }

    public void onClear(){
        queryResults = null;
        selectedDepartment=null;
    }



    protected void handleException(Exception ex) {
        var details = new StringBuilder();
        Throwable causes = ex;

        while (causes.getCause() != null) {
            details.append(ex.getMessage());
            details.append(" Caused by:");
            details.append(causes.getCause().getMessage());
            causes = causes.getCause();
        }

        Messages.create(ex.getMessage())
                .detail(details.toString())
                .error()
                .add("errors");
    }

}
