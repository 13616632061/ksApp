/**
 * Created by wjj on 16/1/2.
 */

$(function() {
    var all_src = new Array();
    var pic_index = 0;
    $("img").each(function() {
        all_src.push($(this).attr("src"));
        $(this).attr("data-index", pic_index);
        pic_index++;
    });
    var all_pic_list = all_src.join("##");
    $("img").each(function() {
        $(this).on("click", function() {
            var index = $(this).attr("data-index");
            msjs.playPic(all_pic_list, index);
        });
    });
});

function mScroll(id){
    $("html,body").stop(true);
    // $("html,body").animate({scrollTop: $("#"+id).offset().top}, 1000);
    $("html,body").animate({scrollTop: $("#"+id).offset().top}, 0);
}