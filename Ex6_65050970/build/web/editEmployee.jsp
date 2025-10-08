<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Employee</title>
    <meta charset="UTF-8">
</head>
<body>
    <h2>Edit Employee: ${requestScope.employee.employeeid}</h2>
    
    <form action="employee" method="POST">
        
        <input type="hidden" name="action" value="editSubmit" />
        
        <label for="id">Employee ID:</label>
        <input type="text" id="id" name="id" value="${requestScope.employee.employeeid}" size="10" readonly />
            <br/><br/>
            
        <label for="name">Employee Name:</label>
        <input type="text" id="name" name="name" value="${requestScope.employee.employeename}" size="40" required />
            <br/><br/>
            
        <label for="salary">Salary (THB):</label>
        <input type="number" id="salary" name="salary" value="${requestScope.employee.salary}" step="0.01" min="0" required />
            <br/><br/>
            
        <input type="submit" value="Save Changes" />
    </form>
    
    <p><a href="employee?action=list">Cancel and Back to List</a></p>
</body>
</html>