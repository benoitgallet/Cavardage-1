<%--
  Created by IntelliJ IDEA.
  User: rety
  Date: 25/10/17
  Time: 18:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Cavardage ~ Inscription ~</title>
</head>
<body>
<div>
    <form action="ControleurAnonyme" method="post">
        <label>Login:</label>
        <input type="text" name="login"/>
        <label>Nom::</label>
        <input type="text" name="nom"/>
        <label>Mot de passe:</label>
        <input type="password" name="mdp"/>
        <label>Confirmer le mot de passe:</label>
        <input type="password" name="mdp_confirmer"/>
        <button type="submit" name="afaire" value="inscrire">Créer</button>
    </form>
</div>
</body>
</html>
