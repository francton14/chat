/**
 * Created by franc on 9/27/2016.
 */
$(function () {
    $('[data-toggle="popover"]').popover()
});

$("#submit_update_form").click(function (event) {
    $("#update_form").submit();
});

$("#clear_update_form").click(function (event) {
    $("#update_form").trigger("reset");
});
