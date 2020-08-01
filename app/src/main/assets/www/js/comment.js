var g_review_page = 1;

function get_comment(id, page) {
    g_review_page = page;

    $.ajax({
        type:"GET",
        url:g_web_uri + "article/reviews?articleId=" + id + "&page=" + page + "&size=20",
        dataType:"jsonp",
        success: function(d) {
            var reviews = d.reviews;
            var l = reviews.length;

            var str = "";
              var has = l.length >= 20;
            for(var i = 0; i < l; i++) {
                var user = reviews[i].user;
                str += "<li class='item' id='reviewItem_" + reviews[i].id + "'>";
                str += '<figure class="avatar-article-thumb "><a href="javascript:;"><img src="'
                        + user.smallAvatar + '"></a></figure>';

                str += '<div class="comment-msg"><div class="msg-top"><div class="static"><div class="msg-top-header"><a href="javascript:;"><span class="usr-name">'
                        + user.nickname + '</span></a></div>';
                str += '<p class="info"><time>' + reviews[i].createdTimeFmt + '</time></p>';
                str += '</div>';
                 str += '<div class="option">';
                 str += '<span onclick="dianzanReview(' + reviews[i].id + ');" class="opt zan x-praise"><i class="ui-icon-like"></i><span class="upsNum">';
                 if(reviews[i].upsNum > 0) {
                    str += reviews[i].upsNum;
                 } else {
                    str += "&nbsp;";
                 }
                 str += '</span></span>';
                  // str += '<div class="action "><ul><li><a href="javascript:;" class="reply">回复</a></li><li><a href="javascript:;" class="report">举报</a></li></ul></div>';
                 str += '</div>';
                str += '</div>';
                str += '<div class="msg-con"><p class="title">' + reviews[i].content + '</p></div>';
                str += '</div>';

                str += '</li>';
            }

            if(page <= 1) {
                $("#review_body").html(str);
            } else {
                $("#review_body").append(str);
            }
        }
    });
}

function toReview() {
    mScroll("reviewList");
}

function dianzanReview(id) {
    msjs.dianzanReview(id);
//    alert(id);
}

function updateReviewDianzan(id, num) {
    var o = $("#reviewItem_" + id);
    var oo = o.find(".upsNum");

    if(num > 0) {
        oo.html(num);
    } else {
        oo.html("&nbsp;");
    }
//    alert(id + "-" + num);
}

$(function() {
	get_comment(g_news_id, 1);
});

