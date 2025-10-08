<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Add Employee</title>
    <meta charset="UTF-8">
</head>
<body>
    <h2>Add New Employee</h2>
    
    <form action="employee" method="POST">
        
        <input type="hidden" name="action" value="addSubmit" />
        
        <label for="id">Employee ID:</label>
        <input type="text" id="id" name="id" size="10" required />
            <br/><br/>
            
        <label for="name">Employee Name:</label>
        <input type="text" id="name" name="name" size="40" required />
            <br/><br/>
            
        <label for="salary">Salary (THB):</label>
        <input type="number" id="salary" name="salary" step="0.01" min="0" required />
            <br/><br/>
            
        <input type="submit" value="Add Employee" />
    </form>
    
    <p><a href="employee?action=list">Back to List</a></p>
</body>
</html>