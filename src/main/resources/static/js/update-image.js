/**
 * Created by franc on 9/27/2016.
 */
$("#image_form_browse").click(function () {
    $("#image_form_input").click();
});

$("#image_form_input").change(function () {
    $("#image_form_upload").remove();
    if (this.files && this.files[0]) {
        var reader = new FileReader();
        reader.onload = function(e) {
            $("#image_preview").css("background-image", "url(" + e.target.result + ")");
        }
        reader.readAsDataURL(this.files[0]);
    }
    $("#image_form").append("<button id='image_form_upload' class='btn btn-success center-block manage-img-btn' type='submit'>Upload</button>");
});