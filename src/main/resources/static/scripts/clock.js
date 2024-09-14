window.addEventListener("load", () => {
  clock();
  function clock() {
    let d = new Date(Date.now() - new Date().getTimezoneOffset() * 60000).toISOString();
    let days = d.slice(8, 10);
    let months = d.slice(5, 7);
    let years = d.slice(0, 4);
    let hours = d.slice(11, 13);
    let mins = d.slice(14, 16);
    let secs = d.slice(17, 19);
    document.getElementById("date-time").innerHTML = "Time: " + days + "/" + months + "/"
                                                    + years + " " + hours + ":" + mins + ":" + secs;
    document.getElementById("dateTime").value = document.getElementById("date-time").innerHTML;
    setTimeout(clock, 1000);
  }
});