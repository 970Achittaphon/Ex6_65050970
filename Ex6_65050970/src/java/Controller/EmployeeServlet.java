package Controller;

import Model.Employee;
import Model.EmployeeTable;
import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "EmployeeServlet", urlPatterns = {"/employee"}) 
public class EmployeeServlet extends HttpServlet {

    private static final String LIST_PAGE = "listEmployee.jsp";
    private static final String ADD_PAGE = "addEmployee.jsp";
    private static final String EDIT_PAGE = "editEmployee.jsp";
    private static final String ERROR_PAGE = "errorPage.jsp"; 

    /**
     * Handles HTTP GET requests (List, Add Form, Edit Form, Delete).
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) {
            action = "list"; 
        }
        
        String targetPage = LIST_PAGE; 

        try {
            switch (action) {
                case "addForm":
                    targetPage = ADD_PAGE; 
                    break;

                case "editForm":
                    String id = request.getParameter("id");
                    Employee employee = EmployeeTable.findEmployeeById(id);
                    request.setAttribute("employee", employee); 
                    targetPage = EDIT_PAGE; 
                    break;

                case "delete":
                    String deleteId = request.getParameter("id");
                    // Note: If delete fails due to lock, the result is ignored here, 
                    // but the "list" action below will ensure the user sees the final state.
                    EmployeeTable.deleteEmployee(deleteId); 
                    // Fall-through is intended here to re-list the employees
                                
                case "list":
                default:
                    List<Employee> employeeList = EmployeeTable.findAllEmployee();
                    request.setAttribute("employeeList", employeeList);
                    targetPage = LIST_PAGE; 
                    break;
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            targetPage = ERROR_PAGE;
        }

        RequestDispatcher rd = request.getRequestDispatcher(targetPage);
        rd.forward(request, response);
    }

    /**
     * Handles HTTP POST requests (Add Submit, Edit Submit).
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        double salary = 0.0; // Initialize to avoid compile error

        Employee employee = null;
        int result = 0;
        String targetPage = LIST_PAGE;
        String errorMessage = null; // Variable to hold the error message

        try {
            // Validate and Parse Input
            salary = Double.parseDouble(request.getParameter("salary"));
            employee = new Employee(id, name, salary);
            
            switch (action) {
                case "addSubmit":
                    result = EmployeeTable.insertEmployee(employee); 
                    if (result == 0) {
                        // result 0 in Insert means: 1. Duplicate ID OR 2. Locked
                        if (EmployeeTable.findEmployeeById(id) != null) {
                             // If ID is found in the DB, it's a Duplicate ID error
                             errorMessage = "Failed to Add: Employee ID " + id + " already exists.";
                        } else {
                            // If ID is NOT found, it means the DML failed due to LOCK
                            errorMessage = "Wait, Update Database in Progress. Please try again later.";
                        }
                        targetPage = ERROR_PAGE;
                    } else if (result == -1) {
                         errorMessage = "Failed to Add due to an internal error.";
                         targetPage = ERROR_PAGE;
                    }
                    break;

                case "editSubmit":
                    result = EmployeeTable.updateEmployee(employee); 
                    if (result == 0) {
                        // result 0 in Update means: System is Locked
                        errorMessage = "Wait, Update Database in Progress. Please try again later.";
                        targetPage = ERROR_PAGE;
                    } else if (result == -1) {
                        errorMessage = "Failed to Update due to an internal error.";
                        targetPage = ERROR_PAGE;
                    }
                    break;
            }
        } catch (NumberFormatException e) {
             errorMessage = "Input Error: Salary must be a valid number.";
             targetPage = ERROR_PAGE;
        } catch (Exception e) {
             errorMessage = "An unexpected error occurred: " + e.getMessage();
             targetPage = ERROR_PAGE;
        }

        // Set the error message if the target page is the error page
        if (targetPage.equals(ERROR_PAGE) && errorMessage != null) {
            request.setAttribute("errorMessage", errorMessage);
        }

        // Reload the list if the operation was successful (or failed without changing targetPage)
        if (!targetPage.equals(ERROR_PAGE)) {
            List<Employee> employeeList = EmployeeTable.findAllEmployee();
            request.setAttribute("employeeList", employeeList);
        }
        
        RequestDispatcher rd = request.getRequestDispatcher(targetPage);
        rd.forward(request, response);
    }
}