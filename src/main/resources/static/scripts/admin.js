function chooseTable(whichTable) {
    if(whichTable != null) {
        document.getElementById(whichTable.toLowerCase() + "sRadio").checked = true;
    } else {
        //document.getElementById("timesRadio").checked = true;
    }
    document.getElementById("timesTable").style.display = "none";
    document.getElementById("employeesTable").style.display = "none";
    document.getElementById("usersTable").style.display = "none";
    document.getElementById("projectsTable").style.display = "none";
    document.getElementById("rolesTable").style.display = "none";
    document.getElementById("logsTable").style.display = "none";
    if(document.getElementById("timesRadio").checked) document.getElementById("timesTable").style.display = "block";
    if(document.getElementById("employeesRadio").checked) document.getElementById("employeesTable").style.display = "block";
    if(document.getElementById("usersRadio").checked) document.getElementById("usersTable").style.display = "block";
    if(document.getElementById("projectsRadio").checked) document.getElementById("projectsTable").style.display = "block";
    if(document.getElementById("rolesRadio").checked) document.getElementById("rolesTable").style.display = "block";
    if(document.getElementById("logsRadio").checked) document.getElementById("logsTable").style.display = "block";
}