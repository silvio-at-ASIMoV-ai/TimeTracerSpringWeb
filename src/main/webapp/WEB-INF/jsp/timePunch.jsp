<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Time Punch</title>
  </head>
  <body>
    <h1>Access Granted</h1>
    <p/>User Id = ${userId}
    <p/>Employee Id = ${employeeId}
    <p/>${employeeName} ${employeeSurname}
    <p/>${projects}
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>ProjectName</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${projects}" var="project">
                        <tr>
                            <td>${project.id}</td>
                            <td>${project.ProjectName}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>




  </body>
</html>
