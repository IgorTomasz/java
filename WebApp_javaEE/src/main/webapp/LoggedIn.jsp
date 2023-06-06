
<%--
  Created by IntelliJ IDEA.
  User: Igor
  Date: 28.05.2023
  Time: 20:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"  %>
<html>
<head>
    <title>Profil użytkownika</title>
    <link rel="stylesheet" href="styleLogged.css">
</head>
<body>
<div class="screen">
    <div class="left_pan">
        <img src="res/PJAMED.png" alt="logo">
        <div class="left_pan_inner">
            <a href="search" >Wyszukaj pacjentów po statusie</a>
            <br/>
            <br/>
            <a href="visit">Wyszukaj wolne wizyty</a>
            <br/>
            <br/>
            <a href="addPatient">Dodaj nowego pacjenta</a>
            <br/>
            <br/>
            <a href="searchPatient">Wyszukaj pacjenta</a>
        </div>

    </div>
    <div class="middle">
            ${message}
    </div>
</div>
</body>
<script>
    const value = document.querySelector("#value")
    const input = document.querySelector("#inp")
    value.textContent = input.value
    input.addEventListener("input", (event) => {
        value.textContent = event.target.value
    })

</script>
</html>
