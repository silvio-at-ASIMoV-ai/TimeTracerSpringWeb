<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Login</title>
    </head>
    <body>
        <h1>Login</h1>
        <form action="checkLogin" method="post">
            User Name: <input name="UserName">
            Password: <input name="Password" type="password">
            <input type="submit" value="Login">
        </form>
    </body>
</html>