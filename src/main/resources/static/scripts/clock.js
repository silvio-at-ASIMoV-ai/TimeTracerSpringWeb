window.addEventListener("load", () => {
  clock();
  function clock() {
    document.getElementById("date-time").innerHTML = "Time: " + new Date().toLocaleString("it-it");
    document.getElementById("dateTime").value = document.getElementById("date-time").innerHTML;
    setTimeout(clock, 1000);
  }
});