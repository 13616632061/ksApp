function appinit() {
    if($(window).width() == 0) {
        setTimeout(appinit, 50);
    } else {
        var aa = $(window).width() - 20;
        var hh = (aa / 16) * 9;
        //更改视频框大小
        $(".video_rect").css("width", aa + "px");
        $(".video_rect").css("height", hh + "px");
        $(".video_rect").css("min-height", hh + "px");
        $(".video_content").css("width", aa + "px");
        $(".video_content").css("height", hh + "px");
    }
}



                        $(document).ready(function() {
                            appinit();

                            $(".video_content").on("click", function() {
                                var title = $(this).attr("rel-title");
                                var src = $(this).attr("rel-src");
                                msjs.playVideo(src, title);

//                                alert(title + "-" + src);
                            });
                        });