$(document).ready(function () {
  var date = new Date();
  var today = new Date(date.getFullYear(), date.getMonth(), date.getDate());
  todayDate(today);
});
function todayDate(today) {
  $("#datepicker").datepicker({
    dateFormat: "dd-mm-yy",
    orientation: "bottom",
  });

  $("#datepicker").datepicker("setDate", today);
}
