<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<br/>
<img src="./res/PJAMED.png" alt="logo">
<div class="login_form">
<h1>System zarządzania przychodnią</h1>
<form id="login_form" method="post" action="login">
    <h3>Login: <input type="text" name="username"></h3><br>
    <h3>Hasło: <input type="password" name="password"></h3><br>
</form>
</div>
<button type="submit" class="subbut" form="login_form">Zaloguj</button>
</body>
</html>