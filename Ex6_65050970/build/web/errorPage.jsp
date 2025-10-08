<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Operation Error</title>
    <meta charset="UTF-8">
</head>
<body>
    <h2>Operation Failed!</h2>
    
    <p style="color: red; font-weight: bold; font-size: 18px;">
        ${requestScope.errorMessage}
    </p>
    
    <hr>
    
    <p>Please check the input data and try again.</p>
    <p><a href="employee?action=list">Back to Employee List</a></p>
</body>
</html>