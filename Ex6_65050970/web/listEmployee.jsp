<%@page import="Model.Employee"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Employee Management System</title>
    <meta charset="UTF-8">
</head>
<body>
    <h2>Employee List (CRUD Application with Synchronization)</h2>
    
    <p><a href="employee?action=addForm">Add New Employee</a></p>
    
    <table border="1" cellpadding="5" cellspacing="0">
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Salary</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <% 
                List<Employee> employeeList = (List<Employee>) request.getAttribute("employeeList");
                
                if (employeeList != null && !employeeList.isEmpty()) {
                    for (Employee employee : employeeList) {
            %>
            <tr>
                <td><%= employee.getEmployeeid() %></td>
                <td><%= employee.getEmployeename() %></td>
                <td><%= String.format("%,.2f", employee.getSalary()) %></td>
                <td>
                    <a href="employee?action=editForm&id=<%= employee.getEmployeeid() %>">Edit</a> |
                    <a href="employee?action=delete&id=<%= employee.getEmployeeid() %>" 
                       onclick="return confirm('Are you sure you want to delete <%= employee.getEmployeename() %>?');">Delete</a>
                </td>
            </tr>
            <% 
                    }
                } else {
            %>
            <tr>
                <td colspan="4">No employees found in the database.</td>
            </tr>
            <%
                }
            %>
        </tbody>
    </table>
</body>
</html>