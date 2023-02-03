/* 비동기 방식의 중복확인 구현*/
let timer2;
let timerOfPassword;

/*userRequestDto to server using ajax*/
function commonAjax(urlName , formDataObject , method) {

    //console.log(JSON.stringify(formDataObject));

    $.ajax({
        type : method,
        url: urlName,
        data : JSON.stringify(formDataObject), // http body에 넣을 데이터
        contentType:"application/json; charset=utf-8" , // body data type : json
    }).done(function(result , textStatus , xhr ) {
        // 응답 성공 시 로직
        window.location = xhr.getResponseHeader("Location");
    }).fail(function(error){
        // 응담 요청 실패 시 로직
        console.log(error);

    });
}

$.fn.serializeObject = function(){
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};
