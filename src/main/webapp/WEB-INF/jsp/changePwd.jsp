<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Change Password</title>
  </head>
  <body>
    <h1>Change Password</h1>
        <form action="changePwd" method="post">
            <p>User Name: ${userId}
            <p>Password: <input name="pwd1" type="password">
            <p>Verify Password: <input name="pwd2" type="password">
            <p><input type="submit" value="Change Password">
            <input name="username" type="hidden" value=${userId}>
        </form>
  </body>
</html>
